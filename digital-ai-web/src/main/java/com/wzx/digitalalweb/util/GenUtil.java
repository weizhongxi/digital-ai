package com.wzx.digitalalweb.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.*;
import com.wzx.digitalalweb.entry.vo.TableInfoVo;
import com.wzx.digitalalweb.entry.GenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GenUtil {
    private static final String TIMESTAMP = "Timestamp";

    private static final String BIGDECIMAL = "BigDecimal";

    public static final String PK = "PRI";

    public static final String EXTRA = "auto_increment";

    public static  Boolean generatedCode(List<TableInfoVo> columns, GenConfig genConfig){
        Boolean redult = true;
        Map<String, Object> genMap = getGenMap(columns, genConfig);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("/templates/", TemplateConfig.ResourceMode.CLASSPATH));
        List<String> adminTemplateNames = getAdminTemplateNames();
        try {
            adminTemplateNames.stream().forEach(templateName->{
                Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");

                String filePath = getAdminFilePath(templateName,genConfig, genMap.get("className").toString(), System.getProperty("user.dir"));
                System.out.println("filePath = " + filePath);
                File file = new File(filePath);

                // 如果非覆盖生成
                if (!FileUtil.exist(file)) {
                    try {
                        genFile(file, template, genMap);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            redult= false;
            throw new RuntimeException("生成代码失败！！");
        }

        return redult;
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className, String rootPath) {
        String projectPath = rootPath ;
        String packagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (!ObjectUtils.isEmpty(genConfig.getPack())) {
            packagePath += genConfig.getPack().replace(".", File.separator) + File.separator;
        }

        if ("Entity".equals(templateName)) {
            return packagePath + "entry" + File.separator + className + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("Dto".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "Dto.java";
        }

        if ("QueryCriteria".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "QueryCriteria.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "service" + File.separator + "mapstruct" + File.separator + className + "Mapper.java";
        }

        if ("Repository".equals(templateName)) {
            return packagePath + "repository" + File.separator + className + "Repository.java";
        }

        return null;
    }
    /**
     * 获取后端代码模板名称
     *
     * @return List
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");
        templateNames.add("Dto");
        templateNames.add("Mapper");
        templateNames.add("Controller");
        templateNames.add("QueryCriteria");
        templateNames.add("Service");
        templateNames.add("ServiceImpl");
        templateNames.add("Repository");
        return templateNames;
    }

    // 获取模版数据
    private static Map<String, Object> getGenMap(List<TableInfoVo> tableInfoVos, GenConfig genConfig) {
        // 存储模版字段数据
        Map<String, Object> genMap = new HashMap<>(16);
        // 接口别名
        genMap.put("apiAlias", genConfig.getApiAlias());
        // 包名称
        genMap.put("package", genConfig.getPack());
        // 模块名称
        genMap.put("moduleName", genConfig.getModuleName());
        // 作者
        genMap.put("author", genConfig.getAuthor());
        // 创建日期
        genMap.put("date", LocalDate.now().toString());
        // 表名
        genMap.put("tableName", genConfig.getTableName());
        // 大写开头的类名
        String className = StringUtils.toCapitalizeCamelCase(genConfig.getTableName());
        // 小写开头的类名
        String changeClassName = StringUtils.toCamelCase(genConfig.getTableName());
        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(genConfig.getTableName(), genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(genConfig.getTableName(), genConfig.getPrefix()));
        }
        // 保存类名
        genMap.put("className", className);
        // 保存小写开头的类名
        genMap.put("changeClassName", changeClassName);
        // 存在 Timestamp 字段
        genMap.put("hasTimestamp", false);
        // 查询类中存在 Timestamp 字段
        genMap.put("queryHasTimestamp", false);
        // 存在 BigDecimal 字段
        genMap.put("hasBigDecimal", false);
        // 查询类中存在 BigDecimal 字段
        genMap.put("queryHasBigDecimal", false);
        // 是否需要创建查询
        genMap.put("hasQuery", false);
        // 自增主键
        genMap.put("auto", false);
        // 存在字典
        genMap.put("hasDict", false);
        // 存在日期注解
        genMap.put("hasDateAnnotation", false);
        // 保存字段信息
        List<Map<String, Object>> columns = new ArrayList<>();
        // 保存查询字段的信息
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        // 存储字典信息
        List<String> dicts = new ArrayList<>();
        // 存储 between 信息
        List<Map<String, Object>> betweens = new ArrayList<>();
        // 存储不为空的字段信息
        List<Map<String, Object>> isNotNullColumns = new ArrayList<>();

        for (TableInfoVo column : tableInfoVos) {
            Map<String, Object> listMap = new HashMap<>(16);
            // 字段描述
            listMap.put("remark", column.getColumnComment());
            // 字段类型
            listMap.put("columnKey", column.getColumnKey());
            // 主键类型
            String colType = ColUtil.cloToJava(column.getDataType());
            // 小写开头的字段名
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName());
            // 大写开头的字段名
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName());
            if (PK.equals(column.getColumnKey())) {
                // 存储主键类型
                genMap.put("pkColumnType", colType);
                // 存储小写开头的字段名
                genMap.put("pkChangeColName", changeColumnName);
                // 存储大写开头的字段名
                genMap.put("pkCapitalColName", capitalColumnName);
            }
            // 是否存在 Timestamp 类型的字段
            if (TIMESTAMP.equals(colType)) {
                genMap.put("hasTimestamp", true);
            }
            // 是否存在 BigDecimal 类型的字段
            if (BIGDECIMAL.equals(colType)) {
                genMap.put("hasBigDecimal", true);
            }
            // 主键是否自增
            if (EXTRA.equals(column.getExtra())) {
                genMap.put("auto", true);
            }
            // 主键存在字典
//            if (StringUtils.isNotBlank(column.getDictName())) {
//                genMap.put("hasDict", true);
//                dicts.add(column.getDictName());
//            }

            // 存储字段类型
            listMap.put("columnType", colType);
            // 存储字原始段名称
            listMap.put("columnName", column.getColumnName());
            // 不为空
            listMap.put("istNotNull", column.getIsNullable());
            // 字段列表显示
//            listMap.put("columnShow", column.getListShow());
            // 表单显示
//            listMap.put("formShow", column.getFormShow());
            // 表单组件类型
//            listMap.put("formType", StringUtils.isNotBlank(column.getFormType()) ? column.getFormType() : "Input");
            // 小写开头的字段名称
            listMap.put("changeColumnName", changeColumnName);
            //大写开头的字段名称
            listMap.put("capitalColumnName", capitalColumnName);
            // 字典名称
//            listMap.put("dictName", column.getDictName());
            // 日期注解
//            listMap.put("dateAnnotation", column.getDateAnnotation());
//            if (StringUtils.isNotBlank(column.getDateAnnotation())) {
//                genMap.put("hasDateAnnotation", true);
//            }
            // 添加非空字段信息
            if (column.getIsNullable()) {
                isNotNullColumns.add(listMap);
            }
            // 判断是否有查询，如有则把查询的字段set进columnQuery
//            if (!StringUtils.isBlank(column.getQueryType())) {
//                // 查询类型
//                listMap.put("queryType", column.getQueryType());
//                // 是否存在查询
//                genMap.put("hasQuery", true);
//                if (TIMESTAMP.equals(colType)) {
//                    // 查询中存储 Timestamp 类型
//                    genMap.put("queryHasTimestamp", true);
//                }
//                if (BIGDECIMAL.equals(colType)) {
//                    // 查询中存储 BigDecimal 类型
//                    genMap.put("queryHasBigDecimal", true);
//                }
//                if ("between".equalsIgnoreCase(column.getQueryType())) {
//                    betweens.add(listMap);
//                } else {
//                    // 添加到查询列表中
//                    queryColumns.add(listMap);
//                }
//            }
            // 添加到字段列表中
            columns.add(listMap);
        }
        // 保存字段列表
        genMap.put("columns", columns);
        // 保存查询列表
        genMap.put("queryColumns", queryColumns);
        // 保存字段列表
        genMap.put("dicts", dicts);
        // 保存查询列表
        genMap.put("betweens", betweens);
        // 保存非空字段信息
        genMap.put("isNotNullColumns", isNotNullColumns);
        return genMap;
    }

    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }

    public static void main(String[] args) {
        List<TableInfoVo> tableInfoVos = new ArrayList<>();
        GenConfig genConfig = new GenConfig();
        generatedCode(tableInfoVos,genConfig);
    }
}
