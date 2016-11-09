package com.qunar.flight.log.strategies;

import java.util.ServiceLoader;

/**
 * Strategies Loader
 * Use ServiceLoader now
 * Is the way of ExtensionLoader better?
 *
 * @author guohang.ding on 16-11-9
 */
public class StrategiesLoader {

    /**
     * 基于Iterable、LazyIterator实现懒加载
     * 加载就是读文件、实例化
     * 基于LinkedHashMap做缓存
     * 非线程安全
     */
    private static final ServiceLoader<ParamStrategy> loader = ServiceLoader.load(ParamStrategy.class);

    public static Iterable<ParamStrategy> getStrategies() {
        return loader;
    }
}
