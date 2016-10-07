package com.qunar.guohang.filter;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

/**
 * Comparator for method filters
 *
 * @author guohang.ding on 2016/10/7.
 */
public class MethodFilterComparator implements Comparator<MethodFilter> {

    private static MethodFilterComparator INSTANCE = null;
    private static final Object LOCK = new Object();

    private MethodFilterComparator() {}

    public static MethodFilterComparator getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new MethodFilterComparator();
                return INSTANCE;
            }
        }

        return INSTANCE;
    }

    /**
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    @Override
    public int compare(MethodFilter f1, MethodFilter f2) {
        // null means lowest priority
        if (f1 == null) {
            return f2 == null ? 0 : -1;
        }

        // order value is greater means lower priority
        if (f1.getOrder() != f2.getOrder()) {
            return f1.getOrder() > f2.getOrder() ? -1 : 1;
        }

        // name value is greater means highest priority
        if (!StringUtils.equals(f1.getClass().getName(), f2.getClass().getName())) {
            return f1.getClass().getName().compareTo(f2.getClass().getName());
        }

        // default is arbitrary positions
        return -1;
    }
}
