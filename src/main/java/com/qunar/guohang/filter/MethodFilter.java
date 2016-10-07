package com.qunar.guohang.filter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;

/**
 * 基于AOP实现的，方法层级的Filter，用于实现自定义性能监控
 * 继承Spring的Ordered来实现排序，更高的Order，意味着更低的优先级
 * 如果Order相同，使用名称字典序
 *
 * @author guohang.ding on 2016/10/7.
 */
public interface MethodFilter extends Ordered {

    public void preHandle(ProceedingJoinPoint pjp);

    public void postHandle(ProceedingJoinPoint pjp);
}
