package com.qunar.guohang.log;

import com.google.common.collect.Lists;
import com.qunar.guohang.performance.LogConstants;
import com.qunar.guohang.strategies.ParamStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Log Service
 * ---init with a log
 * ---log by log
 * <p>
 * Holds the log object for a long time
 * Not single
 * <p>
 * May modify params as strategies.
 * Only serialize by logUtil.
 *
 * @author guohang.ding on 16-10-2
 */
@SuppressWarnings("unused")
public class LogService {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogService.class);
    private Logger log;
    private List<ParamStrategy> strategies;

    private LogService() {
        strategies = Lists.newArrayList();
    }

    private LogService(Logger logger) {
        log = logger == null ? NORMAL_LOGGER : logger;
        strategies = LogConstants.buildStrategies();
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

    /**
     * prepare params by strategies
     * happens before serialize
     */
    private Object[] prepareParams(Object... params) {
        for (ParamStrategy strategy : strategies) {
            params = strategy.modify(params);
        }

        return params;
    }

    public final void trace(String template, Object... params) {
        LogUtil.trace(log, template, prepareParams(params));
    }

    public final void debug(String template, Object... params) {
        LogUtil.debug(log, template, prepareParams(params));
    }

    public final void info(String template, Object... params) {
        LogUtil.info(log, template, prepareParams(params));
    }

    public final void warn(String template, Object... params) {
        LogUtil.warn(log, template, prepareParams(params));
    }

    public final void error(String template, Object... params) {
        LogUtil.error(log, template, prepareParams(params));
    }


}
