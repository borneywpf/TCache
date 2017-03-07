package com.think.cache;

/**
 * Created by borney on 3/7/17.
 */

public interface Cache {
    /**
     * putByteMapper a object to cache
     *
     * @param key    存储对象文件的相对名称,可以是目录树
     * @param mapper 要存储的对象
     * @param <T>    要存储的对象
     */
    <T, M extends ByteMapper<T>> void putByteMapper(String key, T obj, M mapper);

    /**
     * get a object from cache
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     * @param <T> 对象
     * @return 对象
     */
    <T> T getByteMapper(String key, ByteMapper<T> mapper);

    /**
     * cache data is expired or not by key
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     */
    boolean isExpired(String key);

    /**
     * 根据 {@param age} 判断缓存是否过期
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     * @param age 过期指数
     */
    boolean isExpired(String key, long age);

    /**
     * 清除 key 对应的缓存
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     */
    void evict(String key);

    /**
     * 清除所有缓存
     */
    void evictAll();
}
