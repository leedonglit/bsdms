package com.core.security.page;

public class ColDefine {

	String col;
    String value[];
    String type;
    String valueType = String.class.getName();
    public String getValueType() {
        return valueType;
    }
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    public String getCol() {
        return col;
    }
    public void setCol(String col) {
        this.col = col;
    }
    public String[] getValue() {
        return value;
    }
    public void setValue(String[] values) {
        this.value = values;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    String[] sqlValueEncode(String value[]) {
        if (value == null)
            return new String[0];
        for (int i = 0; i < value.length; i++) {
            value[i] = sqlValueEncode(value[i]);
        }
        return value;
    }
    String sqlValueEncode(String value) {
        if (value == null)
            return "";
        value = value.toString().replaceAll("'", "''");
        return value;
    }
    //	String string = "eq,like,left,right,not,eqleft,eqright";
    public String toParamValue() {
        if ("like".equals(type)) {
            return "   " + col + " like ? ";
        }
        if ("in".equals(type)) {
            return "   " + col + "   ";
        }
        return "    " + col + " " + type + " ? ";
    }
    public String toParamValue(int i) {
        if ("like".equals(type)) {
            return "   " + col.split(",")[i] + " like ? ";
        }
        if ("in".equals(type)) {
            return "   " + col.split(",")[i] + "   ";
        }
        return "    " + col.split(",")[i] + " " + type + " ? ";
    }
    public String toString() {
        Object value0 = sqlValueEncode(value[0]);
        String v = "";
        if (value0 instanceof Number) {
            v = value0.toString();
        } else {
            v = "'" + value0 + "'";
        }
        if ("=".equals(type) || ">".equals(type) || "<".equals(type) || ">=".equals(type) || "<=".equals(type)) {
            return "  and " + col + " " + type + " " + v;
        }
        if ("like".equals(type)) {
            return "  and " + col + " like '%" + value0 + "%'";
        }
         if ("in".equals(type)) {
          //  String str = "";
            return " and " + col + " in (" + value0 + ")";
        }
        return "";
    }
}
