package com.qunar.flight.log;

import com.google.common.collect.Lists;
import com.qunar.flight.log.strategies.ParamStrategy;
import com.qunar.flight.log.strategies.StrategiesLoader;
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
public final class LogService {

    private static final Logger NORMAL_LOGGER = LoggerFactory.getLogger(LogService.class);
    private Logger log;
    private List<ParamStrategy> strategies = null;

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
        NORMAL_LOGGER.debug("init logService by logger {}", logger);
        return new LogService(logger);
    }

    /**
     * get instance by class
     *
     * @return logService with log or NORMAL_LOGGER
     */
    public static LogService create(Class clazz) {
        NORMAL_LOGGER.debug("init logService by class {}", clazz);
        return new LogService(LoggerFactory.getLogger(clazz));
    }

    /**
     * get instance by name
     *
     * @return logService with log or NORMAL_LOGGER
     */
    public static LogService create(String name) {
        NORMAL_LOGGER.debug("init logService by name {}", name);
        return new LogService(LoggerFactory.getLogger(name));
    }

    /**
     * prepare params by strategies
     * happens before serialize
     */
    private Object[] prepareParams(Object... params) {
        try {
            Object[] ret = null;
            if (params != null) {
                ret = new Object[params.length];
                System.arraycopy(params, 0, ret, 0, params.length);
            }
            if (strategies == null) {
                synchronized (this) {
                    if (strategies == null) {
                        strategies = Lists.newArrayList(StrategiesLoader.getStrategies());
                    }
                }
            }

            for (ParamStrategy strategy : strategies) {
                ret = strategy.modify(ret);
            }
            return ret;
        } catch (Throwable throwable) {
            // 如果出现问题，保证不影响原始状态使用
            NORMAL_LOGGER.error("Something Wrong With LogService.prepareParams......params={}", params, throwable);
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
