package com.ca.log.filter;

import com.ca.log.Invoker;
import com.ca.log.performance.LogPerformanceAspect;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * chain
 *
 * @author guohang.ding on 17-1-3
 */
public class FilterChain<T> {

    private List<MethodFilter> filters;

    private FilterChain() {

    }

    public static <T> FilterChain<T> init(List<MethodFilter> list, LogPerformanceAspect.Exclusion exclusion) {
        FilterChain<T> chain = new FilterChain<>();

        if (CollectionUtils.isEmpty(list)) {
            chain.filters = Lists.newArrayListWithExpectedSize(0);
            return chain;
        }

        chain.filters = Lists.newArrayList(list);
        // Can do it also by Ordering
        // Collections.sort(filters, Ordering.<ComparableMethodFilter>natural().nullsFirst());
        MethodFilterComparator.sort(exclusion.exclusion(chain.filters));
        return chain;
    }

    public Invoker<T, ProceedingJoinPoint> buildFilterChain(Invoker<T, ProceedingJoinPoint> invoker) {
        Invoker<T, ProceedingJoinPoint> last = invoker;

        for (int i = filters.size() - 1; i >= 0; i--) {
            final MethodFilter filter = filters.get(i);
            final Invoker<T, ProceedingJoinPoint> next = last;
            last = new Invoker<T, ProceedingJoinPoint>() {
                @Override
                public T invoke(ProceedingJoinPoint point) throws Throwable {
                    filter.preHandle(point);
                    T ret = next.invoke(point);
                    filter.postHandle(point);
                    return ret;
                }
            };
        }

        return last;
    }
}
