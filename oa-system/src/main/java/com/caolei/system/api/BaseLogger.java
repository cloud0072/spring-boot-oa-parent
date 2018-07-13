package com.caolei.system.api;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BaseLogger {

    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void error(String errorMessage) {
        String sessionId = (String) SecurityUtils.getSubject().getSession().getId();
        getLogger().error("SessionId : " + sessionId + "\tError : " + errorMessage);
    }

}
