package com.think.cache;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * @author borney
 * @date 3/1/17
 */

public interface CacheManager extends Cache {
    /**
     *
     * @param key
     * @param bitmap
     * @param <T>
     */
    <T extends Bitmap> void putBitmap(String key, T bitmap);

    /**
     *
     * @param key
     * @param <T>
     * @return
     */
    <T extends Bitmap> T getBitmap(String key);

    <T extends Serializable> void putSerializable(String key, T obj);

    <T extends Serializable> T getSerializable(String key);
}
