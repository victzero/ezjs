package me.ezjs.core.model;

/**
 * 查询运算的枚举类.
 * <p/>
 * LIKE("LK", " like "), LOWERLIKE("LL", " like "), EQUAL("EQ", " = "), NOTEQUAL("NE", " != "), ISNULL(
 * "ISN", " is null "), NOTNULL("NN", " is not null "), BETWEEN("BW", " between "), IN("IN", " in "),
 * NOTIN("NI", " not in "), LESS("L", " < "), LESSEQUAL("LE", " <= "), GREAT("G", " > "), GREATEQUAL(
 * "GE", " >= ")
 *
 * @author zhujs
 */
public enum Operate {
    LIKE("LK", " like "), LOWERLIKE("LL", " like "), EQUAL("EQ", " = "), NOTEQUAL("NE", " != "), ISNULL(
            "ISN", " is null "), NOTNULL("NN", " is not null "), BETWEEN("BW", " between "), IN("IN",
            " in "), NOTIN("NI", " not in "), LESS("L", " < "), LESSEQUAL("LE", " <= "), GREAT("G", " > "), GREATEQUAL(
            "GE", " >= ");

    private final String simple;
    private final String sql;

    Operate(String simple, String sql) {
        this.simple = simple;
        this.sql = sql;
    }

    public String getSimple() {
        return simple;
    }

    public String getSql() {
        return sql;
    }

    public int getIndex() {
        return this.getIndex();
    }

}