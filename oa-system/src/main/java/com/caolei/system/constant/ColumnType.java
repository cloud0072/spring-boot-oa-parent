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

    VARCHAR("String"),
    INTEGER("Integer"),
    DECIMAL("BigDecimal"),
    DATETIME("Date"),
    FILE("File");

    private Class clazz;

    ColumnType(String className) {
        switch (className) {
            case "String":
                this.clazz = String.class;
                break;
            case "Integer":
                this.clazz = Integer.class;
                break;
            case "BigDecimal":
                this.clazz = BigDecimal.class;
                break;
            case "Date":
                this.clazz = Date.class;
                break;
            case "File":
                this.clazz = File.class;
                break;
            default:
                this.clazz = String.class;
        }
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
