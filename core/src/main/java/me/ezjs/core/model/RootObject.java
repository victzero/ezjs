package me.ezjs.core.model;

import me.ezjs.core.util.JSONUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zero-mac on 16/6/30.
 */
public abstract class RootObject implements Serializable {
    public RootObject() {
        this.usingType = UsingType.in_using.getCode();
        this.setModified();
    }

    public void setModified() {
        this.setModifyTime(new Date());
    }


    protected Integer id;
    protected Date createTime;
    protected Date modifyTime;

    protected Integer usingType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Integer getUsingType() {
        return usingType;
    }

    public void setUsingType(Integer usingType) {
        this.usingType = usingType;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        if (id == null) {
            this.createTime = modifyTime;
        }
    }

    public String toString() {
        return new JSONUtil(this).toString();
    }
}
