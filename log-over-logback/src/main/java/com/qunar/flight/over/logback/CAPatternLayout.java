package com.qunar.flight.over.logback;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author guohang.ding on 16-11-10
 */
public class CAPatternLayout extends PatternLayout {

    static {
        defaultConverterMap.put("L", RealLineConverter.class.getName());
        defaultConverterMap.put("line", RealLineConverter.class.getName());
    }
}
