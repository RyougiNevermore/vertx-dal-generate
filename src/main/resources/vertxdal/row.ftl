package ${pkg};

import ${abstractRow};
<#if isView == 0>
import ${execRow};
</#if>
import io.vertx.core.json.JsonObject;
<#if hasJsonArray == 1>
import io.vertx.core.json.JsonArray;
</#if>

<#if hasInstant == 1>
import java.time.Instant;
</#if>
import java.util.ArrayList;
import java.util.List;

public class ${className} extends AbstractRow {

    public static List<${className}> map(List<JsonObject> array) {
        List<${className}> rows = new ArrayList<>(array.size());
        int s = array.size();
        for (int i = 0; i < s; i++) {
            ${className} row = new ${className}(array.get(i));
            rows.add(row);
        }
        return rows;
    }

    public static ${className} map(JsonObject object) {
        return new ${className}(object);
    }

    public ${className}() {
    }

    public ${className}(JsonObject row) {
        super(row);
        <#list fields as field>
            <#if field.type == "String">
        this.${field.name} = row.getString("${field.col}", "${field.def}");
            <#elseif field.type == "Integer">
        this.${field.name} = row.getInteger("${field.col}", ${field.def});
            <#elseif field.type == "Double">
        this.${field.name} = row.getDouble("${field.col}", ${field.def});
            <#elseif field.type == "Long">
        this.${field.name} = row.getLong("${field.col}", ${field.def}L);
            <#elseif field.type == "Instant">
        this.${field.name} = row.getInstant("${field.col}", Instant.EPOCH);
            <#elseif field.type == "Boolean">
        this.${field.name} = row.getBoolean("${field.col}", ${field.def});
            <#elseif field.type == "JsonObject">
        this.${field.name} = new JsonObject(row.getString("${field.col}", "{}"));
            <#elseif field.type == "JsonArray">
        this.${field.name} = new JsonArray(row.getString("${field.col}", "[]"));
            </#if>
        </#list>
    }

    <#list fields as field>
    private ${field.type} ${field.name};
    </#list>

    <#if isView == 0>
    private static final String sql_insert = "${sql_insert}";

    public ExecRow insertRow() {
        return ExecRow.build(
                sql_insert,
                this.insertArgs()
                         <#list fields as field>
                         .add(this.${field.name})
                         </#list>
        );
    }

    private static final String sql_update = "${sql_update}";

    public ExecRow updateRow() {
        return ExecRow.build(
                sql_update,
                this.updateArgs()
                         <#list fields as field>
                         .add(this.${field.name})
                         </#list>
                         .add(this.id)
                         .add(this.version)
        );
    }

    private static final String sql_delete_force = "${sql_delete_force}";

    private static final String sql_delete = "${sql_delete}";

    </#if>

    <#list fields as field>
    public ${field.type} get${field.nameU}() {
        return ${field.name};
    }

    public void set${field.nameU}(${field.type} ${field.name}) {
        this.${field.name} = ${field.name};
    }
    </#list>

}
