package com.think.cache;

/**
 * Created by borney on 3/7/17.
 */

public interface ByteMapper<T> {
    byte[] getBytes(T obj);

    T getObject(byte[] bytes);
}
