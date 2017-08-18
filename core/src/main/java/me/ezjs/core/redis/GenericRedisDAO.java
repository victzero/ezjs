package me.ezjs.core.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * redis数据获取窗口
 * 1. 17/5/15 1:28  创建文件,初步完成value为String类型的封装;确定从数据源获取并更新的设计方案
 * Created by zero-mac on 17/5/15.
 */
public abstract class GenericRedisDAO<ID, T> {

    protected final Log log = LogFactory.getLog(getClass());

    protected RedisTemplate redisTemplate;
    protected Class persistentClass;

    protected static final String KEY_SEPARATOR = "_";

    public GenericRedisDAO(RedisTemplate redisTemplate, Class clazz) {
        this.redisTemplate = redisTemplate;
        this.persistentClass = clazz;
    }

    /**
     * 根据包名+类名组成key的前缀
     *
     * @param clazz
     * @return
     */
    protected String generateKeyPrefix(Class clazz) {
//        return clazz.getName();
        return clazz.getSimpleName();
    }

    /**
     * 根据key获取value,如果redis中不存在,则尝试从源头(DB等)获取并更新并返回
     *
     * @param key
     * @return 如果源头仍未查到, 则为null
     */
    public T getOrUpdate(String key) {
        return getOrUpdateFromRedis(key);
    }

    public T getOrUpdateById(ID srcKey) {
        String key = getKey(srcKey, persistentClass);
        return getOrUpdateFromRedis(key);
    }

    /**
     * 根据key直接获取value(String)
     *
     * @param key
     * @return 如果为空, 则为null
     */
    public T get(String key) {
        return getFromRedis(key);
    }

    protected T getFromRedis(String key) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    protected T getOrUpdateFromRedis(String key) {
        T fromRedis = getFromRedis(key);
        if (fromRedis != null) {
            log.debug("get data from redis. key: " + key + ", value: " + fromRedis);
            return fromRedis;
        }
        return update2RedisFromSrc(key);
    }

    protected T update2RedisFromSrc(String key) {
        T fromSrc = getFromSrc(key);
        if (fromSrc == null) {
            return null;
        }
        set(key, fromSrc);
        return fromSrc;
    }


    protected void set(String key, T value) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    /**
     * 删除
     *
     * @param key
     */
    protected void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 从源头(DB等)获取数据
     *
     * @param key
     * @return
     */
    protected abstract T getFromSrc(String key);

    /**
     * 根据key获取原始key值(去掉前缀)
     *
     * @param key
     * @return
     */
    protected String getSrcKey(String key) {
        int i = key.lastIndexOf(KEY_SEPARATOR) + 1;
        String src_key = key.substring(i, key.length());
        return src_key;
    }

    /**
     * 通过srcKey(一般使用主键)和Class名称组合形成新的key
     *
     * @param srcKey
     * @param clazz
     * @return
     */
    protected String getKey(ID srcKey, Class clazz) {
//        return generateKeyPrefix(clazz) + KEY_SEPARATOR + srcKey;
        return KEY_SEPARATOR + srcKey;
    }
}
