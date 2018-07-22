package com.caolei.system.api;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BaseLogger {

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void error(String errorMessage) {
        String sessionId = (String) SecurityUtils.getSubject().getSession().getId();
        logger().error("Class : " + getClass().getName() + "\nSessionId : " + sessionId + "\nERROR : " + errorMessage);
    }

    default void info(String message) {
        String sessionId = (String) SecurityUtils.getSubject().getSession().getId();
        logger().info("Class : " + getClass().getName() + "\nSessionId : " + sessionId + "\nINFO : " + message);
    }

}
