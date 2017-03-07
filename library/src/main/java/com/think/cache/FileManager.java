package com.think.cache;

import android.annotation.SuppressLint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by borney on 2/27/17.
 */
@SuppressLint("NewApi")
@SuppressWarnings("unused")
class FileManager {

    FileManager() {

    }

    /**
     * write byte array to file
     */
    public void writeBytes(File file, byte[] content) {
        checkNotNull(file);
        checkNotNull(content);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel channel = fos.getChannel();
            buffer.put(content);
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return byte array from file
     *
     * @param file which provide content
     * @return byte array of file content
     */
    public byte[] readBytes(File file) {
        checkNotNull(file);
        if (!file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            FileChannel fisChannel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int j = 0;
            byte[] result = new byte[fis.available()];
            while (fisChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    result[j++] = buffer.get();
                }
                buffer.clear();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * write Serializable {@link Serializable} object to file
     */
    public <T extends Serializable> void writeSerializable(File file, T object) {
        checkNotNull(object);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            out.flush();
            writeBytes(file, bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return a Serializable {@link Serializable} object by file content
     */
    public <T extends Serializable> T readSerializable(File file) throws ClassCastException {
        byte[] bytes = readBytes(file);
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try (ObjectInput in = new ObjectInputStream(bis)) {
            return (T) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleFile(File file) {
        checkNotNull(file);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleFile(f);
            }

        }
        file.delete();
    }

    private Object checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException("error:object is null");
        }
        return object;
    }

    static class SerializableWrapper<T> implements Serializable {
        private T obj;

        public T getObj() {
            return obj;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }
    }
}
