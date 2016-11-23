package com.ca.log.performance;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Block in flow
 *
 * @author guohang.ding on 16-10-4
 */
public class FlowBlock {

    private Map<String, Long> map = Maps.newHashMap();

    public void setBlock(String name, long cost) {
        if (map.containsKey(name)) {
            map.put(name, map.get(name) + cost);
        } else {
            map.put(name, cost);
        }
    }

    public Long getBlock(String name) {
        return map.get(name);
    }

    public void remove(String name) {
        map.remove(name);
    }
}
