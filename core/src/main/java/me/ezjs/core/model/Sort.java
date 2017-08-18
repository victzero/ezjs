package me.ezjs.core.model;

import me.ezjs.core.util.BeanUtil;
import me.ezjs.core.util.StringUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 单一匹配基本类./LIKE/EQUAL/NOTEQUAL/ISNULL/NOTNULL/. 默认为like.
 * <p>
 * <pre>
 * fieldName_0 --> LIKE. fieldName_1 --> EQUAL. fieldName_2 --> NOTEQUAL.
 * fieldName_3 --> ISNULL. fieldName_4 --> NOTNULL.
 *
 * @author zhujs
 */
public class Sort extends RootObject {

    private static final long serialVersionUID = 731333569681855679L;

    public Sort() {
    }

    public Sort(String field, boolean desc) {
        this.field = field;
        this.desc = desc;
    }

    private String field;
    private boolean desc;

    public String getField() {
        return field;
    }

    public String getFieldUnderLine() {
        if (StringUtil.isNull(field)) {
            return null;
        }
        return BeanUtil.camel2underline(field);
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("field",
                this.field).append("desc", this.desc);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Sort)) {
            return false;
        }

        if (o.hashCode() == this.hashCode()) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.field.hashCode() * 3 * (this.desc ? 1 : -1);
    }

}
