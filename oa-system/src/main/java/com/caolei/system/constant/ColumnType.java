package com.caolei.system.constant;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author caolei
 * @ClassName: ColumnType
 * @Description: TODO
 * @date 2018/7/13 19:41
 */
public enum ColumnType {

    VARCHAR(String.class),
    INTEGER(Integer.class),
    DECIMAL(BigDecimal.class),
    DATETIME(Date.class),
    FILE(File.class);

    private Class clazz;

    ColumnType(Class<?> stringClass) {
        clazz = stringClass;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
