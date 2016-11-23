package com.ca.log.performance;

import com.ca.log.filter.MethodFilter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关注性能
 * 主要关注方法耗时
 * <p>
 * 可以配置流程块，监控关注的流程块的耗时
 * 注意：只关注【流程块】中方法（配置了注解）的耗时，不关注【流程】中其它方法的耗时
 * <p>
 * 允许一次请求中出现多个流程块重叠，但是某个方法只能作为某一个流程块的成员 -> 所以这个设定蛮鸡肋的
 * 但是想象不出有什么场景需要多个流程块重叠
 *
 * updated by guohang.ding on 16-10-6
 * 通过实现MethodFilter进行监控扩展，使用exclusion属性进行filter的屏蔽
 *
 * @author guohang.ding on 16-10-2
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogPerformance {

    /** 流程块，默认不关注流程块 */
    String flow() default "";

    /** 该方法是否是流程块最后一个方法 */
    boolean finish() default true;

    /** 该方法不使用哪些Filter进行监控 */
    Class<? extends MethodFilter>[] exclusion() default {};
}
