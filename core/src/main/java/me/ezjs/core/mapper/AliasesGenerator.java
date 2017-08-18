package me.ezjs.core.mapper;

import me.ezjs.core.model.Operate;
import me.ezjs.core.model.Parameter;
import me.ezjs.core.model.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AliasesGenerator {

    public static final String alias = "obj";
    public static final String filterAlias = "filter";

    private static final String SPACE = " ";
    private static final String DOT = ".";

    private String entityName;
    private Map<String, String> map;

    private String fromClause;

    public AliasesGenerator(String entryName) {
        this.entityName = entryName;
        map = new HashMap<>();
        fromClause = entityName + SPACE + alias;// = "entityName obj";
    }

    /**
     * 暂时不支持在主表A上同时查询A.B.C.field和A.C.field的方式.
     *
     * @param p
     */
    public void analyse(Parameter p) {
        if (p.isLink()) {
            for (Parameter pm : p.getAoList()) {
                analyse(pm);
            }
            return;
        }
        String field = p.getKeyUnderLine();
        if (field.indexOf('.') == -1) {// 就没有 . ;必定是主表的
            p.setKey(alias + DOT + field); // = "obj.field";
        } else {// 有小数点,但是不知道是几个,可能是关联表的关联表的字段.
            StringTokenizer st = new StringTokenizer(field, ".");
            String last = null;
            int allCount = st.countTokens();
            int index = 0;
            while (st.hasMoreElements()) {
                index++;
                if (index == allCount) {
                    break;
                }
                String fi = (String) st.nextElement();
                if (fi.equals(entityName)) {
                    break;
                }
                if (map.get(fi) == null) {
                    String al = alias + map.size();
                    map.put(fi, al);
                    if (p.isOrSearch()) {
                        fromClause += " left join " + (last == null ? alias : map.get(last)) + DOT + fi + " "
                                + al;
                    } else {
                        fromClause += " inner join " + (last == null ? alias : map.get(last)) + DOT + fi
                                + " " + al;
                    }

                    p.setKey(al + field.substring(field.lastIndexOf('.')));
                } else {
                    p.setKey(map.get(fi) + field.substring(field.lastIndexOf('.')));
                }
                last = fi;
            }
        }

    }

    public void analyse(Sort s) {

        String field = s.getFieldUnderLine();
        if (field.indexOf('.') == -1) {// 就没有 . ;必定是主表的
            s.setField(alias + DOT + field); // = "obj.field";
        } else {// 有小数点,但是不知道是几个,可能是关联表的关联表的字段.
            StringTokenizer st = new StringTokenizer(field, ".");
            String last = null;
            int allCount = st.countTokens();
            int index = 0;
            while (st.hasMoreElements()) {
                index++;
                if (index == allCount) {
                    break;
                }
                String fi = (String) st.nextElement();
                if (fi.equals(entityName)) {
                    break;
                }
                if (map.get(fi) == null) {
                    String al = alias + map.size();
                    map.put(fi, al);
                    fromClause += " inner join " + (last == null ? alias : map.get(last)) + DOT + fi + " "
                            + al;

                    s.setField(al + field.substring(field.lastIndexOf('.')));
                } else {
                    s.setField(map.get(fi) + field.substring(field.lastIndexOf('.')));
                }
                last = fi;
            }
        }
    }

    public String getFromClause() {
        return this.fromClause;
    }

    public static void main(String[] args) {
        AliasesGenerator ag = new AliasesGenerator("Personnel");
        ag.analyse(new Parameter("org.otherName.name", "", Operate.LIKE));
        ag.analyse(new Parameter("org.name", "", Operate.LIKE));
        ag.analyse(new Parameter("org.event.name", "", Operate.LIKE));
        // ag.analyse(new Parameter("event.name", "", Operate.LIKE)); Not
        // supported.
        System.out.println(ag.getFromClause());
    }

}
