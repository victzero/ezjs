package me.ezjs.core.model;

import me.ezjs.core.util.BeanUtil;
import me.ezjs.core.util.StringUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.*;
import java.util.*;

/**
 * TODO:此处继承了HashMap后无法自动注入了
 * Created by zero-mac on 16/6/29.
 */
public class DAOFilter extends HashMap {

    protected Class persistentClass;
    protected String entityName;

    public DAOFilter() {
    }

    public DAOFilter(Class clazz) {
        setClass(clazz);
    }

    public void setClass(Class clazz) {
        this.persistentClass = clazz;
        this.entityName = persistentClass.getSimpleName();
    }

    private boolean distinct = true;

    public boolean isDistinct() {
        return distinct;
    }

    public Class getPersistentClass() {
        return persistentClass;
    }

    public String getEntityName() {
        return entityName;
    }

    /**
     * 绑定查询条件的值到map属性中.
     *
     * @param key
     * @param val
     */
    public void bindProperties(String key, Object val) {
        super.put(key, val);
    }

    /**
     * 自定义 select 语句, TODO:完成select语句的封装.
     * <p/>
     * 使用obj.*的原因是因为可以直接将结果转换为对象. 而使用obj.id, obj.name时只能返回为map,不是对象.
     */
    private List<String> selectList = null;

    public List<String> getSelect() {
        return selectList;
    }

    public void setSelect(List<String> selectList) {
        this.selectList = selectList;
    }

    public DAOFilter addSelect(String... arr) {
        if (this.selectList == null) {
            this.selectList = new ArrayList<String>();
        }
        for (String s :
                arr) {
            this.selectList.add(s);
        }
        return this;
    }

    /**
     * 单一查询.
     * <p/>
     * LIKE.0
     * <p/>
     * <pre>
     * < input type="hidden" name="p['fieldName'].value" value="查询内容" />
     * 或者
     * < input type="hidden" name="p['fieldName_LK'].value" value="查询内容" />
     * </pre>
     * <p/>
     * EQUAL.1
     * <p/>
     * <pre>
     * < input type="hidden" name="p['fieldName_EQ'].value" value="查询内容" />
     * </pre>
     * <p/>
     * NOTEQUAL.2
     * <p/>
     * <pre>
     * < input type="hidden" name="p['fieldName_NE'].value" value="查询内容" />
     * </pre>
     * <p/>
     * ISNULL.3
     * <p/>
     * <pre>
     * < input type="hidden" name="p['fieldName_IN'].value" value="0" />
     * </pre>
     * <p/>
     * NOTNULL.4
     * <p/>
     * <pre>
     * < input type="hidden" name="p['fieldName_NN'].value" value="0" />
     * </pre>
     */
    private Map<String, Parameter> s;

    private List<Parameter> pl = new ArrayList<Parameter>();

    public void setS(Map<String, Parameter> s) {
        this.s = s;
    }

    public List<Parameter> getPl() {
        return pl;
    }

    public void setPl(List<Parameter> pl) {
        this.pl = pl;
    }

    /**
     * 增加单一查询条件,支持大多数运算符.
     * <p/>
     *
     * @param value     字段值
     * @param op        查询运算 {@code com.asd.model.DAOFilter.Operate } operate
     * @param fieldName 字段名
     * @return {@code DAOFilter} DAOFilter
     */
    public DAOFilter addSearch(Object value, Operate op, String fieldName) {
        if (!validateSearchValue(value, op)) {
            return this;
        }
        Parameter pm = new Parameter(fieldName, value, op);
        String bindName = "P" + pl.size();
        pm.setBindName(bindName);
        bindProperties(bindName, pm.getValue());
        pl.add(pm);
        return this;
    }

    private boolean validateSearchValue(Object val, Operate op) {
        if (val == null) {
            switch (op) {
                case LIKE:
                case LOWERLIKE:
                case EQUAL:
                case NOTEQUAL:
                case IN:
                case NOTIN:
                case LESSEQUAL:
                case GREAT:
                case GREATEQUAL:
                    return false;
                case ISNULL:
                case NOTNULL:
                    return true;
                default:
                    return false;
            }
        }
        if (val instanceof String) {
            String v = (String) val;
            if ("".equals(v) || "%".equals(v) || "%%".equals(v)) {
                return false;
            }
        }
        return true;
    }

