package com.think.cache;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author borney
 * @date 3/1/17
 */

public interface CacheManager {
    /**
     * put a Serializable {@link Serializable} object to cache
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     * @param obj 要存储的对象
     * @param <T> Serializable类型的序列化对象
     */
    <T extends Serializable> void put(String key, T obj);

    /**
     * get a Serializable {@link Serializable} object from cache
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     * @param <T> Serializable类型的序列化对象
     * @return Serializable 对象
     */
    <T extends Serializable> T get(String key);


    /**
     * put a Parcelable {@link Parcelable} object to cache
     *
     * @param key 存储对象文件的相对名称,可以是目录树
     * @param obj 要存储的对象
     * @param <T> Parcelable类型的序列化对象
     */
    <T extends Parcelable> void put(String key, T obj);


    /**
     * get a Parcelable {@link Parcelable} object from cache
     *
     * @param key    存储对象文件的相对名称,可以是目录树
     * @param create Parcelable类型的序列化对象的 Creator {@link android.os.Parcelable.Creator}
     * @param <T>    Parcelable类型的序列化对象
     */
    <T extends Parcelable> T get(String key, Parcelable.Creator<T> create);

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
