package org.pharosnet.vertx.dal.generate.def;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class Query {

    public Query() {
        this.args = new ArrayList<>();
    }

    @JSONField(name = "columns")
    private String name;
    @JSONField(name = "columns")
    private String kind;
    @JSONField(name = "columns")
    private List<String> args;
    @JSONField(name = "columns")
    private String sql;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
