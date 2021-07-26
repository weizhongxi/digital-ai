package com.wzx.digitalalweb.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.wzx.digitalalweb.entry.vo.TableInfoVo;
import com.wzx.digitalalweb.util.GenUtil;
import com.wzx.digitalalweb.util.PageUtil;
import com.wzx.digitalalweb.entry.GenConfig;
import com.wzx.digitalalweb.entry.TableInfo;
import com.wzx.digitalalweb.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratorServiceImpl implements GeneratorService {
    @PersistenceContext
    private EntityManager em;
    @Override
    public Object getTables() {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "order by create_time desc";
        Query query = em.createNativeQuery(sql);
        return query.getResultList();
    }

    @Override
    public Object getTables(String name, int[] startEnd) {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "and table_name like ? order by create_time desc";
        Query query = em.createNativeQuery(sql);
        query.setFirstResult(startEnd[0]);
        query.setMaxResults(startEnd[1] - startEnd[0]);
        query.setParameter(1, StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        List result = query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            tableInfos.add(new TableInfo(arr[0], arr[1], arr[2], arr[3], ObjectUtil.isNotEmpty(arr[4]) ? arr[4] : "-"));
        }
        Query query1 = em.createNativeQuery("SELECT COUNT(*) from information_schema.tables where table_schema = (select database())");
        Object totalElements = query1.getSingleResult();
        return PageUtil.toPage(tableInfos, totalElements);
    }

    @Override
    public List<TableInfoVo> query(String table) {
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                "where table_name = ? and table_schema = (select database()) order by ordinal_position";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, table);
        List result = query.getResultList();
        List<TableInfoVo> tableInfoVos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            tableInfoVos.add(
                    new TableInfoVo(
                            table,
                            arr[0].toString(),
                            "NO".equals(arr[1]),
                            arr[2].toString(),
                            ObjectUtil.isNotNull(arr[3]) ? arr[3].toString() : null,
                            ObjectUtil.isNotNull(arr[4]) ? arr[4].toString() : null,
                            ObjectUtil.isNotNull(arr[5]) ? arr[5].toString() : null)
            );
        }
        return tableInfoVos;
    }

    @Override
    public Boolean genInfo(String tableName) {
        Boolean result = true;
        List<TableInfoVo> tableInfoVos = query(tableName);
        if (StringUtils.isNotBlank(tableName) && tableInfoVos != null){
            GenConfig genConfig = new GenConfig();
            genConfig.setTableName(tableName);
            genConfig.setApiAlias(tableName);
            genConfig.setModuleName("aa");
            genConfig.setPack("com.wzx.generatorspace.user");
            genConfig.setAuthor("wzx");
            Boolean code = GenUtil.generatedCode(tableInfoVos, genConfig);
            if (code){
                log.info("生成代码成功！！");
            }else {
                log.info("生成代码失败！！");
                result = false;
            }
        }
        return result;
    }
}