    private List<List<Parameter>> por = null;

    public List<List<Parameter>> getPor() {
        return por;
    }

    public void setPor(List<List<Parameter>> por) {
        this.por = por;
    }

    /**
     * 或查询,支持类似 (field1 = value or field2 = value or ...)或条件的查询操作.
     * <p/>
     * 注:不支持页面直接封装.
     *
     * @param value     需要匹配的值
     * @param op        统一的查询运算符
     * @param fieldName 各个字段
     * @return {@code DAOFilter} DAOFilter
     */
    public DAOFilter addOrSearch(Object value, Operate op, String... fieldName) {
        if (value == null || (value instanceof String && ((String) value).trim().length() == 0)) {
            return this;
        }
        List<Parameter> porl = new ArrayList<Parameter>();
        if (por == null) {
            por = new ArrayList<List<Parameter>>();
        }

        int index = 0;
        for (String field : fieldName) {
            index++;
            String bindName = "OR" + por.size() + index;
            Parameter p = new Parameter(field, value, op, "OR" + por.size() + index);
            p.setOrSearch(true);
            bindProperties(bindName, p.getValue());
            porl.add(p);
        }

        por.add(porl);
        return this;
    }

    public DAOFilter addOrSearchSameKey(String fieldName, Operate op, Object... values) {
        List<Parameter> porl = new ArrayList<Parameter>();
        if (por == null) {
            por = new ArrayList<List<Parameter>>();
        }

        int index = 0;
        for (Object val : values) {
            index++;
            String bindName = "OR" + por.size() + index;
            Parameter p = new Parameter(fieldName, val, op, bindName);
            p.setOrSearch(true);
            bindProperties(bindName, p.getValue());
            porl.add(p);
        }

        por.add(porl);
        return this;
    }

    public DAOFilter addOrSearchSameKey(List<Object> values, Operate op, String fieldName) {
        return addOrSearchSameKey(fieldName, op, values.toArray());
    }

    /**
     * or高级查询.
     *
     * @param porl
     * @return
     */
    public DAOFilter addOrSearch(List<Parameter> porl) {
        if (por == null) {
            por = new ArrayList<List<Parameter>>();
        }

        int index = 0;
        for (Parameter p : porl) {
            index++;
            String bindName = "OR" + por.size() + index;
            p.setBindName(bindName);
            bindProperties(bindName, p.getValue());
        }

        por.add(porl);
        return this;
    }

    /**
     * addOrSearch使用方法方法.
     */
    @SuppressWarnings("unused")
    private void addOrSearchTest() {
        Parameter p1 = new Parameter("fieldName1", "v1", Operate.EQUAL);
        Parameter p2 = new Parameter("fieldName2", "v2", Operate.NOTEQUAL);
        Parameter p3 = new Parameter("fieldName3", "v3", Operate.LIKE);

        List<Parameter> pl = new ArrayList<Parameter>();
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);

