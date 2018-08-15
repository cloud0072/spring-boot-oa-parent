package com.caolei.common.constant;


/**
 * @author caolei
 * @ClassName: ColumnType
 * @Description: TODO
 * @date 2018/7/13 19:41
 */
public enum ColumnType {

    VARCHAR("文本"),
    INTEGER("整数"),
    DECIMAL("小数"),
    DATETIME("日期"),;

    private String text;

    ColumnType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
