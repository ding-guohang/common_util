package com.qunar.guohang.log.performance;

import com.google.common.base.Joiner;
import com.qunar.guohang.log.strategies.ParamStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Constants for log and performance
 *
 * @author guohang.ding on 16-10-4
 */
public class LogConstants {

    private static ThreadLocal<FlowBlock> flow = new ThreadLocal<FlowBlock>() {
        @Override
        protected FlowBlock initialValue() {
            return new FlowBlock();
        }
    };

    private static ConcurrentHashMap<Class<? extends ParamStrategy>, ParamStrategy> strategies = new ConcurrentHashMap<Class<? extends ParamStrategy>, ParamStrategy>();

    public static final String SEPARATOR = "_";
    public static final Joiner JOINER = Joiner.on(SEPARATOR);
    public static final String COST_FORMAT = "{} cost {}";

    public static void setFlow(String name, long cost) {
        flow.get().setBlock(name, cost);
    }

    public static Long getFlow(String name) {
        Long cost = flow.get().getBlock(name);
        return cost == null ? 0 : cost;
    }

    public static void clear(String name) {
        flow.get().remove(name);
    }

    /**
     * 允许用户在使用的时候，自定义自己的参数处理策略并加入到处理过程中
     */
    public static void addStrategy(ParamStrategy strategy) {
        if (strategy != null) {
            strategies.putIfAbsent(strategy.getClass(), strategy);
        }
    }

    public static List<ParamStrategy> buildStrategies() {
        return Collections.unmodifiableList(new ArrayList<ParamStrategy>(strategies.values()));
    }
}
