package com.qunar.guohang.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.qunar.guohang.util.SerializeUtil.serialize;

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
public class LogUtil {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogUtil.class);

    private static Logger build(Logger logger) {
        return logger == null ? NORMAL_LOGGER : logger;
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
        logger = build(logger);
        if (!logger.isTraceEnabled()) {
            return;
        }
        log(LogLevel.Trace, logger, template, serialize(params));
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
        logger = build(logger);
        if (!logger.isDebugEnabled()) {
            return;
        }
        log(LogLevel.Debug, logger, template, serialize(params));
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
        logger = build(logger);
        if (!logger.isInfoEnabled()) {
            return;
        }
        log(LogLevel.Info, logger, template, serialize(params));
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
        logger = build(logger);
        if (!logger.isWarnEnabled()) {
            return;
        }
        log(LogLevel.Warn, logger, template, serialize(params));
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
        logger = build(logger);
        if (!logger.isErrorEnabled()) {
            return;
        }
        log(LogLevel.Error, logger, template, serialize(params));
    }

    /**
     * choose log method by level
     */
    private static void log(LogLevel level, Logger logger, String template, Object... params) {
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
}
