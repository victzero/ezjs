package me.ezjs.core.mapper;

import me.ezjs.core.cache.ThreadCache;
import me.ezjs.core.model.*;
import me.ezjs.core.model.orm.FieldColumn;
import me.ezjs.core.util.BeanUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zero-mac on 16/6/30.
 */
public class SqlProvider {

    protected final Log log = LogFactory.getLog(getClass());


    private void logForSql(final String table_name, final Object object) {
        try {
            String sqlForLog = new SQL() {
                {
                    Map map = BeanUtil.bean2Map(object);
                    INSERT_INTO(table_name);
                    for (FieldColumn fc : BeanUtil.getFieldsWithReturnValue(object)) {
                        String column = fc.getColumn();
                        VALUES(column, String.valueOf(map.get(fc.getField())));
                    }
                }
            }.toString();
            List listSql = (List) ThreadCache.getData("sqlForLog");
            if (listSql == null) {
                listSql = new ArrayList();
            }
            listSql.add(sqlForLog);
            ThreadCache.putData("sqlForLog", listSql);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成sql-插入单条记录.
     *
     * @param object
     * @return
     */
    public String insert(final Object object) {
        //更新createTime
        RootObject ro = (RootObject) object;
        ro.setModified();

        Class clazz = object.getClass();
        final String table_name = BeanUtil.getTableName(clazz);

        String sql = new SQL() {
            {
                INSERT_INTO(table_name);
                for (FieldColumn fc : BeanUtil.getFieldsWithReturnValue(object)) {
                    String column = fc.getColumn();
                    VALUES(column, fieldAlias(fc.getField()));
                }

            }
        }.toString();
        log.info(sql);
        //logForSql(table_name, object);
        return sql;
    }

    private boolean inArray(String field, String[] fields) {
        if (field == null) {
            return false;
        }
        for (String f : fields) {
            if (field.equals(f)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 仅更新指定列的数据
     *
     * @param object
     * @param fields
     * @return
     */
    public String updateFields(final Object object, final String[] fields) {
        //更新createTime
        RootObject ro = (RootObject) object;
        ro.setModified();

        Class clazz = object.getClass();
        final String table_name = BeanUtil.getTableName(clazz);

        String sql = new SQL() {
            {
                UPDATE(table_name);

                for (FieldColumn fc : BeanUtil.getFieldsWithReturnValue(object)) {
                    String field = fc.getField();
                    if (!inArray(field, fields)) {
                        continue;
                    }
                    if (field.equals("id")) {
                        continue;
                    }
                    String column = fc.getColumn();
                    if (column.equals("create_time")) {
                        continue;
                    }
                    SET(column + " = " + fieldAlias(field));
                }

                WHERE("id = #{id}");
            }
        }.toString();
        logForSql(table_name, object);
        return sql;
    }

    public String updateByFilter(final Object object, final DAOFilter filter) {

        //更新createTime
        RootObject ro = (RootObject) object;
        ro.setModified();

        Class clazz = object.getClass();
        final String table_name = BeanUtil.getTableName(clazz);

        //1.构建set相关sql
        String sql = new SQL() {
            {
                UPDATE(table_name);

                for (FieldColumn fc : BeanUtil.getFieldsWithReturnValue(object)) {
                    String field = fc.getField();
                    if (field.equals("id")) {
                        continue;
                    }
                    String column = fc.getColumn();
                    if (column.equals("create_time")) {
                        continue;
                    }
                    SET(column + " = " + fieldAliasWithObj(field));
                }
            }
        }.toString();

        //2.构建where 相关sql
        StringBuffer whereClause = new StringBuffer();
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass(), AliasesGenerator.filterAlias);
        builder.buildWhereClause(filter, whereClause);
        sql += whereClause;
        log.debug(sql);
        return sql;
    }


    public String update(final Object object) {
        //更新createTime
        RootObject ro = (RootObject) object;
        ro.setModified();

        Class clazz = object.getClass();
        final String table_name = BeanUtil.getTableName(clazz);

        String sql = new SQL() {
            {
                UPDATE(table_name);

                for (FieldColumn fc : BeanUtil.getFieldsWithReturnValue(object)) {
                    String field = fc.getField();
                    if (field.equals("id")) {
                        continue;
                    }
                    String column = fc.getColumn();
                    if (column.equals("create_time")) {
                        continue;
                    }
                    SET(column + " = " + fieldAlias(field));
                }

                WHERE("id = #{id}");
            }
        }.toString();

        logForSql(table_name, object);
        return sql;
    }

    /**
     * #{field}
     *
     * @param field
     * @return
     */
    protected String fieldAlias(String field) {
        return "#{" + field + "}";
    }

    /**
     * #{field}
     *
     * @param field
     * @return
     */
    protected String fieldAliasWithObj(String field) {
        return "#{obj." + field + "}";
    }

    public String getById(Class clazz) {
        final String table_name = BeanUtil.getTableName(clazz);

        DAOFilter filter = new DAOFilter(clazz);
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass());

        StringBuffer sb = new StringBuffer(256);
        sb.append("select distinct ").append(builder.buildSelectClause(filter)).append(" from ");
        sb.append(table_name + " where id = #{id}");
        final String sql = sb.toString();

        log.debug(sql);
        return sql;
    }

    public String deleteById(Class clazz) {
        final String table_name = BeanUtil.getTableName(clazz);
        String sql = "delete from " + table_name + " where id = #{id}";
        log.info(sql);
        return sql;
    }

    /**
     * 根据条件删除数据
     */
    public String deleteByFilter(DAOFilter filter) {
        //表名
        final String table_name = BeanUtil.getTableName(filter.getPersistentClass());
        StringBuffer sb = new StringBuffer();
        sb.append("delete from ").append(table_name);
        //构建where 条件
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass());
        builder.buildWhereClause(filter, sb);
        log.info(sb);
        return sb.toString();
    }

    public String selectTop1(Class clazz, String field, Object val) {
        FlipFilter filter = new FlipFilter(clazz);
        filter.addSearch(val, Operate.EQUAL, field);
        filter.setPageNo(1);
        filter.setPageSize(1);
        return flip(filter);
    }

    public String flipFirst(FlipFilter filter) {
        filter.setPageNo(1);
        filter.setPageSize(1);
        return flip(filter);
    }

    /**
     * 生成sql-根据条件查询符合的记录.
     *
     * @param filter
     * @return
     */
    public String select(DAOFilter filter) {
        Assert.notNull(filter.getPersistentClass());
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass());

        StringBuffer sb = new StringBuffer(256);
        sb.append("select distinct ").append(builder.buildSelectClause(filter)).append(" from ");
        sb.append(builder.buildFromClause(filter, null));
        builder.buildWhereClause(filter, sb);
        builder.buildOrderClause(filter, sb);
        final String sql = sb.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * 生成sql-根据条件查询符合的记录.
     *
     * @param filter
     * @return
     */
    public String flip(FlipFilter filter) {
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass());
        filter.validate();
        AliasesGenerator ag = builder.buildFromClauseForFlip(filter);
        String fromClause = builder.buildFromClause(filter, ag);

        StringBuffer hqlBuilder = new StringBuffer(256);
        if (filter.isDistinct()) {
            hqlBuilder.append("select distinct " + builder.buildSelectClause(filter)).append(" from ")
                    .append(fromClause);
        } else {
            hqlBuilder.append("select " + builder.buildSelectClause(filter)).append(" from ").append(fromClause);
        }
        builder.buildWhereClause(filter, hqlBuilder);
        builder.buildOrderClause(filter, hqlBuilder);
        hqlBuilder.append(" limit " + filter.getOffset() + ", " + filter.getPageSize());
        final String sql = hqlBuilder.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * 生成sql-根据条件查询符合的记录数.
     *
     * @param filter
     * @return
     */
    public String count(DAOFilter filter) {
        StringBuffer sb = new StringBuffer(256);
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass());

        sb.append("select count(*) from ").append(builder.buildFromClause(filter, null));
        builder.buildWhereClause(filter, sb);
        final String sql = sb.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * suny 生成sql-根据条件查询符合的记录数.(支持max查询)
     *
     * @param filter
     * @return
     */
    public String countMax(DAOFilter filter) {
        StringBuffer sb = new StringBuffer(256);
        SqlBuilder builder = new SqlBuilder(filter.getPersistentClass());

        sb.append("select count(*) from (select * from (select distinct * from ").append(builder.buildFromClause(filter, null));
        builder.buildWhereClause(filter, sb);
//        sb.append(" order by " + filter.getMaxFieldName()).append(" desc) as max430 group by ").append(filter.getGroupFieldName() + ") as max566");
        final String sql = sb.toString();
        log.info(sql);
        return sql;
    }

    /**
     * 直接执行SQL语句
     *
     * @param sql
     * @return
     */
    public String sql(String sql) {
        return sql;
    }

}
