package com.caolei.common.constant;

public enum Operation {
    GET("查询"),
    POST("创建"),
    DELETE("删除"),
    PUT("修改"),
    ALL("全部");

    private String text;

    Operation(String text) {
        this.text = text;
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
