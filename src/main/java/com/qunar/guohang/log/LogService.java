package com.qunar.guohang.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log Service
 * ---init with a log
 * ---log by log
 * <p>
 * Holds the log object for a long time
 * Not single
 *
 * @author guohang.ding on 16-10-2
 */
@SuppressWarnings("unused")
public class LogService {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogService.class);
    private Logger log;

    private LogService() {
    }

    private LogService(Logger logger) {
        log = logger == null ? NORMAL_LOGGER : logger;
    }

    /**
     * get instance by log
     *
     * @return logService with log or NORMAL_LOGGER
     */
    public static LogService create(Logger logger) {
        NORMAL_LOGGER.info("init logService by logger {}", logger);
        return new LogService(logger);
    }

    /**
     * get instance by class
     *
     * @return logService with log or NORMAL_LOGGER
     */
    public static LogService create(Class clazz) {
        NORMAL_LOGGER.info("init logService by class {}", clazz);
        return new LogService(LoggerFactory.getLogger(clazz));
    }

    public final void trace(String template, Object... params) {
        LogUtil.trace(log, template, params);
    }

    public final void debug(String template, Object... params) {
        LogUtil.debug(log, template, params);
    }

    public final void info(String template, Object... params) {
        LogUtil.info(log, template, params);
    }

    public final void warn(String template, Object... params) {
        LogUtil.warn(log, template, params);
    }

    public final void error(String template, Object... params) {
        LogUtil.error(log, template, params);
    }


}
