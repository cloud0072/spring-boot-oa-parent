package com.caolei.system.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ColumnConfigModel
 * @Description: 列设置模型  用于获取多列ColumnEntity时的模型类
 * @author caolei
 * @date 2018/8/1 20:29
 */
public class ColumnConfigModel {
    private List<ColumnEntity> columnEntity = new ArrayList<>();

    public ColumnConfigModel() {
    }

    public List<ColumnEntity> getColumnEntity() {
        return columnEntity;
    }

    public void setColumnEntity(List<ColumnEntity> columnEntity) {
        this.columnEntity = columnEntity;
    }
}