package com.eme.mas.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by luokaiwen on 15/8/26.
 * <p/>
 * SDCard帮助类
 */
public class SDCardUtil {

    /**
     * 判断sdcard是否可用
     *
     * @return true：可用 false：不可用
     */
    public static boolean isEnable() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return isMounted() && SDCardUtil.getSDFreeSize() > 1;
    }

    /**
     * 是否挂载了sdcard
     *
     * @return true：是 false：没有
     */
    public static boolean isMounted() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }

    /**
     * 获取sdcard根路径
     *
     * @return sdcard根路径
     */
    public static String getSDCardPath() {

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 根据路径创建文件夹
     */
    public static void createFile(String path) {

        try {
            File file = new File(path);

            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param fileDir File路径
     * @return long
     */
    public static long getFolderSize(String fileDir) {

        long size = 0;
        try {

            File file = new File(fileDir);

            if (file.exists() && file.isDirectory()) {// 处理目录

                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i].getAbsolutePath());

                    } else {
                        size = size + fileList[i].length();

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //back size/1048576;
        return size / 1024;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {

        File source = new File(filePath);

        if (!source.exists()) {

            // 如果文件或者文件夹不存在什么也不做
            return;
        }

        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {

        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //back freeBlocks * blockSize;  //单位Byte
        //back (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * SD卡总容量
     *
     * @return
     */
    public static long getSDAllSize() {

        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //back allBlocks * blockSize; //单位Byte
        //back (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }
}
