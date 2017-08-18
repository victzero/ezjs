package me.ezjs.core.model.orm;

import me.ezjs.core.util.BeanUtil;

import java.lang.reflect.Method;

/**
 * Created by zero-mac on 17/8/13.
 */
public class FieldColumn {

    public FieldColumn(String field) {
        this.field = field;
    }

    private String field;
    private String column;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setColumnByCheck(boolean ignoreCamelFormatInAll, Method method, String field) {
        if (ignoreCamelFormatInAll) {
            this.column = (field);
        } else {
            if (method.getAnnotation(IgnoreCamelFormat.class) == null) {
                String dbField = BeanUtil.camel2underline(field);
                this.column = (dbField);
            } else {
                this.column = (field);
            }
        }
        return;
    }
}
