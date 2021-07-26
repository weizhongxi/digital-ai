package com.wzx.digitalalweb.service;

import com.wzx.digitalalweb.entry.vo.TableInfoVo;

import java.util.List;

public interface GeneratorService {
    /**
     * 获取所有table
     * @return /
     */
    Object getTables();

    /**
     * 查询数据库元数据
     * @param name 表名
     * @param startEnd 分页参数
     * @return /
     */
    Object getTables(String name, int[] startEnd);

    /**
     * 查询数据库的表字段数据数据
     * @param table /
     * @return /
     */
    List<TableInfoVo> query(String table);


    /**
     * 查询数据库的表字段数据数据
     * @param tableName /
     * @return /
     */
    Boolean genInfo(String tableName);
}
