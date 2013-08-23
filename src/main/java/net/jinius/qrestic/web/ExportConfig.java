package net.jinius.qrestic.web;

import java.util.LinkedHashMap;

/**
 * Struct object that contains configuration for POI Excel Sheet generation.
 */
public class ExportConfig {
    public String name;
    public String sql;
    public LinkedHashMap<String,String> columnHeaderMap = new LinkedHashMap<String, String>();
    public LinkedHashMap<String,Integer> columnIndexMap = new LinkedHashMap<String,Integer>();

    public ExportConfig() {
    }

    public ExportConfig(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public ExportConfig(String name, String sql, LinkedHashMap<String, String> columnHeaderMap) {
        this.name = name;
        this.sql = sql;
        this.columnHeaderMap = columnHeaderMap;
    }

    public ExportConfig addColumnHeading(String column,String heading){
        columnHeaderMap.put(column,heading);
        return this;
    }

    public LinkedHashMap<String, String> getColumnHeaderMap() {
        return columnHeaderMap;
    }

    public LinkedHashMap<String, Integer> getColumnIndexMap() {
        return columnIndexMap;
    }

    public String getName() {
        return name;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return String.format("ExportConfig: %s\n%s",name,sql);
    }
}
