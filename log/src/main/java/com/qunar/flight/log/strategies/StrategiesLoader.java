package com.qunar.flight.log.strategies;

import java.util.ServiceLoader;

/**
 * Strategies Loader
 * Use ServiceLoader now
 * Is extensionLoader better?
 *
 * @author guohang.ding on 16-11-9
 */
public class StrategiesLoader {

    private static final ServiceLoader<ParamStrategy> loader = ServiceLoader.load(ParamStrategy.class);

    public static Iterable<ParamStrategy> getStrategies() {
        return loader;
    }
}
