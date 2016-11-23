package com.ca.log.filter;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Comparator for method filters
 *
 * @author guohang.ding on 2016/10/7.
 */
public final class MethodFilterComparator {


    private MethodFilterComparator() {
    }

    public static void sort(List<MethodFilter> collection) {
        Collections.sort(collection, FilterComparator.getInstance());
    }

    private static final class FilterComparator implements Comparator<MethodFilter> {

        private static volatile FilterComparator INSTANCE = null;
        private static final Object LOCK = new Object();

        private FilterComparator() {
        }

        private static FilterComparator getInstance() {
            if (INSTANCE == null) {
                synchronized (LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = new FilterComparator();
                        return INSTANCE;
                    }
                }
            }

            return INSTANCE;
        }

        /**
         * greater value means lower priority
         *
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         */
        @Override
        public final int compare(MethodFilter f1, MethodFilter f2) {
            // null means lowest priority, it can be instead of Ordering.nullsLast
            if (f1 == null) {
                return f2 == null ? 0 : 1;
            }

            // order value is greater means lower priority
            if (f1.getOrder() != f2.getOrder()) {
                return f1.getOrder() > f2.getOrder() ? 1 : -1;
            }

            // name value is greater means highest priority
            if (!StringUtils.equals(f1.getClass().getName(), f2.getClass().getName())) {
                return f2.getClass().getName().compareTo(f1.getClass().getName());
            }

            // default is arbitrary positions
            return -1;
        }
    }
}
