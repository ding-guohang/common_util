package com.qunar.flight.log.strategies;

import org.slf4j.helpers.FormattingTuple;

/**
 * Strategy for params' modification in log
 * <p>
 * 手动替换{}和参数吧，SLF4J有这个功能
 * <p>
 * 还有问题是，这个操作发生在实现类之前，比如logback的filter，也许应该过滤掉，但是却浪费了资源去处理参数
 *
 * @author guohang.ding on 16-10-11
 * @see FormattingTuple ft = MessageFormatter.arrayFormat(message, argArray);
 */
public interface ParamStrategy {

    /**
     * modify param in log
     * such as encrypt or decrypt
     *
     * @return Object for other strategies and serializeUtil
     */
    Object[] modify(Object... param);
}
