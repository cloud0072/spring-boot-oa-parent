package com.caolei.system.web;

import com.caolei.system.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BaseLogger {

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void error(String errorMessage) {
        logger().error("{}\tSessionId : {}", errorMessage, RequestUtils.getSessionId());
    }

    default void info(String message) {
        logger().info("{}\tSessionId : {}", message, RequestUtils.getSessionId());
    }

}
