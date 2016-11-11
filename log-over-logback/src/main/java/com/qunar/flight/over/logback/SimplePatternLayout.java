package com.qunar.flight.over.logback;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author guohang.ding on 16-11-10
 */
public class SimplePatternLayout extends PatternLayout {

    static {
        defaultConverterMap.put("L", SimpleConverter.class.getName());
        defaultConverterMap.put("line", SimpleConverter.class.getName());
    }
}
