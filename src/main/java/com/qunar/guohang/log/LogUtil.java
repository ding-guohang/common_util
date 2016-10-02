package com.qunar.guohang.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.api.json.JsonFeature;
import qunar.api.json.JsonMapper;
import qunar.api.json.MapperBuilder;

/**
 * Normal Util for logging
 * <p>
 * To log as json
 * To log after enable
 * To log performance
 *
 * @author guohang.ding on 16-10-2
 */
public class LogUtil {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogUtil.class);

    private static Logger logger;
    /** 这个具体是什么属性还需要确认一下，目前直接使用的泽哥的配置 */
    private static final JsonMapper MAPPER = MapperBuilder.create().enable(JsonFeature.INCLUSION_NOT_NULL).build();

    public static void build(Logger logger) {
        LogUtil.logger = logger == null ? NORMAL_LOGGER : logger;
    }

    /**
     * log info
     *
     * @param logger   real logger
     * @param template string template, normal is empty
     * @param params   params, can be empty
     */
    public static void info(Logger logger, String template, Object... params) {
        build(logger);
        if (!getLogger().isInfoEnabled()) {
            return;
        }
        info(template, params);
    }

    private static void info(String template, Object... params) {
        logger.info(template, params);
    }

    private static String seriliaze(Object param) {
        return MAPPER.writeValueAsString(param);
    }

    public static Logger getLogger() {
        return logger;
    }
}
