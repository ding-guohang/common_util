package com.qunar.flight.log.util;

import com.qunar.flight.log.strategies.ParamStrategy;
import qunar.api.json.JsonMapper;
import qunar.api.json.MapperBuilder;

/**
 * Json序列化策略
 * <p>
 * 使用JsonFeature，Long型mask，每一位对应一个策略的使用
 *
 * @see qunar.api.json.JsonFeature
 * enable就是或，disable就是与非
 *
 * @author guohang.ding on 16-10-11
 */
public class SerializeStrategy implements ParamStrategy {

    /** default features */
    private final JsonMapper MAPPER = MapperBuilder.getDefaultMapper();

    private Object serialize(Object param) {
        return MAPPER.writeValueAsString(param);
    }

    @Override
    public Object[] modify(Object... params) {
        if (params != null && params.length > 0) {
            Object[] trimmed = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                trimmed[i] = (params[i] instanceof Throwable || params[i] instanceof String) ?
                        params[i] : serialize(params[i]);
            }
            return trimmed;
        }
        return null;
    }
}
