package com.qunar.guohang.log;

import com.qunar.guohang.util.SerializeStrategy;
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
    public static void trace(Logger logger, String template, Object... params) {
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
    public static void debug(Logger logger, String template, Object... params) {
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
    public static void info(Logger logger, String template, Object... params) {
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
    public static void warn(Logger logger, String template, Object... params) {
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
    public static void error(Logger logger, String template, Object... params) {
        logger = build(logger);
        if (!logger.isErrorEnabled()) {
            return;
        }
        log(LogLevel.Error, logger, template, params);
    }

    private static void log(LogLevel level, Logger logger, String template, Object... params) {
        try {
            logSwitch(level, logger, template, serialize.modify(params));
        } catch (Throwable throwable) {
            // 如果出现问题，保证不影响原始状态使用
            NORMAL_LOGGER.error("Something Wrong With LogUtil.log...", throwable);
            logSwitch(level, logger, template, params);
        }
    }

    /**
     * choose log method by level
     */
    private static void logSwitch(LogLevel level, Logger logger, String template, Object params) {
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
