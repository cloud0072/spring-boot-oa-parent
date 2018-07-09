package com.caolei.system.constant;

import static com.caolei.system.constant.Constants.*;

public enum Operation {
    //查询
    FIND,
    //创建
    CREATE,
    //删除
    DELETE,
    //修改
    UPDATE,
    //全部
    ALL;

    public String code() {
        if (this == ALL) {
            return "*";
        }
        return name();
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
}
