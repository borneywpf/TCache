package com.think.cache;

import android.annotation.SuppressLint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by borney on 3/1/17.
 */
@SuppressLint("NewApi")
class DiskCacheManager implements CacheManager {
    private FileManager fileManager;
    private String cacheDir;
    private int maxCount;
    private int maxSpace;
    private long age;

    DiskCacheManager(FileManager fileManager, String cacheDir, int maxCount, int maxSpace,
            long age) {
        this.fileManager = fileManager;
        this.cacheDir = cacheDir;
        this.maxCount = maxCount;
        this.maxSpace = maxSpace;
        this.age = age;
    }

    @Override
    public <T> void put(String key, T obj) {
        //create wrapper serializable
        FileManager.SerializableWrapper<T> wrapper = new FileManager.SerializableWrapper<>();
        wrapper.setObj(obj);

        //ensure total file count and space
        ensureTotalCount();
        ensureTotalSpace(wrapper);

        //create cache file
        File file = buildFile(key);
        createNotExistsParent(file);

        //write data to cache file
        fileManager.writeSerializable(file, wrapper);

        //update file and it's parent list modify time
        updateLastModified(file, System.currentTimeMillis());
    }

    @Override
    public <T> T get(String key) {
        File file = buildFile(key);

        FileManager.SerializableWrapper<T> wrapper = fileManager.readSerializable(file);
        return wrapper.getObj();
    }

    @Override
    public boolean isExpired(String key) {
        return isExpired(key, age);
    }

    @Override
    public boolean isExpired(String key, long age) {
        File file = buildFile(key);
        return System.currentTimeMillis() - file.lastModified() > age;
    }

    @Override
    public void evict(String key) {
        File file = buildFile(key);
        fileManager.deleFile(file);
    }

    @Override
    public void evictAll() {
        fileManager.deleFile(new File(cacheDir));
    }

    private void ensureTotalSpace(Serializable o) {
        int objSize = serializableSize(o);
        File file = new File(cacheDir);
        long cacheSize = calFileSize(file);
        while (cacheSize + objSize > maxSpace) {
            long removeSize = removeLastModifiedFile(file);
            cacheSize -= removeSize;
        }
    }

    private void ensureTotalCount() {
        File file = new File(cacheDir);
        int size = calFileCount(file);
        while (size > maxCount) {
            removeLastModifiedFile(file);
            size--;
        }
    }

    private File buildFile(String key) {
        return new File(key);
    }

    private void createNotExistsParent(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    private void updateLastModified(File file, long time) {
        if (file.getPath().equals(cacheDir)) {
            return;
        }
        file.setLastModified(time);
        updateLastModified(file.getParentFile(), time);
    }

    private long calFileSize(File file) {
        return file.length();
    }

    private int calFileCount(File file) {
        return allFiles(file).size();
    }

    private List<File> allFiles(File file) {
        ArrayList<File> files = new ArrayList<>();
        allFiles(file, files);
        return files;
    }

    private void allFiles(File file, List<File> files) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
                allFiles(f, files);
            }
        } else {
            files.add(file);
        }
    }

    private long removeLastModifiedFile(File parent) {
        List<File> files = allFiles(parent);
        if (!files.isEmpty()) {
            File lastModifyFile = files.get(0);
            long removedSize = lastModifyFile.length();
            for (File f : files) {
                if (f.lastModified() < lastModifyFile.lastModified()) {
                    lastModifyFile = f;
                    removedSize = lastModifyFile.length();
                }
            }
            lastModifyFile.delete();
            return removedSize;
        }
        return 0;
    }

    private int serializableSize(Serializable obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
