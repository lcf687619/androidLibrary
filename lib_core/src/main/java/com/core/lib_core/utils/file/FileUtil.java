package com.core.lib_core.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.constants.CoreConstant;
import com.core.lib_core.utils.NiceLogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-15 下午6:18:21
 * @Description: 文件工具类
 */
public class FileUtil {

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/goxueche_student/uploadPhoto/";

    public static void saveBitmap(Bitmap bm, String picName) {
        try {
            File dir = new File(SDPATH);

            dir.mkdirs();


            File f = new File(SDPATH, picName);

            if (f.exists()) {
                f.delete();
                f = new File(SDPATH, picName);
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filePath
     * @return
     * @Description: 检查目录是否存在, 不存在则创建
     */
    public static boolean checkDir(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }

    /**
     * @return
     * @Description: 判断SD卡是否存在
     */
    public static boolean isExistSD() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        if (isExistSD()) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        } else {
            return Environment.getRootDirectory().getAbsolutePath() + "/";
        }
    }

    //获取缓存文件目录
    public static String getDiskCacheDir(Context context) {
        String cachePath = "";
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            NiceLogUtil.e(e.getMessage());
        }
        return cachePath;
    }

    /**
     * @param context
     * @return
     * @Description: 获取当前应用SD卡缓存目录
     */
    public static String getSDCacheDir(Context context) {
        //api大于8的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            //目录为/mnt/sdcard/Android/data/com.mt.mtpp/cache
            return context.getExternalCacheDir().getPath();
        }
        String cacheDir = "/Android/data/" + context.getPackageName() + "/cache";
        return Environment.getExternalStorageDirectory().getPath() + cacheDir;
    }

    /**
     * @param type
     * @return
     * @Description: 获取缓存文件目录
     */
    public static String getCacheFileDir(int type) {
        if (CoreConstant.CACHE_DIR_SD == type) {
            //缓存在SD卡中
            return CoreApplication.FILE_DIR;
        } else {
            //缓存在SYSTEM文件中
            return CoreApplication.CACHE_DIR_SYSTEM;
        }
    }

    /**
     * 获取目录文件 如果目录文件不存在则创建
     *
     * @param folderPath ：禁用字符 \ | * ? < >
     * @return File 如果exists证明存在
     */
    public static File getSdcardDirectory(String folderPath) {
        File sdcardDirectory = android.os.Environment
                .getExternalStorageDirectory();// sdcard
        if (TextUtils.isEmpty(folderPath))
            return sdcardDirectory;
        if (!folderPath.startsWith("/"))
            folderPath = "/" + folderPath;
        String path = sdcardDirectory.getPath() + folderPath;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     * @Description: 复制文件
     */
    public static void copyFile(String sourceFile, String targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    /**
     * 将字节数组保到文件
     *
     * @param buffer
     * @return
     */
    public static boolean saveBytesToFile(byte[] buffer, File file) {
        return saveBytesToFile(buffer, file, false);
    }

    /**
     * 将字节数组保到文件
     *
     * @param buffer 中文可直接使用getBytes方法
     * @param file   文件所在目录不存在则写入不成功: java.io.IOException: No such file or
     *               directory
     * @param append
     * @return
     */
    public static boolean saveBytesToFile(byte[] buffer, File file,
                                          boolean append) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (file.isFile() && file.canWrite()) {
            try {
                FileOutputStream fos = new FileOutputStream(file, append);
                fos.write(buffer);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }


    /**
     * @param path 文件夹绝对路径
     * @return
     * @author caibing.zhang
     * @createdate 2013-11-14 下午8:46:55
     * @Description: 删除指定文件夹下所有文件
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                boolean b = temp.delete();
                if (b) {
                    System.out.println("删除成功");
                } else {
                    System.out.println("删除失败：" + temp);
                }
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * @param folderPath 文件夹绝对路径
     * @author caibing.zhang
     * @createdate 2013-11-14 下午8:46:10
     * @Description: 删除文件, 包括文件夹
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filename
     * @return
     * @author caibing.zhang
     * @createdate 2013-11-22 下午2:28:45
     * @Description: Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * @param path
     * @return
     * @author miaoxin.ye
     * @createdate 2014-1-23 上午11:04:52
     * @Description: 删除文件
     */
    public static boolean deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }

    /**
     * @param file
     * @return
     * @author caibing.zhang
     * @createdate 2015年3月7日 下午5:44:18
     * @Description: 获得某个文件的大小
     */
    public static double getDirSize(File file) {
        //判断文件是否存在     
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位   
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    /**
     * @param dirName 在assets下的一文件名    如datas/tt.txt
     * @param name    文件名
     * @param context
     * @return
     * @description 取得assets里面的某个文件，并且读取里面的内容
     */
    public static String getAssetContentByName(String dirName, String name, Context context) {

        try {

            String fileFullPath = "";
            if ("".equals(dirName)) {
                fileFullPath = name;
            } else {
                fileFullPath = dirName + "/" + name;
            }

            InputStream in = context.getAssets().open(fileFullPath);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            return new String(buffer);
        } catch (Exception ex) {
            NiceLogUtil.D("   not exits " + dirName + "/" + name);
        }

        return "";
    }

    public static String getStrFromSDFilePath(String path) {
        String content = "";
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
//            content = org.apache.http.util.EncodingUtils.getString(buffer, "UTF-8");
            content = new String(buffer, "UTF-8");
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
