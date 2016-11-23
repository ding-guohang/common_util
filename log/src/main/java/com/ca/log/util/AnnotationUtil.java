package com.ca.log.util;

import com.ca.log.LogService;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Annotation Util
 * to get annotation on method
 *
 * @author guohang.ding on 16-10-4
 */
public class AnnotationUtil {

    private static final LogService log = LogService.create(AnnotationUtil.class);

    /**
     * 只能通过这种方式获取正确的注解
     * 因为某些时候获取的方法签名是接口的方法签名，无法获得注解
     */
    public static Annotation getAnnotation(ProceedingJoinPoint pjp, Class tClass, Method realMethod) {
        Annotation annotation = null;
        try {
            Class<?> aClass = pjp.getTarget().getClass();
            Method[] methods = aClass.getMethods();
            for (Method m : methods) {
                if (!MethodUtil.equals(m, realMethod)) {
                    continue;
                }
                annotation = m.getAnnotation(tClass);
                if (annotation != null) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("find annotation fail", e);
        }
        return annotation;
    }
}
