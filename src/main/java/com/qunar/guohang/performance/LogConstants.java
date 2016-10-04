package com.qunar.guohang.performance;

import com.google.common.base.Joiner;

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
}
