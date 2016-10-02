package com.qunar.guohang.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log Service
 * ---init with a logger
 * ---log by logger
 * <p>
 * Holds the logger object for a long time
 *
 * @author guohang.ding on 16-10-2
 */
public class LogService {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogService.class);
    private Logger logger;

    private LogService() {
    }

    private LogService(Logger logger) {
        this.logger = logger == null ? NORMAL_LOGGER : logger;
    }

    /**
     * get instance by logger
     * @return logService with logger or NORMAL_LOGGER
     */
    public static LogService getInstance(Logger logger) {
        NORMAL_LOGGER.info("init logService {}", logger);
        return new LogService(logger);
    }
}
