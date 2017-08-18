package me.ezjs.core.mapper;

import me.ezjs.core.model.DAOFilter;
import me.ezjs.core.model.FlipFilter;
import me.ezjs.core.model.Parameter;
import me.ezjs.core.model.Sort;
import me.ezjs.core.model.orm.FieldColumn;
import me.ezjs.core.util.BeanUtil;
import me.ezjs.core.util.StringUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zero-mac on 16/7/1.
 */
public class SqlBuilder {

    protected static final boolean IS_ESCAPE_SLASH = false;
    protected final static String ESCAPE = " escape '\\'";

    protected Class persistentClass;
    protected String entityName;
    protected String tableName;

    protected String filterAlias;

    public SqlBuilder(Class clazz) {
        this.persistentClass = clazz;
        this.entityName = persistentClass.getSimpleName();
        this.tableName = BeanUtil.getTableName(clazz);
    }

    public SqlBuilder(Class clazz, String filterAlias) {
        this.persistentClass = clazz;
        this.entityName = persistentClass.getSimpleName();
        this.tableName = BeanUtil.getTableName(clazz);
        this.filterAlias = filterAlias;
    }

    /**
     * * 自定义 select 语句, TODO:完成select语句的封装.
     * <p>
     * 使用obj.*的原因是因为可以直接将结果转换为对象. 而使用obj.id, obj.name时只能返回为map,不是对象.
     *
     * @param filter
     * @return
     */
    public String buildSelectClause(DAOFilter filter) {
        return buildSelectClause(filter, null);
    }

