package com.ca.log.performance;

import com.ca.log.Invoker;
import com.ca.log.LogService;
import com.ca.log.filter.FilterChain;
import com.ca.log.filter.MethodFilter;
import com.ca.log.monitor.DMonitor;
import com.ca.log.util.AnnotationUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

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
    private List<MethodFilter> filters; // TODO SPI or @Resource ? All of these depend on Spring...

    @Around("@annotation(com.ca.log.performance.LogPerformance)")
    public Object logPerformance(final ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        LogPerformance annotation = (LogPerformance) AnnotationUtil.getAnnotation(point, LogPerformance.class, method);

        if (annotation == null) {
            return point.proceed();
        }

        long start = System.currentTimeMillis();
        Invoker<Object, ProceedingJoinPoint> invoker = new Invoker<Object, ProceedingJoinPoint>() {
            @Override
            public Object invoke(ProceedingJoinPoint o) throws Throwable {
                return o.proceed();
            }
        };

        FilterChain<Object> chain = FilterChain.init(filters, new Exclusion(annotation));
        chain.buildFilterChain(invoker);
        Object ret = invoker.invoke(point);

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
        DMonitor.recordOne(name, cost);
    }

    public static class Exclusion {

        private LogPerformance logPerformance;

        @SuppressWarnings("all")
        public Exclusion(LogPerformance logPerformance) {
            this.logPerformance = logPerformance;
        }

        public List<MethodFilter> exclusion(List<MethodFilter> filters) {
            Class<? extends MethodFilter>[] exclusions = logPerformance.exclusion();
            if (exclusions.length == 0) {
                return filters;
            }

            /*
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
            */
            // maybe 'Iterables.filter' is better? but i don't need to lazy it...
            final Set<Class<? extends MethodFilter>> set = Sets.newHashSet(exclusions);
            return Lists.newArrayList(Iterables.filter(filters, new Predicate<MethodFilter>() {
                @Override
                public boolean apply(MethodFilter methodFilter) {
                    return methodFilter != null && !set.contains(methodFilter.getClass());
                }
            }));
        }
    }
}
