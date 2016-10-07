package com.qunar.guohang.performance;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.flight.qmonitor.QMonitor;
import com.qunar.guohang.filter.MethodFilter;
import com.qunar.guohang.filter.MethodFilterComparator;
import com.qunar.guohang.log.LogService;
import com.qunar.guohang.util.AnnotationUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 记录方法和流的耗时
 *
 * @author guohang.ding on 16-10-4
 */
@Aspect
@Order(Integer.MAX_VALUE)
@Service
public class LogPerformanceAspect {

    private static final LogService log = LogService.create(LogPerformanceAspect.class);

    @Resource
    private List<MethodFilter> filters; // TODO SPI or @Resource ? All of these depends on Spring...

    @Around("@annotation(LogPerformance)")
    public Object logPerformance(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        LogPerformance annotation = (LogPerformance) AnnotationUtil.getAnnotation(point, LogPerformance.class, method);

        if (annotation == null) {
            return point.proceed();
        }

        long start = System.currentTimeMillis();
        List<MethodFilter> filterList = exclusion(filters, annotation);
        preHandle(filterList, point);

        Object ret = point.proceed();

        postHandle(filterList, point);
        logAndMonitor(annotation, start, method, point.getTarget().getClass().getSimpleName());
        return ret;
    }

    private void logAndMonitor(LogPerformance annotation, long start, Method method, String className) {
        String flow = annotation.flow();
        boolean finish = annotation.finish();
        String name = LogConstants.JOINER.skipNulls().join(className, method.getName());
        long cost = System.currentTimeMillis() - start;

        // 记录方法耗时
        doLogAndMonitor(name, cost);
        // 记录流耗时
        if (StringUtils.isNotBlank(flow)) {
            if (finish) {
                doLogAndMonitor(LogConstants.JOINER.join(flow, name), LogConstants.getFlow(flow) + cost);
                LogConstants.clear(flow);
            } else {
                // 增加流耗时
                LogConstants.setFlow(flow, cost);
            }
        }
    }

    private void doLogAndMonitor(String name, long cost) {
        log.info(LogConstants.COST_FORMAT, name, cost);
        QMonitor.recordOne(name, cost);
    }

    private void preHandle(List<MethodFilter> filters, ProceedingJoinPoint point) {
        Collections.sort(filters, MethodFilterComparator.getInstance());

        for (MethodFilter filter : filters) {
            filter.preHandle(point);
        }
    }

    private void postHandle(List<MethodFilter> filters, ProceedingJoinPoint point) {
        for (MethodFilter filter : filters) {
            filter.postHandle(point);
        }
    }

    private List<MethodFilter> exclusion(List<MethodFilter> filters, LogPerformance logPerformance) {
        Class<? extends MethodFilter>[] exclusions = logPerformance.exclusion();
        if (exclusions == null || exclusions.length == 0) {
            return filters;
        }

        Map<Class<? extends MethodFilter>, MethodFilter> map = Maps.newHashMap();
        for (MethodFilter filter : filters) {
            map.put(filter.getClass(), filter);
        }
        for (Class c : exclusions) {
            if (map.containsKey(c)) {
                map.remove(c);
            }
        }

        return Lists.newArrayList(map.values());
    }
}
