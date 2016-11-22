package com.qunar.guohang.processor;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * @author guohang.ding on 16-10-18
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String encode(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
            //ignore
        }
        return o.toString();
    }
}
