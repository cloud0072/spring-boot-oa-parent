package com.caolei.system.api;

import com.caolei.system.util.BaseLogger;
import org.slf4j.Logger;

/**
 * @ClassName: LoggerEntity
 * @Description: 实现了Logger的实体，一般给需要静态打印
 * @author caolei
 * @date 2018/8/2 19:43
 */
public class LoggerEntity implements BaseLogger {

    protected static Logger logger;

    protected LoggerEntity() {
        logger = logger();
    }

    public Logger logger() {
        return logger;
    }

}
