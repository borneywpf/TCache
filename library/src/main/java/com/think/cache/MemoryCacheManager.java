package com.think.cache;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by borney on 3/1/17.
 */

class MemoryCacheManager implements CacheManager {
    @Override
    public <T extends Serializable> void put(String key, T obj) {

    }

    @Override
    public <T extends Serializable> T get(String key) {
        return null;
    }

    @Override
    public <T extends Parcelable> void put(String key, T obj) {

    }

    @Override
    public <T extends Parcelable> T get(String key, Parcelable.Creator<T> create) {
        return null;
    }

    @Override
    public boolean isExpired(String key) {
        return false;
    }

    @Override
    public boolean isExpired(String key, long age) {
        return false;
    }

    @Override
    public void evict(String key) {

    }

    @Override
    public void evictAll() {

    }
}
