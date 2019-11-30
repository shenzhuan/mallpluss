package com.zscat.mallplus.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.template.*;
import com.zscat.mallplus.bo.ColumnInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.*;

/**
 * 代码生成
 * @author Zheng Jie
 * @date 2019-01-02
 */
@Slf4j
public class GenUtil {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIGDECIMAL = "BigDecimal";

    private static final String PK = "PRI";

    private static final String EXTRA = "auto_increment";

    /**
     * 获取后端代码模板名称
     * @return List
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");
        templateNames.add("Dto");
        templateNames.add("Mapper");
        templateNames.add("Repository");
        templateNames.add("Service");
        templateNames.add("ServiceImpl");
        templateNames.add("QueryCriteria");
        templateNames.add("Controller");
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     * @return List
     */
    private static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("api");
        templateNames.add("index");
        templateNames.add("eForm");
        return templateNames;
    }

    /**
     * 生成代码
     * @param columnInfos 表元数据

     */
    public static void generatorCode(List<ColumnInfo> columnInfos,  String tableName) throws IOException {
        String packages = "com.zscat.mallplus";
        Map<String,Object> map = new HashMap<>();
        map.put("package",packages);
        map.put("moduleName",tableName.split("_")[0]);
        map.put("author","mallplus多租户商城");
        map.put("date", LocalDate.now().toString());
        map.put("tableName",tableName);
        String className = StringUtils.toCapitalizeCamelCase(tableName);
        String changeClassName = StringUtils.toCamelCase(tableName);


        map.put("className", className);
        map.put("upperCaseClassName", className.toUpperCase());
        map.put("changeClassName", changeClassName);
        map.put("hasTimestamp",false);
        map.put("queryHasTimestamp",false);
        map.put("queryHasBigDecimal",false);
        map.put("hasBigDecimal",false);
        map.put("hasQuery",false);
        map.put("auto",false);
        map.put("tableName", "tableName");
        map.put("comments", "tableName");


        List<Map<String,Object>> columns = new ArrayList<>();
        List<Map<String,Object>> queryColumns = new ArrayList<>();
        for (ColumnInfo column : columnInfos) {
            Map<String,Object> listMap = new HashMap<>();
            listMap.put("columnComment",column.getColumnComment());
            listMap.put("columnKey",column.getColumnKey());

            String colType = ColUtil.cloToJava(column.getColumnType().toString());
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName().toString());
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName().toString());
            if(PK.equals(column.getColumnKey())){
                map.put("pkColumnType",colType);
                map.put("pkChangeColName",changeColumnName);
                map.put("pkCapitalColName",capitalColumnName);
            }
            if(TIMESTAMP.equals(colType)){
                map.put("hasTimestamp",true);
            }
            if(BIGDECIMAL.equals(colType)){
                map.put("hasBigDecimal",true);
            }
            if(EXTRA.equals(column.getExtra())){
                map.put("auto",true);
            }
            listMap.put("columnType",colType);
            listMap.put("columnName",column.getColumnName());
            listMap.put("isNullable",column.getIsNullable());
            listMap.put("columnShow",column.getColumnShow());
            listMap.put("changeColumnName",changeColumnName);
            listMap.put("capitalColumnName",capitalColumnName);

            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if(!StringUtils.isBlank(column.getColumnQuery())){
                listMap.put("columnQuery",column.getColumnQuery());
                map.put("hasQuery",true);
                if(TIMESTAMP.equals(colType)){
                    map.put("queryHasTimestamp",true);
                }
                if(BIGDECIMAL.equals(colType)){
                    map.put("queryHasBigDecimal",true);
                }
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns",columns);
        map.put("queryColumns",queryColumns);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));

        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/"+templateName+".ftl");
            String filePath = getAdminFilePath(templateName,tableName.split("_")[0],className);

            assert filePath != null;
            File file = new File(filePath);


            // 生成代码
            genFile(file, template, map);
        }

        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/"+templateName+".ftl");
            String filePath = getFrontFilePath(templateName,map.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);


            // 生成代码
            genFile(file, template, map);
        }
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String template, String Module, String className) {
        String packages = "com.zscat.mallplus";
        String projectPath = System.getProperty("user.dir") ;
        String packagePath = projectPath + File.separator + "src" +File.separator+ "main" + File.separator + "java" + File.separator;
        if (!ObjectUtils.isEmpty(packages)) {
            packagePath += packages.replace(".", File.separator) + File.separator;
        }


        if (template.contains("domain.java.ftl")) {
            return Module + className + ".java";
        }

        if (template.contains("Dao.java.ftl")) {
            return Module + className + "Mapper.java";
        }

//		if(template.contains("Mapper.java.vm")){
//			return packagePath + "dao" + File.separator + className + "Mapper.java";
//		}
        // templates.add("common/generator/menu.sql.ftl");
        if (template.contains("menu.sql.ftl")) {
            return Module + className + "menu.sql";
        }
        if (template.contains("Service.java.ftl")) {
            return "I" + Module + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.ftl")) {
            return Module + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.ftl")) {
            return Module + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return Module + className + "Mapper.xml";
        }

        return null;
    }

    /**
     * 定义前端文件路径以及名称
     */
    private static String getFrontFilePath(String template,  String classname) {

        if (template.contains("api.js.ftl")) {
            return classname + ".js";
        }
        if (template.contains("path.js.ftl")) {
            return classname + "path" + ".js";
        }
        if (template.contains("add.vue.ftl")) {
            return classname + File.separator + "add.vue";
        }

        if (template.contains("index.vue.ftl")) {
            return classname + File.separator + "index.vue";
        }
        if (template.contains("update.vue.ftl")) {
            return classname + File.separator + "update.vue";
        }
        if (template.contains("BrandDetail.vue.ftl")) {
            return classname + File.separator + "components" + File.separator + classname + "Detail.vue";
        }
        return null;
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
}
