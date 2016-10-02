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
 * <p>
 * if error in using this util, may lose exception stack
 *
 * @author guohang.ding on 16-10-2
 */
@SuppressWarnings("unused")
public class LogUtil {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogUtil.class);

    private static Logger logger;
    /** 这个具体是什么属性还需要确认一下，目前直接使用的泽哥的配置 */
    private static final JsonMapper MAPPER = MapperBuilder.create().enable(JsonFeature.INCLUSION_NOT_NULL).build();

    private static void build(Logger logger) {
        LogUtil.logger = logger == null ? NORMAL_LOGGER : logger;
    }

    /**
     * log trace
     * ---isTraceEnabled
     * ---serialize params
     *
     * @param logger   real logger
     * @param template string template, normal is empty
     * @param params   params, can be empty
     */
    public static void trace(Logger logger, String template, Object... params) {
        build(logger);
        if (!getLogger().isTraceEnabled()) {
            return;
        }
        log(LogLevel.Trace, template, serialize(params));
    }

    /**
     * log debug
     * ---isDebugEnabled
     * ---serialize params
     *
     * @param logger   real logger
     * @param template string template, normal is empty
     * @param params   params, can be empty
     */
    public static void debug(Logger logger, String template, Object... params) {
        build(logger);
        if (!getLogger().isDebugEnabled()) {
            return;
        }
        log(LogLevel.Debug, template, serialize(params));
    }

    /**
     * log info
     * ---isInfoEnabled
     * ---serialize params
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
        log(LogLevel.Info, template, serialize(params));
    }

    /**
     * log warn
     * ---warnEnabled
     * ---serialize params
     *
     * @param logger   real logger
     * @param template string template, normal is empty
     * @param params   params, can be empty
     */
    public static void warn(Logger logger, String template, Object... params) {
        build(logger);
        if (!getLogger().isWarnEnabled()) {
            return;
        }
        log(LogLevel.Warn, template, serialize(params));
    }

    /**
     * log error
     * ---errorEnabled
     * ---serialize params
     *
     * @param logger   real logger
     * @param template string template, normal is empty
     * @param params   params, the last is throwable
     */
    public static void error(Logger logger, String template, Object... params) {
        build(logger);
        if (!getLogger().isErrorEnabled()) {
            return;
        }
        log(LogLevel.Error, template, serialize(params));
    }

    private static Object[] serialize(Object... params) {
        if (params != null && params.length > 0) {
            Object[] trimmed = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                trimmed[i] = params[i] instanceof Throwable ? params[i] : serialize(params[i]);
            }
            return trimmed;
        }
        return null;
    }

    private static String serialize(Object param) {
        return MAPPER.writeValueAsString(param);
    }

    /**
     * choose log method by level
     */
    private static void log(LogLevel level, String template, Object... params) {
        switch (level) {
            case Trace:
                logger.trace(template, params);
                break;
            case Debug:
                logger.debug(template, params);
                break;
            case Info:
                logger.info(template, params);
                break;
            case Warn:
                logger.warn(template, params);
                break;
            case Error:
                logger.error(template, params);
                break;
            default:
                logger.info(template, params);
                break;
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    private enum LogLevel {
        Trace,
        Debug,
        Info,
        Warn,
        Error
    }
}
