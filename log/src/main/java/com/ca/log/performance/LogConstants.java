package com.ca.log.performance;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ca.log.strategies.ParamStrategy;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

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

    private static ConcurrentMap<Class<? extends ParamStrategy>, ParamStrategy> strategies = Maps.newConcurrentMap();

    public static final Joiner JOINER = Joiner.on("_");
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
        return Collections.unmodifiableList(Lists.newArrayList(strategies.values()));
    }
}
