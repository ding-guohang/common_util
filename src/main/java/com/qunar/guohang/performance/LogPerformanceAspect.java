package com.qunar.guohang.performance;

import com.qunar.flight.qmonitor.QMonitor;
import com.qunar.guohang.log.LogService;
import com.qunar.guohang.util.AnnotationUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * 记录方法和流的耗时
 * TODO 在FlowBlock中应该记录开始时间更好一点
 *
 * @author guohang.ding on 16-10-4
 */
@Aspect
@Order(Integer.MAX_VALUE)
@Service
public class LogPerformanceAspect {

    private static final LogService log = LogService.create(LogPerformanceAspect.class);

    @Around("@annotation(LogPerformance)")
    public Object logPerformance(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        LogPerformance annotation = (LogPerformance) AnnotationUtil.getAnnotation(point, LogPerformance.class, method);

        if (annotation == null) {
            return point.proceed();
        }

        long start = System.currentTimeMillis();
        Object ret = point.proceed();
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
}
