package com.qunar.guohang.util;

import qunar.api.json.JsonFeature;
import qunar.api.json.JsonMapper;
import qunar.api.json.MapperBuilder;

/**
 * @author guohang.ding on 16-10-11
 */
public class SerializeUtil {

    /** 这个具体是什么属性还需要确认一下，目前直接使用的泽哥的配置 */
    private static final JsonMapper MAPPER = MapperBuilder.create().enable(JsonFeature.INCLUSION_NOT_NULL).build();

    public static Object[] serialize(Object... params) {
        if (params != null && params.length > 0) {
            Object[] trimmed = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                trimmed[i] = params[i] instanceof Throwable ? params[i] : serialize(params[i]);
            }
            return trimmed;
        }
        return null;
    }

    public static String serialize(Object param) {
        return MAPPER.writeValueAsString(param);
    }
}
