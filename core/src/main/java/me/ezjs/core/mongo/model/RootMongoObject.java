package me.ezjs.core.mongo.model;

import me.ezjs.core.util.JSONUtil;

import java.util.Date;

/**
 * Created by Zjs-yd on 2016/11/2.
 */
public class RootMongoObject {

    public RootMongoObject() {
        this.setModified();
    }

    public void setModified() {
        this.setModifyTime(new Date());
    }

    protected String id;
    protected Date createTime;
    protected Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
