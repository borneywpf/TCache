package com.think.cache;

/**
 * Created by borney on 3/8/17.
 */

public class BytesMapper implements ByteMapper<byte[]> {

    BytesMapper() {

    }

    @Override
    public byte[] getBytes(byte[] obj) {
        return obj;
    }

    @Override
    public byte[] getObject(byte[] bytes) {
        return bytes;
    }
}
