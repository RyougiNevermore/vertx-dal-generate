package org.pharosnet.vertx.dal.generate.def;

public class Field {

    public Field() {

    }

    private String name;
    private String type;
    private String col;
    private String def;
    private String nameU;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(name.charAt(0)))
                .append(name.substring(1));
        this.nameU = sb.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getNameU() {
        return nameU;
    }

    public void setNameU(String nameU) {
        this.nameU = nameU;
    }

}
