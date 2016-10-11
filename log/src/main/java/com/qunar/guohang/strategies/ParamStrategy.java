package com.qunar.guohang.strategies;

/**
 * Strategy for params' modification in log
 *
 * @author guohang.ding on 16-10-11
 */
public interface ParamStrategy {

    /**
     * modify params in log
     * such as encrypt or decrypt
     *
     * @return Object[] for other strategies and serializeUtil
     */
    Object[] modify(Object... params);
}
