package com.qunar.guohang.filter;

import org.apache.commons.lang3.StringUtils;

/**
 * Comparable Filter
 * Is it better than MethodFilterComparator?
 *
 * @author guohang.ding on 16-10-13
 */
public abstract class ComparableMethodFilter implements MethodFilter, Comparable<ComparableMethodFilter> {

    @Override
    public final int compareTo(ComparableMethodFilter o) {
        // null means lowest priority, it can be instead of Ordering.nullsLast

        // order value is greater means lower priority
        if (this.getOrder() != o.getOrder()) {
            return this.getOrder() > o.getOrder() ? 1 : -1;
        }

        // name value is greater means highest priority
        if (!StringUtils.equals(this.getClass().getName(), o.getClass().getName())) {
            return o.getClass().getName().compareTo(this.getClass().getName());
        }

        // default is arbitrary positions
        return -1;
    }
}
