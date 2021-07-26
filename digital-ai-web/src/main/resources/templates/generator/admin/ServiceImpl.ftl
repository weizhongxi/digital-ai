package ${package}.service.impl;

import ${package}.entry.${className};
import lombok.RequiredArgsConstructor;
import ${package}.repository.${className}Repository;
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.service.mapstruct.${className}Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
<#if !auto && pkColumnType = 'Long'>
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
</#if>
<#if !auto && pkColumnType = 'String'>
import cn.hutool.core.util.IdUtil;
</#if>
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.wzx.digitalalweb.util.PageUtil;
import com.wzx.digitalalweb.util.FileUtil;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @description 服务实现
* @author ${author}
* @date ${date}
**/
@Service
@RequiredArgsConstructor
public class ${className}ServiceImpl implements ${className}Service {

    private final ${className}Repository ${changeClassName}Repository;
    private final ${className}Mapper ${changeClassName}Mapper;

    @Override
    public Map<String,Object> queryAll(${className}QueryCriteria criteria, Pageable pageable){
        Page<${className}> page = ${changeClassName}Repository.findAll(pageable);
        return PageUtil.toPage(page.map(${changeClassName}Mapper::toDto));
    }

    @Override
    public List<${className}Dto> queryAll(${className}QueryCriteria criteria){
        return ${changeClassName}Mapper.toDto(${changeClassName}Repository.findAll());
    }

    @Override
    @Transactional
    public ${className}Dto findById(${pkColumnType} ${pkChangeColName}) {
        ${className} ${changeClassName} = ${changeClassName}Repository.findById(${pkChangeColName}).orElseGet(${className}::new);
        return ${changeClassName}Mapper.toDto(${changeClassName});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${className}Dto create(${className} resources) {
        return ${changeClassName}Mapper.toDto(${changeClassName}Repository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(${className} resources) {
        ${className} ${changeClassName} = ${changeClassName}Repository.findById(resources.get${pkCapitalColName}()).orElseGet(${className}::new);
        ${changeClassName}.copy(resources);
        ${changeClassName}Repository.save(${changeClassName});
    }

    @Override
    public void deleteAll(${pkColumnType}[] ids) {
        for (${pkColumnType} ${pkChangeColName} : ids) {
            ${changeClassName}Repository.deleteById(${pkChangeColName});
        }
    }

    @Override
    public void download(List<${className}Dto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (${className}Dto ${changeClassName} : all) {
            Map<String,Object> map = new LinkedHashMap<>();
        <#list columns as column>
            <#if column.columnKey != 'PRI'>
            <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
            <#else>
            map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
            </#if>
            </#if>
        </#list>
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
