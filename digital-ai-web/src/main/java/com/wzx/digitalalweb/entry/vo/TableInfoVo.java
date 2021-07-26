package com.wzx.digitalalweb.entry.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableInfoVo implements Serializable {
    //表名
    private String tableName;

    //列表属性名字
    private String columnName;

    //列表的属性是否空！！
    private Boolean isNullable;

    //列表属性类型
    private String dataType;

    //列表属性注释
    private String columnComment;

    //列表属性是否为主键
    private String columnKey;

    //是否为自增
    private String extra;

    public TableInfoVo(String tableName, String columnName, Boolean isNullable, String dataType, String columnComment, String columnKey, String extra) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.isNullable = isNullable;
        this.dataType = dataType;
        this.columnComment = columnComment;
        this.columnKey = columnKey;
        this.extra = extra;
    }
}
