package com.think.cache;

import android.annotation.SuppressLint;
import android.util.ArrayMap;

/**
 * Created by borney on 3/1/17.
 */
@SuppressLint("NewApi")
class MemoryCacheManager implements CacheManager {
    private ArrayMap<String, Object> objectMap;

    MemoryCacheManager() {
        objectMap = new ArrayMap<>();
    }

    @Override
    public <T> void put(String key, T obj) {
        objectMap.put(key, obj);
    }

    @Override
    public <T> T get(String key) {
        return (T) objectMap.get(key);
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
        if (objectMap.containsKey(key)) {
            objectMap.remove(key);
        }
    }

    @Override
    public void evictAll() {
        objectMap.clear();
    }
}
