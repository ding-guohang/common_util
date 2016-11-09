package com.qunar.flight.log.strategies;

import com.google.common.collect.Lists;

import java.util.List;
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

    private static List<ParamStrategy> strategies = null;
    private static final Object LOCK = new Object();

    public static Iterable<ParamStrategy> getStrategies() {
        if (strategies == null) {
            synchronized (LOCK) {
                if (strategies == null) {
                    strategies = Lists.newArrayList(loader);
                }
            }
        }
        return strategies;
    }
}
