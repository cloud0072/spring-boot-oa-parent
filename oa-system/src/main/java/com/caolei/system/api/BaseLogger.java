package com.caolei.system.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BaseLogger {

    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

}