    public String buildSelectClause(DAOFilter filter, String alias) {
        boolean hasAlias = false;
        if (alias != null && alias.length() > 0) {
            hasAlias = true;
        }
        List<String> sc = filter.getSelect();
        if (sc == null) {
            StringBuffer sb = new StringBuffer();
            sb.append(hasAlias ? alias + ".id" : "id");
            Class clazz = filter.getPersistentClass();
            List<FieldColumn> fcs = BeanUtil.getFieldsWithReturnValue(clazz);
            for (FieldColumn fc :
                    fcs) {
                String field = fc.getField();
                if (!field.equals("id")) {
                    String column = fc.getColumn();
                    String aliasField = hasAlias ? alias + "." + column : column;
                    sb.append(", " + aliasField + " as " + field);
                }
            }
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String s : sc) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(AliasesGenerator.alias + "." + s);
            }
            return sb.toString();
        }
    }

    protected String buildFromClause(DAOFilter filter, AliasesGenerator ag) {
        if (filter == null) {
            return tableName + " " + AliasesGenerator.alias;
        }
        boolean noAnd = filter.getPl() == null || filter.getPl().size() <= 0;
        boolean noOr = filter.getPor() == null || filter.getPor().size() <= 0;
        boolean noSort = filter.getSl() == null || filter.getSl().size() <= 0;
        if (noAnd && noOr && noSort) {
            return tableName + " " + AliasesGenerator.alias;
        }
        if (ag == null) {
            ag = new AliasesGenerator(tableName);
        }
        if (!noAnd) {
            for (Parameter p : filter.getPl()) {// 这儿的PL一定是验证过的.
                ag.analyse(p);
            }
        }
        if (!noOr) {
            for (List<Parameter> porl : filter.getPor()) {
                for (Parameter p : porl) {
                    ag.analyse(p);
                }
            }
        }
        if (!noSort) {
            for (Sort s : filter.getSl()) {
                ag.analyse(s);
            }
        }

        return ag.getFromClause();
    }

    protected AliasesGenerator buildFromClauseForFlip(FlipFilter filter) {
        AliasesGenerator ag = new AliasesGenerator(tableName);
        return ag;
    }

    /**
     * 所有的过滤条件都放在parameterList中,与map无关.
     *
     * @param filter
     * @param hqlBuilder
     */
    protected final void buildWhereClause(DAOFilter filter, StringBuffer hqlBuilder) {
        if (filter == null) {
            return;
        }
        boolean noAnd = filter.getPl() == null || filter.getPl().size() <= 0;
        boolean noOr = filter.getPor() == null || filter.getPor().size() <= 0;
        if (noAnd && noOr) {
            return;
        }

        boolean first = true;
        hqlBuilder.append(" where ");

        // do and.
        if (!noAnd) {
            for (Parameter p : filter.getPl()) {
                if (first) {
                    first = false;
                } else {
                    hqlBuilder.append(" and ");
                }
                if (!p.isLink()) {
                    buildOperateClause(p, hqlBuilder);
                } else {

                    StringBuffer linkHql = new StringBuffer();
                    buildOperateClause4Link(p, linkHql, p.isOrSearch());
                    hqlBuilder.append(linkHql.toString());
                }
            }
        }

        // do or.
        if (!noOr) {

            for (List<Parameter> porl : filter.getPor()) {
                if (first) {
                    first = false;
                } else {
                    hqlBuilder.append(" and ");
                }
                hqlBuilder.append(" ( ");

                boolean orf = true;

                for (Parameter p : porl) {
                    if (orf) {
                        orf = false;
                    } else {
                        hqlBuilder.append(" or ");
                    }
                    buildOperateClause(p, hqlBuilder);
                }

                hqlBuilder.append(" ) ");
            }
        }
    }

    protected final static void buildOrderClause(DAOFilter filter, StringBuffer hqlBuilder) {
        if (filter == null) {
            return;
        }
        if ((StringUtil.assertNull(filter.getSortField()) && (filter.getSl() == null || filter.getSl().size() == 0))) {
            return;
        }

        boolean first = true;
        if (!StringUtil.assertNull(filter.getSortField())) {
            first = false;
            hqlBuilder.append(" order by ").append(filter.getSortField())
                    .append(' ' + (filter.isSortDesc() ? "DESC" : "ASC"));
        }
        if (!(filter.getSl() == null || filter.getSl().size() == 0)) {
            for (Sort s : filter.getSl()) {
                if (first) {
                    first = false;
                    hqlBuilder.append(" order by ").append(s.getFieldUnderLine())
                            .append(' ' + (s.isDesc() ? "DESC" : "ASC"));
                } else {
                    hqlBuilder.append(" , ").append(s.getFieldUnderLine()).append(' ' + (s.isDesc() ? "DESC" : "ASC"));
                }
            }
        }
    }

    protected final void buildOperateClause(Parameter p, StringBuffer hqlBuilder) {
        String bind = p.getBindName();

        switch (p.getOperate()) {
            case ISNULL:
            case NOTNULL:
                hqlBuilder.append(p.getKeyUnderLine());
                hqlBuilder.append(p.getOperate().getSql());
                break;
            case EQUAL:
            case NOTEQUAL:
            case LESS:
            case LESSEQUAL:
            case GREAT:
            case GREATEQUAL:
                hqlBuilder.append(p.getKeyUnderLine());
                hqlBuilder.append(p.getOperate().getSql());
                hqlBuilder.append(bindKey(bind));
                break;
            case LIKE:
                hqlBuilder.append(p.getKeyUnderLine());
                hqlBuilder.append(p.getOperate().getSql());
                hqlBuilder.append(bindKey(bind));
                if (IS_ESCAPE_SLASH) {
                    hqlBuilder.append(ESCAPE);
                }
                break;
            case LOWERLIKE:
                hqlBuilder.append("lower(" + p.getKeyUnderLine() + ")");
                hqlBuilder.append(p.getOperate().getSql());
                hqlBuilder.append(bindKey(bind));
                if (IS_ESCAPE_SLASH) {
                    hqlBuilder.append(ESCAPE);
                }
                break;
            case BETWEEN:
                hqlBuilder.append(p.getKeyUnderLine());
                hqlBuilder.append(p.getOperate().getSql());
                hqlBuilder.append(bindKey(bind + "lo"));
                hqlBuilder.append(" and ");
                hqlBuilder.append(bindKey(bind + "hi"));

                break;
            case IN:
            case NOTIN:
                hqlBuilder.append(p.getKeyUnderLine());
                hqlBuilder.append(p.getOperate().getSql());
//                hqlBuilder.append(bindKey(bind));
                //FIXME
                hqlBuilder.append("(");

                String in = "";
                Collection c = (Collection) p.getValue();
                Iterator iterator = c.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (next instanceof String) {
                        in += "'" + (next) + "',";
                    } else {
                        in += (next) + ",";
                    }
                }
                hqlBuilder.append(in.substring(0, in.length() - 1));

                hqlBuilder.append(")");

                break;
            default: // impossible
                break;
        }
    }

    protected void buildOperateClause4Link(Parameter p, StringBuffer sb, boolean isOr) {
        if (p.isLink()) {
            sb.append("(");

            List<Parameter> aoList = p.getAoList();
            for (int i = 0; i < aoList.size(); i++) {
                Parameter pm = aoList.get(i);
                buildOperateClause4Link(pm, sb, p.isOrSearch());
                if (i != aoList.size() - 1) {
                    if (p.isOrSearch()) {
                        sb.append(" or ");
                    } else {
                        sb.append(" and ");
                    }
                }
            }
            sb.append(")");
        } else {
            buildOperateClause(p, sb);
        }
    }

//    protected final static void validateFlip(FlipFilter filter) {
//        if (filter.getPageNo() < 1) {
//            // throw new DAOException("Invalid page no.");
//            // change to default instead.
//            filter.setPageNo(1);
//        }
//        if (filter.getPageSize() <= 0 || filter.getPageSize() > 10000) {
//            filter.setPageSize(Constants.DEFAULT_PAGE_SIZE);
//        }
//        filter.setOffset((filter.getPageNo() - 1) * filter.getPageSize());
//        return;
//    }

    protected final String bindKey(String key) {
        if (StringUtil.isNull(this.filterAlias)) {
            return "#{" + key + "}";
        }
        return "#{" + this.filterAlias + "." + key + "}";
    }

}
