package com.wzx.digitalalweb.log;

/**
 * @author: wzx
 * @date: 2021-07-8
 * @apiNote: 日志类型
 */

public enum LogActionType {
    /**
     * 增删改查
     */
    ADD("新增"),
    SELECT("查询"),
    UPDATE("更新"),
    DELETE("删除");
    private String value;

    LogActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
