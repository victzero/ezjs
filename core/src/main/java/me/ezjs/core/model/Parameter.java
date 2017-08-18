package me.ezjs.core.model;

import me.ezjs.core.util.BeanUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段查询参数,字段名+运算符+字段值
 *
 * @author zhujs
 */
public class Parameter extends RootObject {

    private static final long serialVersionUID = 5275723848080370068L;

    private String key;
    private Object begin;
    private Object end;

    private String bindName;
    private Operate operate;
    private boolean orSearch = false;

    private boolean isLink = false;// 是否为桥梁.
    private List<Parameter> aoList;

    public Parameter() {
    }

    public Parameter(String key, Object value, Operate operate) {
        this.key = key;
        this.begin = value;
        this.operate = operate;
    }

    public Parameter(String key, Object begin, Object end, Operate operate) {
        this.key = key;
        boolean hasBegin = false;
        boolean hasEnd = false;
        if (begin != null) {
            hasBegin = true;
        }
        if (end != null) {
            hasEnd = true;
        }
        if (hasBegin && hasEnd) {
            this.begin = begin;
            this.end = end;
            this.operate = Operate.BETWEEN;
            return;
        }
        if (hasBegin) {
            this.begin = begin;
            this.operate = Operate.GREATEQUAL;
            return;
        }
        if (hasEnd) {
            this.begin = end;
            this.operate = Operate.LESSEQUAL;
            return;
        }

    }

    public Parameter(String key, Object value, Operate operate, String bindName) {
        this.key = key;
        this.begin = value;
        this.operate = operate;
        this.bindName = bindName;
    }

    public Parameter(boolean isOrSearch, Parameter... ps) {
        this.isLink = true;
        this.orSearch = isOrSearch;
        this.aoList = new ArrayList<Parameter>();
        for (Parameter p : ps) {
            aoList.add(p);
        }
    }

    public String getKeyUnderLine() {
        return BeanUtil.camel2underline(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getBegin() {
        return begin;
    }

    public void setBegin(Object begin) {
        this.begin = begin;
    }

    public void setValue(Object value) {
        this.begin = value;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    public String getBindName() {
        return bindName;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
    }

    public Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

    public Object getValue() {
        return begin;
    }

    public boolean isOrSearch() {
        return orSearch;
    }

    public void setOrSearch(boolean orSearch) {
        this.orSearch = orSearch;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean isLink) {
        this.isLink = isLink;
    }

    public List<Parameter> getAoList() {
        return aoList;
    }

    public void setAoList(List<Parameter> aoList) {
        this.aoList = aoList;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("key", this.key)
                .append("begin", this.begin).append("end", this.end);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Parameter)) {
            return false;
        }

        if (o.hashCode() == this.hashCode()) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.key.hashCode() * 3 + this.begin.hashCode() * 5;
    }

}
