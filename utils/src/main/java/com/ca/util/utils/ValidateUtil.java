package com.ca.util.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 参数校验工具
 * Spring的参数校验好像就是基于Hibernate的Validator实现类
 * 在寻找一个直接调用一下，就可以触发参数校验的方式……但是没找到
 * 自己写了一个依赖于Hibernate的，很麻烦的参数校验
 *
 * @author guohang.ding on 16-11-8
 */
public class ValidateUtil {

    private static final ConstraintHelper helper = new ConstraintHelper();
    private static final Logger LOG = LoggerFactory.getLogger(ValidateUtil.class);
    private static final String PARAMS_INVALID = "Params_Invalid";
    public static final String SYSTEM_ERROR = "System_Error";

    private static final ConcurrentHashMap<Class<? extends ConstraintValidator<? extends Annotation, ?>>,
            ConstraintValidator> cache = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    public static void checkRequest(Object request) {
        checkArgument(request != null, PARAMS_INVALID);
        boolean valid = true;
        LOG.info("validate request: {}", request);

        Field[] fields = request.getClass().getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            LOG.info("empty fields for request: {}", request.getClass());
            return;
        }

        Annotation[] annotations;
        for (Field field : fields) {
            annotations = field.getDeclaredAnnotations();
            if (ArrayUtils.isEmpty(annotations)) {
                continue;
            }
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getAnnotation(Constraint.class) == null) {
                    continue;
                }
                if (CollectionUtils.isEmpty(helper.getAllValidatorClasses(annotation.annotationType()))) {
                    continue;
                }
                for (Class<? extends ConstraintValidator<? extends Annotation, ?>> clazz
                        : helper.getAllValidatorClasses(annotation.annotationType())) {
                    try {
                        ConstraintValidator validator = cache.get(clazz);
                        if (validator == null) {
                            LOG.info("init validator: {}", clazz);
                            validator = clazz.newInstance();
                            cache.putIfAbsent(clazz, validator);
                        }
                        validator.initialize(annotation);
                        field.setAccessible(true);
                        valid = validator.isValid(field.get(request), null);
                        if (!valid) {
                            LOG.error("invalid params, annotation: {}", annotation);
                            break;
                        }
                    } catch (Exception e) {
                        LOG.error("validator validate error", e);
                        throw new IllegalArgumentException(SYSTEM_ERROR);
                    }
                }
                checkArgument(valid, PARAMS_INVALID);
            }
        }
    }
}
