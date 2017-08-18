package me.ezjs.core.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lfq on 2016/9/8.
 */
public class ThreadCache {

    private static ThreadLocal<Map> YNCache = new ThreadLocal();

    public static Object getData (String key) {
        Map map = YNCache.get();
        if(map == null){
            map = new HashMap();
            YNCache.set(map);
        }
        return map.get(key);
    }

    public static void putData (String key, Object value) {
        Map map = YNCache.get();
        if(map == null){
            map = new HashMap();
        }
        map.put(key, value);
        YNCache.set(map);
    }

    public static void remove () {
        YNCache.remove();
    }

}
