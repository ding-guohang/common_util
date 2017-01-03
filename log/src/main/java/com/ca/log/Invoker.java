package com.ca.log;

/**
 * @author guohang.ding on 17-1-3
 */
public interface Invoker<T, K> {

    T invoke(K k) throws Throwable;
}
