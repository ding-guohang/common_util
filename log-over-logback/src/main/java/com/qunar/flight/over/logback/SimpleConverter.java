package com.qunar.flight.over.logback;


import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * @author guohang.ding on 16-11-10
 */
public class SimpleConverter extends LineOfCallerConverter {

    private static final String FQCN = com.qunar.flight.log.LogService.class.getName();
    private static final String FQCN_BY_UTIL = com.qunar.flight.log.LogUtil.class.getName();

    @Override
    public String convert(ILoggingEvent le) {
        if (!(le instanceof LoggingEvent)) {
            return super.convert(le);

        }
        if (!checkIsService(FQCN, FQCN_BY_UTIL)) {
            return super.convert(le);
        }
        StackTraceElement[] cda = getCallerData((LoggingEvent) le);
        if (cda != null && cda.length > 0) {
            return Integer.toString(cda[0].getLineNumber());
        } else {
            return CallerData.NA;
        }
    }

    private boolean checkIsService(String class1, String class2) {
        StackTraceElement[] steArray = new Throwable().getStackTrace();
        // check if LogService or LogUtil
        for (StackTraceElement aSteArray : steArray) {
            if (class1.equals(aSteArray.getClassName())
                    || class2.equals(aSteArray.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private StackTraceElement[] getCallerData(LoggingEvent event) {
        if (event.hasCallerData()) {
            StackTraceElement[] array = extract(new Throwable(), FQCN, FQCN_BY_UTIL,
                    ClassicConstants.DEFAULT_MAX_CALLEDER_DATA_DEPTH);
            event.setCallerData(array);
        }
        return event.getCallerData();
    }

    private StackTraceElement[] extract(Throwable t, String fqnOfInvokingClass,
                                        String fqnOfInvokingClass2, final int maxDepth) {
        if (t == null) {
            return null;
        }

        StackTraceElement[] steArray = t.getStackTrace();
        StackTraceElement[] callerDataArray;

        int found = CallerData.LINE_NA;
        for (int i = 0; i < steArray.length; i++) {
            if (CallerData.isDirectlyInvokingClass(steArray[i].getClassName(),
                    fqnOfInvokingClass) || fqnOfInvokingClass.equals(fqnOfInvokingClass2)) {
                // the caller is assumed to be the next stack frame, hence the +1.
                found = i + 1;
            } else {
                if (found != CallerData.LINE_NA) {
                    break;
                }
            }
        }

        // we failed to extract caller data
        if (found == CallerData.LINE_NA) {
            return CallerData.EMPTY_CALLER_DATA_ARRAY;
        }

        int availableDepth = steArray.length - found;
        int desiredDepth = maxDepth < (availableDepth) ? maxDepth : availableDepth;

        callerDataArray = new StackTraceElement[desiredDepth];
        for (int i = 0; i < desiredDepth; i++) {
            callerDataArray[i] = steArray[found + i];
        }
        return callerDataArray;
    }
}
