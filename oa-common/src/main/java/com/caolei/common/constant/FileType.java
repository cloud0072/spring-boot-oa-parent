package com.caolei.common.constant;

/**
 * 文件类别
 */
public enum FileType {

    PORTRAIT("头像"),
    TEST("测试");

    /**
     * 说明
     */
    String text;

    FileType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getPath() {
        return name().toLowerCase();
    }

}
