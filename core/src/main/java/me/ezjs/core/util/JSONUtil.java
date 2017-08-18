package me.ezjs.core.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONString;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSONUtil {

    private static final boolean IS_DEVELOPING = true;

    private static final class Null {

        @Override
        protected final Object clone() {
            return this;
        }

        @Override
        public boolean equals(Object object) {
            return object == null || object == this;
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    private final Map<Object, Object> map;

    public static final Object NULL = new Null();

    /**
     * Construct an empty JSONObject.
     */
    public JSONUtil() {
        this.map = new HashMap<Object, Object>();
    }

    public JSONUtil(Object bean) {
        this();
        this.populateMap(bean);
    }

    private void populateMap(Object bean) {
        Class<? extends Object> klass = bean.getClass();

        // If klass is a System class then set includeSuperClass to false.

        boolean includeSuperClass = klass.getClassLoader() != null;

        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i += 1) {
            try {
                Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {

                    String key = BeanUtil.getFieldByMethod(method);
                    if (key.length() == 0) {
                        continue;
                    }
                    Object result = method.invoke(bean, (Object[]) null);
                    if (result != null) {
                        Object obj = wrap(result);
                        if (IS_DEVELOPING && obj instanceof String && obj.equals("_$NSIAC$_")) {
                            this.map.put(key, wrap(result));
                        }
                        if (!(obj instanceof String && obj.equals("_$NSIAC$_"))) {
                            this.map.put(key, wrap(result));
                        }
                    }
                }
            } catch (Exception ignore) {
            }
        }
    }

    public static Object wrap(Object object) {
        try {
            if (object == null) {
                return NULL;
            }
            if (object instanceof JSONUtil || object instanceof JSONArray || NULL.equals(object)
                    || object instanceof JSONString || object instanceof Byte || object instanceof Character
                    || object instanceof Short || object instanceof Integer || object instanceof Long
                    || object instanceof Boolean || object instanceof Float || object instanceof Double
                    || object instanceof String) {
                return object;
            }

            if (object instanceof Collection) {
                return "_$NSIAC$_";// not supported in asd common
            }
            if (object.getClass().isArray()) {
                return "_$NSIAC$_";// not supported in asd common
            }
            if (object instanceof Map) {
                return "_$NSIAC$_";// not supported in asd common
            }
            if (object instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.format(object);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.")
                    || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return "_$NSIAC$_";// not supported in asd common
        } catch (Exception exception) {
            return null;
        }
    }

    // public static void main(String[] args) {
    // Person person = new Person();
    // person.setChineseName("asdf");
    // List<Clue> clues = new LinkedList<Clue>();
    // Clue clue = new Clue();
    // clue.setChineseName("asdf");
    // clues.add(clue);
    // person.setClues(clues);
    // person.setBirthday(new Date());
    // JSONUtil js = new JSONUtil(person);
    // System.out.println(js.toString());
    // }

    @Override
    public String toString() {
        try {
            return this.toString(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p/>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of indentation.
     * @return a printable, displayable, portable, transmittable representation
     * of the object, beginning with <code>{</code>&nbsp;<small>(left
     * brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     * brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    public String toString(int indentFactor) throws JSONException {
        StringWriter w = new StringWriter();
        synchronized (w.getBuffer()) {
            return this.write(w, indentFactor, 0).toString();
        }
    }

    public int length() {
        return this.map.size();
    }

    public Iterator<Object> keys() {
        return this.keySet().iterator();
    }

    public Set<Object> keySet() {
        return this.map.keySet();
    }

    public static String quote(String string) {
        StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            try {
                return quote(string, sw).toString();
            } catch (IOException ignored) {
                // will never happen - we are writing to a string writer
                return "";
            }
        }
    }

    public static Writer quote(String string, Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
            return w;
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        int len = string.length();

        w.write('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    w.write('\\');
                    w.write(c);
                    break;
                case '/':
                    if (b == '<') {
                        w.write('\\');
                    }
                    w.write(c);
                    break;
                case '\b':
                    w.write("\\b");
                    break;
                case '\t':
                    w.write("\\t");
                    break;
                case '\n':
                    w.write("\\n");
                    break;
                case '\f':
                    w.write("\\f");
                    break;
                case '\r':
                    w.write("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
                        w.write("\\u");
                        hhhh = Integer.toHexString(c);
                        w.write("0000", 0, 4 - hhhh.length());
                        w.write(hhhh);
                    } else {
                        w.write(c);
                    }
            }
        }
        w.write('"');
        return w;
    }

    public static String numberToString(Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(number);

        // Shave off trailing zeros and decimal point, if possible.

        String string = number.toString();
        if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string.indexOf('E') < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }

    public static void testValidity(Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            } else if (o instanceof Float) {
                if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
        }
    }

    static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent)
            throws JSONException, IOException {
        if (value == null || value.equals(null)) {
            writer.write("null");
        } else if (value instanceof JSONUtil) {
            ((JSONUtil) value).write(writer, indentFactor, indent);
        } else if (value instanceof Number) {
            writer.write(numberToString((Number) value));
        } else if (value instanceof Boolean) {
            writer.write(value.toString());
        } else if (value instanceof JSONString) {
            Object o;
            try {
                o = ((JSONString) value).toJSONString();
            } catch (Exception e) {
                throw new JSONException(e);
            }
            writer.write(o != null ? o.toString() : quote(value.toString()));
        } else {
            quote(value.toString(), writer);
        }
        return writer;
    }

    static final void indent(Writer writer, int indent) throws IOException {
        for (int i = 0; i < indent; i += 1) {
            writer.write(' ');
        }
    }

    Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean commanate = false;
            final int length = this.length();
            Iterator<Object> keys = this.keys();
            writer.write('{');

            if (length == 1) {
                Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(':');
                if (indentFactor > 0) {
                    writer.write(' ');
                }
                writeValue(writer, this.map.get(key), indentFactor, indent);
            } else if (length != 0) {
                final int newindent = indent + indentFactor;
                while (keys.hasNext()) {
                    Object key = keys.next();
                    if (commanate) {
                        writer.write(',');
                    }
                    if (indentFactor > 0) {
                        writer.write('\n');
                    }
                    indent(writer, newindent);
                    writer.write(quote(key.toString()));
                    writer.write(':');
                    if (indentFactor > 0) {
                        writer.write(' ');
                    }
                    writeValue(writer, this.map.get(key), indentFactor, newindent);
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.write('\n');
                }
                indent(writer, indent);
            }
            writer.write('}');
            return writer;
        } catch (IOException exception) {
            throw new JSONException(exception);
        }
    }
}
