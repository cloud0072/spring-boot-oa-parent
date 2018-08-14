package com.caolei.common.constant;

import static com.caolei.common.constant.Constants.*;

public enum Operation {
    FIND("查询"),
    CREATE("创建"),
    DELETE("删除"),
    UPDATE("修改"),
    ALL("全部"),;

    private String text;

    Operation(String text) {
        this.text = text;
    }

    public static Operation of(String method) {
        switch (method) {
            case OP_FIND:
            case OP_LIST:
            case OP_EXPORT:
            case OP_DOWNLOAD:
                return FIND;
            case OP_CREATE:
            case OP_IMPORT:
                return CREATE;
            case OP_UPDATE:
                return UPDATE;
            case OP_DELETE:
                return DELETE;
            default:
                throw new UnsupportedOperationException("无法识别的操作类型:" + method);
        }
    }

    public String code() {
        if (this == ALL) {
            return "*";
        }
        return name();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
