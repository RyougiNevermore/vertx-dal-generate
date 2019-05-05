package org.pharosnet.vertx.dal.generate.def;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Table {

    public Table() {
    }

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    @JSONField(name = "schema")
    private String schema;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "columns")
    private List<Column> columns;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

}
