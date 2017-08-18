package me.ezjs.core.util;

import me.ezjs.core.mapper.ColumnTransient;
import me.ezjs.core.model.TableName;
import me.ezjs.core.model.orm.FieldColumn;
import me.ezjs.core.model.orm.IgnoreCamelFormat;
import me.ezjs.core.model.orm.IgnoreFieldsInRootObj;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zero-mac on 16/7/1.
 */
public class BeanUtil {

    public static String getFieldByMethod(Method method) {
        String name = method.getName();
        String field;
        if (name.startsWith("get")) {
            if ("getClass".equals(name) || "getDeclaringClass".equals(name)) {
                field = "";
            } else {
                field = name.substring(3);
            }
        } else if (name.startsWith("is")) {
            field = name.substring(2);
        } else {
            return "";
        }

        if (field.length() > 0 && Character.isUpperCase(field.charAt(0))
                && method.getParameterTypes().length == 0) {
            if (field.length() == 1) {
                field = field.toLowerCase();
            } else if (!Character.isUpperCase(field.charAt(1))) {
                field = field.substring(0, 1).toLowerCase() + field.substring(1);
            }
        }

        return field;
    }

    public static List<FieldColumn> getFieldsWithReturnValue(Class clazz) {
        List<FieldColumn> list = new ArrayList<>();

        boolean ignoreCamelFormatInAll = isIgnoreCamelFormatInClass(clazz);

        boolean includeSuperClass = clazz.getClassLoader() != null;
        final Method[] methods = includeSuperClass ? clazz.getMethods() : clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (Modifier.isPublic(method.getModifiers())) {
                String field = BeanUtil.getFieldByMethod(method);
                if (field.length() == 0) {
                    continue;
                }

                if (ignoreField(method)) {
                    continue;
                }
                if (ignoreFieldInRootObj(clazz, field)) {
                    continue;
                }

                FieldColumn f = new FieldColumn(field);
                f.setColumnByCheck(ignoreCamelFormatInAll, method, field);
                list.add(f);
            }
        }
        return list;
    }


    /**
     * 包含空字符窜检查.
     *
     * @param object
     * @return
     */
    public final static List<FieldColumn> getFieldsWithReturnValue(Object object) {
        Class clazz = object.getClass();
        List<FieldColumn> list = new ArrayList<>();
        boolean ignoreCamelFormatInAll = isIgnoreCamelFormatInClass(clazz);

        boolean includeSuperClass = clazz.getClassLoader() != null;
        final Method[] methods = includeSuperClass ? clazz.getMethods() : clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (Modifier.isPublic(method.getModifiers())) {
                String field = BeanUtil.getFieldByMethod(method);
                if (field.length() == 0) {
                    continue;
                }
                if (ignoreField(method)) {
                    continue;
                }
                if (ignoreFieldInRootObj(clazz, field)) {
                    continue;
                }
                try {
                    Object val = method.invoke(object);
                    if (val != null) {
                        Class<?> rt = method.getReturnType();
                        if (rt == String.class) {
                            //字符串不能为空.
                            if (((String) val).length() == 0) {
                                continue;
                            }
                        }

                        FieldColumn f = new FieldColumn(field);
                        f.setColumnByCheck(ignoreCamelFormatInAll, method, field);
                        list.add(f);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
        return list;
    }

    private static final boolean isIgnoreCamelFormatInClass(Class clazz) {

        boolean ignoreCamelFormatInAll = false;
        Annotation annotation = clazz.getAnnotation(IgnoreCamelFormat.class);
        if (annotation != null) {
            ignoreCamelFormatInAll = true;
        }
        return ignoreCamelFormatInAll;
    }

    public static final String getTableName(Class clazz) {
        TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
        if (tableName == null) {
            return clazz.getSimpleName();
        }
        return tableName.value();
    }

    /**
     * 驼峰式转下划线.
     * unchecked
     *
     * @param param
     * @return
     */
    public static String camel2underline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * TODO:包含大写字母
     *
     * @param word
     * @return
     */
    public static boolean hasUppercaseLetter(String word) {
//        for (int i = 0; i < word.length(); i++) {
//            char c = word.charAt(i);
//            if (!Character.isLowerCase(c)) {
//                return false;
//            }
//        }
        return true;
    }

    /**
     * 将bean转为Map
     *
     * @param bean
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> bean2Map(Object bean) throws ClassNotFoundException, IllegalAccessException {
        Map<String, Object> ret = new HashMap<>();
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (modifiers == 2) {
                ret.put(field.getName(), field.get(bean));
            }
        }
        Class superClazz = clazz.getSuperclass();
        fields = superClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (modifiers == 4) {
                ret.put(field.getName(), field.get(bean));
            }
        }
        return ret;
    }

    /**
     * 将Map转为对象
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (map.containsKey(fieldName)) {
                field.set(t, map.get(fieldName));
            }
        }
        return t;
    }

    private static final Class[] IGNORED_ANNOTATIONS = {Transient.class, ColumnTransient.class};

    public static final boolean ignoreField(Method method) {
        for (Class an : IGNORED_ANNOTATIONS) {
            if (method.getAnnotation(an) != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean ignoreFieldInRootObj(Class clazz, String field) {
        IgnoreFieldsInRootObj ig = (IgnoreFieldsInRootObj) clazz.getAnnotation(IgnoreFieldsInRootObj.class);
        if (ig == null) {
            return false;
        }
        String[] value = ig.value();
        for (String val : value) {
            if (val.equals(field)) {
                return true;
            }
        }
        return false;
    }
}
