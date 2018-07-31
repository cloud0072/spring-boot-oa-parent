package com.caolei.system.api;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BaseLogger {

    static Logger logger(String className) {
        return LoggerFactory.getLogger(className);
    }

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void error(String errorMessage) {
        String sessionId = (String) SecurityUtils.getSubject().getSession().getId();
        logger().error("SessionId : " + sessionId + "\tERROR : " + errorMessage);
    }

    default void info(String message) {
        String sessionId = (String) SecurityUtils.getSubject().getSession().getId();
        logger().info("SessionId : " + sessionId + "\tINFO : " + message);
    }

}
