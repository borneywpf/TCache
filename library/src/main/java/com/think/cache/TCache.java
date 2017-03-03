package com.think.cache;

import android.content.Context;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by borney on 3/1/17.
 */

public class TCache implements CacheManager {
    private static final int DEFAULT_MAX_DISK_SPACE = 50 * 1024 * 1024;
    private static final int DEFAULT_MAX_DISK_FILE_COUNT = 500;
    private String cacheDir;
    private CacheManager diskCacheManager;
    private CacheManager memoryCacheManager;
    private final static Map<String, TCache> T_CACHE_MAP = new ConcurrentHashMap<>();

    private TCache() {
    }

    public static CacheManager get(Context context) {
        return get(context, "tcache");
    }

    public static CacheManager get(Context context, String relativeCacheDir) {
        return get(context, relativeCacheDir, DEFAULT_MAX_DISK_FILE_COUNT, DEFAULT_MAX_DISK_SPACE);
    }

    public static CacheManager get(Context context, String relativeCacheDir, int maxDiskTotalCount,
            int maxDiskTotalSpace) {
        return get(context, relativeCacheDir, maxDiskTotalCount, maxDiskTotalSpace,
                Integer.MAX_VALUE);
    }

    public static CacheManager get(Context context, String relativeCacheDir, int maxDiskTotalCount,
            int maxDiskTotalSpace, int defCacheAge) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        checkDirArgument(relativeCacheDir, "relativeCacheDir");
        String cacheDir = context.getCacheDir().getPath() + File.separator + relativeCacheDir;
        return getCacheManager(cacheDir, maxDiskTotalCount, maxDiskTotalSpace,
                defCacheAge);
    }

    private static CacheManager getCacheManager(String cacheDir,
            int maxDiskTotalCount, int maxDiskTotalSpace, int defCacheAge) {
        TCache cache = getTCache(cacheDir);
        cache.cacheDir = cacheDir;
        cache.diskCacheManager = new DiskCacheManager(new FileManager(),
                cache.cacheDir,
                maxDiskTotalCount,
                maxDiskTotalSpace,
                defCacheAge);
        if (cache.memoryCacheManager != null) {
            cache.evictAll();
        }
        cache.memoryCacheManager = new MemoryCacheManager();
        return cache;
    }

    private static TCache getTCache(String cacheDir) {
        TCache cache = T_CACHE_MAP.get(cacheDir);
        if (cache == null) {
            cache = new TCache();
            T_CACHE_MAP.put(cacheDir, cache);
        }
        return cache;
    }

    @Override
    public synchronized <T extends Serializable> void put(String key, T obj) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Can not operate in the main thread !!!");
        }
        String k = absoluteKey(key);
        diskCacheManager.put(k, obj);
        memoryCacheManager.put(k, obj);
    }

    @Override
    public synchronized <T extends Serializable> T get(String key) {
        String k = absoluteKey(key);
        T obj = memoryCacheManager.get(k);
        if (obj == null) {
            obj = diskCacheManager.get(k);
            memoryCacheManager.put(k, obj);
        }
        return obj;
    }

    @Override
    public synchronized <T extends Parcelable> void put(String key, T obj) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Can not operate in the main thread !!!");
        }
        String k = absoluteKey(key);
        diskCacheManager.put(k, obj);
        memoryCacheManager.put(k, obj);
    }

    @Override
    public synchronized <T extends Parcelable> T get(String key, Parcelable.Creator<T> create) {
        String k = absoluteKey(key);
        T obj = memoryCacheManager.get(k, create);
        if (obj == null) {
            obj = diskCacheManager.get(k, create);
            memoryCacheManager.put(k, obj);
        }
        return obj;
    }

    @Override
    public boolean isExpired(String key) {
        String k = absoluteKey(key);
        return diskCacheManager.isExpired(k);
    }

    @Override
    public boolean isExpired(String key, long age) {
        String k = absoluteKey(key);
        return diskCacheManager.isExpired(k, age);
    }

    @Override
    public synchronized void evict(String key) {
        String k = absoluteKey(key);
        diskCacheManager.evict(k);
        memoryCacheManager.evict(k);
    }

    @Override
    public synchronized void evictAll() {
        diskCacheManager.evictAll();
        memoryCacheManager.evictAll();
    }

    /**
     * recycle cache manager and memory cache
     */
    public void recycle() {
        Iterator<TCache> iterator = T_CACHE_MAP.values().iterator();
        while (iterator.hasNext()) {
            TCache cache = iterator.next();
            cache.memoryCacheManager.evictAll();
            iterator.remove();
        }
    }

    private String absoluteKey(String key) {
        checkDirArgument(key, "key");
        return new StringBuilder()
                .append(cacheDir)
                .append(File.separator)
                .append(key)
                .toString();
    }

    private static void checkDirArgument(String dir, String tag) {
        if (TextUtils.isEmpty(dir)) {
            throw new IllegalArgumentException(tag + " can not empty!!!");
        }

        String trim = dir.trim();
        if (!trim.equals(dir)) {
            throw new IllegalArgumentException(tag + " not start or end with blank!!");
        }

        if (trim.startsWith(File.separator) || trim.endsWith(File.separator)) {
            throw new IllegalArgumentException(
                    tag + " not start or end with " + File.separator + "!!");
        }
    }
}
