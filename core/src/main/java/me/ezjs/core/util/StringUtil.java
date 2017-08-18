package me.ezjs.core.util;

public class StringUtil {

    public static boolean assertNull(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }
        if (((String) value).trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNull(String val) {
        if (val == null || val.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String format(String str, Object... args) {
        for (Object arg : args) {
            String a = arg.toString();
            str = str.replaceFirst("\\{\\}", a);
        }
        return str;
    }

//    public static void main(String[] args) {
//        System.out.println(StringUtil.format("111{}222{}333", "--", ">>"));
//    }
}