        addOrSearch(pl);
    }

    /**
     * 清空已有or查询条件.
     *
     * @return
     */
    public DAOFilter clearOrSearch() {
        por = new ArrayList<List<Parameter>>();// 清空OrSearch条件
        return this;
    }

    /**
     * 清空所有已有or查询后再添加新的or查询条件.
     *
     * @param value
     * @param op
     * @param fieldName
     * @return
     */
    public DAOFilter setOrSearch(Object value, Operate op, String... fieldName) {
        if (value == null || (value instanceof String && ((String) value).trim().length() == 0)) {
            return this;
        }
        por = new ArrayList<List<Parameter>>();// 清空OrSearch条件
        List<Parameter> porl = new ArrayList<Parameter>();

        int index = 0;
        for (String field : fieldName) {
            index++;
            String bindName = "OR" + por.size() + index;
            Parameter p = new Parameter(field, value, op, bindName);
            porl.add(p);
            bindProperties(bindName, p.getValue());
        }

        por.add(porl);
        return this;
    }

    public DAOFilter setPaoSearch(Parameter pm) {
        bindAllPao(pm, "", 0);
        pl.add(pm);
        return this;
    }

    public void bindAllPao(Parameter pm, String pre, int index) {
        if (pm.isLink()) {
            List<Parameter> aol = pm.getAoList();
            for (int i = 0; i < aol.size(); i++) {
                bindAllPao(aol.get(i), pre + index, i);
            }
        } else {
            // System.out.println("P" + pl.size() + "_" + pre + "_" + index);
            String bindName = "P" + pl.size() + "_" + pre + "_" + index;
            pm.setBindName(bindName);
            bindProperties(bindName, pm.getValue());
        }
    }

    public static void main(String[] args) {
//        DAOFilter filter = new DAOFilter();
//        Parameter p1 = new Parameter("sMonth", 3, Operate.LESSEQUAL);
//        Parameter p2 = new Parameter("bMonth", 3 + 1, Operate.GREATEQUAL);
//
//        Parameter p3 = new Parameter("sMonth", 7 - 1, Operate.LESSEQUAL);
//        Parameter p4 = new Parameter("bMonth", 7, Operate.GREATEQUAL);
//        Parameter p5 = new Parameter("bMonth", 7, Operate.GREATEQUAL);
//
//        // 组成 (p1 & p2) or (p3 & p4)
//        Parameter pa1 = new Parameter(false, p1, p2);
//        Parameter pa2 = new Parameter(false, p3, p4, p5);
//
//        Parameter po = new Parameter(true, pa1, pa2);
//        filter.setPaoSearch(po);
    }

    /**
     * 区间查询.对between运算符的支持.
     * <p/>
     * 若begin为空,则自动转为小于end;若end为空则自动转为大于begin.
     * <p/>
     * 支持页面的封装.封装在名为"r"的map中.
     * <p/>
     * <pre>
     * < input type="hidden" name="r['fieldName'].begin" value="开始值" />
     * </pre>
     * <p/>
     * <pre>
     * < input type="hidden" name="r['fieldName'].end" value="结束值" />
     * </pre>
     */
    public DAOFilter addRegion(String fieldName, Object begin, Object end) {
        Parameter pm = new Parameter(fieldName, begin, end, Operate.BETWEEN);
        String bindName = "P" + pl.size();
        pm.setBindName(bindName);
        bindProperties(bindName + "lo", pm.getBegin());
        bindProperties(bindName + "hi", pm.getEnd());
        pl.add(pm);
        return this;
    }

    /**
     * sortList
     */
    private List<Sort> sl = new LinkedList<Sort>();

    public List<Sort> getSl() {
        return sl;
    }

    public void setSl(List<Sort> sl) {
        this.sl = sl;
    }

    /**
     * 添加排序条件,支持多字段排序,排序优先级同添加的先后顺序.
     *
     * @param field 字段名
     * @param desc  是否降序
     * @return
     */
    public DAOFilter addSort(String field, boolean desc) {
        sl.add(new Sort(field, desc));
        return this;
    }

    /**
     * 排序字段名.只用于页面分页时;
     * <p/>
     * 升序.ascend
     * <p/>
     * <pre>
     * < input type="hidden" name="sort['fieldName'].desc" value="false" />
     * </pre>
     * <p/>
     * 降序.descend
     * <p/>
     * <pre>
     * < input type="hidden" name="sort['fieldName'].desc" value="true" />
     * </pre>
     */
    private String sortField;

    private boolean sortDesc;

    public String getSortField() {
        return sortField;
    }

    public String getSortFieldUnderLine() {
        if (StringUtil.isNull(sortField)) {
            return null;
        }
        return BeanUtil.camel2underline(sortField);
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isSortDesc() {
        return sortDesc;
    }

    public void setSortDesc(boolean sortDesc) {
        this.sortDesc = sortDesc;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
        for (Parameter p : pl) {
            sb.append("key", p.getKeyUnderLine()).append("begin", p.getBegin()).append("end", p.getEnd());
        }
        sb.append("  ||  map: " + super.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    public DAOFilter deepClone() {

        try {
            // 将对象写到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo;
            oo = new ObjectOutputStream(bo);
            oo.writeObject(this);
            // 从流里读出来
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (DAOFilter) (oi.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasPAndOrs() {
        return false;
    }


}
