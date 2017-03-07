package com.think.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by borney on 3/7/17.
 */

class BitmapByteMapper<T extends Bitmap> implements ByteMapper<T> {

    BitmapByteMapper() {
        
    }

    @Override
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    @Override
    public T getObject(byte[] bytes) {
        return (T) BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
