package org.pharosnet.vertx.dal.generate.def;

import java.util.ArrayList;
import java.util.List;

public class TemplateData {

    public TemplateData(boolean isView) {
        this.isView = 0;
        if (isView) {
            this.isView = 1;
        }
        this.fields = new ArrayList<>();
        this.hasJsonArray = 0;
        this.hasJsonObject = 0;
        this.hasInstant = 0;
    }

    private String pkg;
    private String abstractRow;

    private String execRow;

    private String className;

    private List<Field> fields;

    private int isView;
    private int hasJsonObject;
    private int hasJsonArray;
    private int hasInstant;

    private String schema;
    private String tableName;

    private String sql_insert;
    private String sql_update;
    private String sql_delete_force;
    private String sql_delete;

    public void init() {
        for (Field field : fields) {
            if (field.getType().equals("Instant")) {
                this.hasInstant = 1;
            } else if (field.getType().equals("JsonObject")) {
                this.hasJsonObject = 1;
            } else if (field.getType().equals("JsonArray")) {
                this.hasJsonArray = 1;
            }
        }
        if (this.isView == 0) {
            this.buildSQL();
        }
    }

    public void buildSQL() {
        StringBuilder ib = new StringBuilder();
        ib.append("INSERT INTO ").append(String.format("\\\"%s\\\".\\\"%s\\\"", this.schema, this.tableName)).append(" ");
        ib.append("(");
        for (int i = 0; i < this.fields.size(); i++) {
            if (i == 0) {
                ib.append(String.format("\\\"%s\\\"", fields.get(i).getCol()));
                continue;
            }
            ib.append(" ,").append(String.format("\\\"%s\\\"", fields.get(i).getCol()));
        }
        ib.append(") VALUES (");
        for (int i = 0; i < this.fields.size(); i++) {
            if (i == 0) {
                ib.append("?");
                continue;
            }
            ib.append(" ,?");
        }
        ib.append(")");
        this.sql_insert = ib.toString();

        StringBuilder ub = new StringBuilder();
        ub.append("UPDATE ").append(String.format("\\\"%s\\\".\\\"%s\\\"", this.schema, this.tableName)).append(" SET ");
        ub.append("\\\"MODIFY_BY\\\"=?, \\\"MODIFY_AT\\\"=?, \\\"VERSION\\\"=\\\"VERSION\\\"+1");
        for (int i = 0; i < this.fields.size(); i++) {
            ub.append(" ,").append(String.format("\\\"%s\\\"=?", fields.get(i).getCol()));
        }
        ub.append(" ").append("WHERE \\\"ID\\\"=? AND \\\"VERSION\\\"=?");
        this.sql_update = ub.toString();

        this.sql_delete = String.format("UPDATE \\\"%s\\\".\\\"%s\\\" SET \\\"DELETE_BY\\\"=?, \\\"DELETE_AT\\\"=?, \\\"VERSION\\\"=\\\"VERSION\\\"+1 WHERE \\\"ID\\\"=? AND \\\"VERSION\\\"=?", this.schema, this.tableName);
        this.sql_delete_force = String.format("DELETE FROM \\\"%s\\\".\\\"%s\\\" WHERE \\\"ID\\\"=? AND \\\"VERSION\\\"=?", this.schema, this.tableName);
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getAbstractRow() {
        return abstractRow;
    }

    public void setAbstractRow(String abstractRow) {
        this.abstractRow = abstractRow;
    }

    public String getExecRow() {
        return execRow;
    }

    public void setExecRow(String execRow) {
        this.execRow = execRow;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public int getIsView() {
        return isView;
    }

    public void setIsView(int isView) {
        this.isView = isView;
    }

    public int getHasJsonObject() {
        return hasJsonObject;
    }

    public void setHasJsonObject(int hasJsonObject) {
        this.hasJsonObject = hasJsonObject;
    }

    public int getHasJsonArray() {
        return hasJsonArray;
    }

    public void setHasJsonArray(int hasJsonArray) {
        this.hasJsonArray = hasJsonArray;
    }

    public int getHasInstant() {
        return hasInstant;
    }

    public void setHasInstant(int hasInstant) {
        this.hasInstant = hasInstant;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql_insert() {
        return sql_insert;
    }

    public void setSql_insert(String sql_insert) {
        this.sql_insert = sql_insert;
    }

    public String getSql_update() {
        return sql_update;
    }

    public void setSql_update(String sql_update) {
        this.sql_update = sql_update;
    }

    public String getSql_delete_force() {
        return sql_delete_force;
    }

    public void setSql_delete_force(String sql_delete_force) {
        this.sql_delete_force = sql_delete_force;
    }

    public String getSql_delete() {
        return sql_delete;
    }

    public void setSql_delete(String sql_delete) {
        this.sql_delete = sql_delete;
    }

}
