package com.caolei.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.caolei.common.util.HttpUtils.sessionId;

public interface BaseLogger {

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void error(String errorMessage) {
        logger().error("{}\tSessionId : {}", errorMessage, sessionId());
    }

    default void info(String message) {
        logger().info("{}\tSessionId : {}", message, sessionId());
    }
}
