package com.think.cache;

import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.util.ArrayMap;

import java.io.Serializable;

/**
 * Created by borney on 3/1/17.
 */
@SuppressLint("NewApi")
class MemoryCacheManager implements CacheManager {
    private ArrayMap<String, Serializable> serializableMap;
    private ArrayMap<String, Parcelable> parcelableMap;

    MemoryCacheManager() {
        serializableMap = new ArrayMap<>();
        parcelableMap = new ArrayMap<>();
    }

    @Override
    public <T extends Serializable> void put(String key, T obj) {
        serializableMap.put(key, obj);
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return (T) serializableMap.get(key);
    }

    @Override
    public <T extends Parcelable> void put(String key, T obj) {
        parcelableMap.put(key, obj);
    }

    @Override
    public <T extends Parcelable> T get(String key, Parcelable.Creator<T> create) {
        return (T) parcelableMap.get(key);
    }

    @Override
    public boolean isExpired(String key) {
        throw new UnsupportedOperationException("not support isExpired(key, age) operation!!!");
    }

    @Override
    public boolean isExpired(String key, long age) {
        throw new UnsupportedOperationException("not support isExpired(key, age) operation!!!");
    }

    @Override
    public void evict(String key) {
        if (serializableMap.containsKey(key)) {
            serializableMap.remove(key);
        }
        if (parcelableMap.containsKey(key)) {
            parcelableMap.remove(key);
        }
    }

    @Override
    public void evictAll() {
        serializableMap.clear();
        parcelableMap.clear();
    }
}
