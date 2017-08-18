package me.ezjs.core.model;

/**
 * Created by Zjs-yd on 2017/1/18.
 */
public enum UsingType {

    in_using(1),
    logic_delete(0),
    physical_delete(-1),
    ;

    int code;
    UsingType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
