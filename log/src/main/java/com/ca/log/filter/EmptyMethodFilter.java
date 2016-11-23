package com.ca.log.filter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * @author guohang.ding on 16-10-21
 */
@Service
public class EmptyMethodFilter extends ComparableMethodFilter {

    @Override
    public void preHandle(ProceedingJoinPoint pjp) {

    }

    @Override
    public void postHandle(ProceedingJoinPoint pjp) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
