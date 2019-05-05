package org.pharosnet.vertx.dal.generate.def;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class DataAccessLayer {

    public DataAccessLayer() {
        this.tables = new ArrayList<>();
        this.views = new ArrayList<>();
    }



    @JSONField(name = "tables")
    private List<Table> tables;
    @JSONField(name = "views")
    private List<Table> views;


    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<Table> getViews() {
        return views;
    }

    public void setViews(List<Table> views) {
        this.views = views;
    }

}
