package com.ca.log.util;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * Method Util
 *
 * @author guohang.ding on 16-9-30
 */
public class MethodUtil {

    /**
     * check method.equals ignore interface
     */
    public static boolean equals(@NotNull Method m1, @NotNull Method m2) {
        if (!m1.getName().equals(m2.getName())) {
            return false;
        }

        if (m1.getReturnType() != m2.getReturnType()) {
            return false;
        }

        Class<?>[] params1 = m1.getParameterTypes();
        Class<?>[] params2 = m2.getParameterTypes();
        if (params1.length != params2.length) {
            return false;
        }

        for (int i = 0; i < params1.length; i++) {
            if (params1[i] != params2[i])
                return false;
        }
        return true;
    }
}
