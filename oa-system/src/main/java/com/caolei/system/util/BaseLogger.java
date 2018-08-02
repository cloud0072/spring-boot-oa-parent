package com.caolei.system.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BaseLogger {

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void error(String errorMessage) {
        logger().error("SessionId : " + RequestUtils.getSessionId() + "\tERROR : " + errorMessage);
    }

    default void info(String message) {
        logger().info("SessionId : " + RequestUtils.getSessionId() + "\tINFO : " + message);
    }

}
