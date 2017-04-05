package com.think.cache;

/**
 * Created by borney on 3/7/17.
 */

interface Cache {
    /**
     * putByteMapper a object to cache
     *
     * @param key    The relative name of the storage object file can be a directory tree
     * @param mapper Store the object of the converter
     * @param <T>    The object to be stored
     */
    <T> void putByteMapper(String key, T obj, ByteMapper<T> mapper);

    /**
     * get a object from cache
     *
     * @param key The relative name of the storage object file can be a directory tree
     * @param <T> Store the object of the converter
     * @return Stored object
     */
    <T> T getByteMapper(String key, ByteMapper<T> mapper);

    /**
     * cache data is expired or not by key
     *
     * @param key The relative name of the storage object file can be a directory tree
     */
    boolean isExpired(String key);

    /**
     * According to {@param age} to determine whether the cache expired
     *
     * @param key The relative name of the storage object file can be a directory tree
     * @param age Expiration index
     */
    boolean isExpired(String key, long age);

    /**
     * Clear the cache corresponding to key
     *
     * @param key The relative name of the storage object file can be a directory tree
     */
    void evict(String key);

    /**
     * Clear all caches
     */
    void evictAll();

    /**
     * Whether the cache corresponding to the data
     *
     * @param key
     * @return
     */
    boolean isCached(String key);
}
