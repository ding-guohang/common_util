package com.qunar.guohang.log;

import com.qunar.guohang.util.SerializeStrategy;
import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final SerializeStrategy serialize = new SerializeStrategy();

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
    public static void trace(@Nullable Logger logger, String template, Object... params) {
        logger = build(logger);
        if (!logger.isTraceEnabled()) {
            return;
        }
        log(LogLevel.Trace, logger, template, params);
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
    public static void debug(@Nullable Logger logger, String template, Object... params) {
        logger = build(logger);
        if (!logger.isDebugEnabled()) {
            return;
        }
        log(LogLevel.Debug, logger, template, params);
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
    public static void info(@Nullable Logger logger, String template, Object... params) {
        logger = build(logger);
        if (!logger.isInfoEnabled()) {
            return;
        }
        log(LogLevel.Info, logger, template, params);
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
    public static void warn(@Nullable Logger logger, String template, Object... params) {
        logger = build(logger);
        if (!logger.isWarnEnabled()) {
            return;
        }
        log(LogLevel.Warn, logger, template, params);
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
    public static void error(@Nullable Logger logger, String template, Object... params) {
        logger = build(logger);
        if (!logger.isErrorEnabled()) {
            return;
        }
        log(LogLevel.Error, logger, template, params);
    }

    /**
     * choose log method by level
     */
    private static void log(LogLevel level, Logger logger, String template, Object... params) {
        switch (level) {
            case Trace:
                logger.trace(template, serialize.modify(params));
                break;
            case Debug:
                logger.debug(template, serialize.modify(params));
                break;
            case Info:
                logger.info(template, serialize.modify(params));
                break;
            case Warn:
                logger.warn(template, serialize.modify(params));
                break;
            case Error:
                logger.error(template, serialize.modify(params));
                break;
            default:
                logger.info(template, serialize.modify(params));
                break;
        }
    }
}