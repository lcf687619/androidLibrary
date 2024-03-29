package com.core.lib_core.utils.file;

import android.text.TextUtils;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.constants.CoreConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


/**
 * 缓存
 *
 * @author caibing.zhang
 */
public class FileLocalCache {

    /**
     * 检查文件是否存在
     *
     * @return
     */
    public static boolean checkDir(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }

    /**
     * 返回md5后的值
     *
     * @param url
     * @return
     */
    public static String md5(String url) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String t = Integer.toHexString(0xFF & messageDigest[i]);
                if (t.length() == 1) {
                    hexString.append("0" + t);
                } else {
                    hexString.append(t);
                }
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清除SD卡中的数据缓存
     */
/*	public static void clearCache1() {
		File f = new File(FancusApplication.CACHE_DIR);
		if (f.exists() && f.isDirectory()) {
			File flist[] = f.listFiles();
			for (int i = 0; flist != null && i < flist.length; i++) {
				flist[i].delete();
			}
		}
	}*/

    /**
     * 缓存文件大小
     *
     * @return
     */
    public static String getCacheSize() {
        long size = 0;
        File f = new File(CoreApplication.CACHE_DIR_SD);
        if (f.exists() && f.isDirectory()) {
            File flist[] = f.listFiles();
            for (int i = 0; flist != null && i < flist.length; i++) {
                size = size + flist[i].length();
            }

        }
        if (size < 1000) {
            return "0KB";
        } else if (size < 1000000) {
            return size / 1000 + "KB";
        } else {
            return size / 1000000 + "M";
        }
    }

    /**
     * 从缓存中读取缓存数据，条件是在一小时之内有效
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String load2(String url) throws Exception {
        String md5 = md5(url);
        File f = new File(CoreApplication.CACHE_DIR_SD + md5);
        long expiredTime = 600000;
        //数据在10分钟有效内
        if (f.exists() && System.currentTimeMillis() - f.lastModified() < expiredTime) {
            FileInputStream fstream = new FileInputStream(f);
            long length = f.length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length &&
                    (numRead = fstream.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            fstream.close();
            return new String(bytes, "UTF-8");
        }
        return null;
    }

    /**
     * 将读取的String写入缓存中
     *
     * @param url
     * @param c
     */
    public static void store(String url, String c) {
        if (CoreApplication.IS_EXIST_SDCARD) {
            String md5 = md5(url);
            File f = new File(CoreApplication.CACHE_DIR_SD + md5);
            try {
                FileOutputStream out;
                out = new FileOutputStream(f);
                out.write(c.getBytes("UTF-8"));
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输入流
     *
     * @param in
     */
    public static void closeInputStream(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输入流
     *
     * @param in BufferedReader
     */
    public static void closeBufferedReader(BufferedReader in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     *
     * @param out
     */
    public static void closeOutputStream(OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络返回的数据保存(追加文件)，方便调试，发版时应该关闭，
     *
     * @param url
     * @param message
     * @throws IOException
     */
    public static void saveFile(String url, String message) {
        if (!CoreConstant.IS_TEST_FLAG) {  //不保存加载数据
            return;
        }
        if (CoreApplication.IS_EXIST_SDCARD && !TextUtils.isEmpty(message)) {
            try {
                //替换写入
                File file = new File(CoreApplication.LOG);
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);
                out.write(message.getBytes());
                out.flush();
                out.close();

                //追加写文件
                File allFile = new File(CoreApplication.AllLOG);
                if (!allFile.exists()) {
                    allFile.createNewFile();
                }
                //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                FileWriter writer = new FileWriter(allFile, true);
                String meg = url + message;
                writer.write(meg);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取得序列化数据
     *
     * @param type     缓存文件在SD卡中，还是在SYSTEM中,Constants.CACHE_DIR_SD,Constants.CACHE_DIR_SYSTEM
     * @param fileName
     * @return
     */
    public static Object getSerializableData(int type, String fileName) {
        String dir;
        if (CoreConstant.CACHE_DIR_SD == type) {  //缓存在SD卡中
            dir = CoreApplication.CACHE_DIR_SD;
        } else {  //缓存在SYSTEM文件中
            dir = CoreApplication.CACHE_DIR_SYSTEM;
        }
        Object obj = null;
        try {
            File d = new File(dir);
            if (!d.exists()) {
                d.mkdirs();
            }
            File f = new File(dir + md5(fileName));
            if (!f.exists()) {
                f.createNewFile();
            }
            if (f.length() == 0) {
                return null;
            }
            FileInputStream byteOut = new FileInputStream(f);
            ObjectInputStream out = new ObjectInputStream(byteOut);
            obj = out.readObject();
            out.close();
            closeInputStream(byteOut);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 取得序列化数据
     *
     * @param type     缓存文件在SD卡中，还是在SYSTEM中Constants.CACHE_DIR_SD,Constants.CACHE_DIR_SYSTEM
     * @param fileName
     * @param time
     * @return
     */
    public static Object getSerializableData(int type, String fileName, long time) {
        String dir;
        if (CoreConstant.CACHE_DIR_SD == type) {  //缓存在SD卡中
            dir = CoreApplication.CACHE_DIR_SD;
        } else {  //缓存在SYSTEM文件中
            dir = CoreApplication.CACHE_DIR_SYSTEM;
        }
        Object obj = null;
        try {
            File d = new File(dir);
            if (!d.exists()) {
                d.mkdirs();
            }
            File f = new File(dir + md5(fileName));
            if (!f.exists()) {
                f.createNewFile();
            }
            if (f.length() == 0) {
                return null;
            }
            long lastTime = f.lastModified();
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime > time) {
                return null;
            }
            FileInputStream byteOut = new FileInputStream(f);
            ObjectInputStream out = new ObjectInputStream(byteOut);
            obj = out.readObject();
            out.close();
            closeInputStream(byteOut);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 进行序列化
     *
     * @param type     缓存文件在SD卡中，还是在SYSTEM中Constants.CACHE_DIR_SD,Constants.CACHE_DIR_SYSTEM
     * @param obj
     * @param fileName
     */
    public static void setSerializableData(int type, Object obj, String fileName) {
        String dir;
        if (CoreConstant.CACHE_DIR_SD == type) {  //缓存在SD卡中
            dir = CoreApplication.CACHE_DIR_SD;
        } else {  //缓存在SYSTEM文件中
            dir = CoreApplication.CACHE_DIR_SYSTEM;
        }
        try {
            FileOutputStream bytetOut = new FileOutputStream(new File(dir + md5(fileName)));
            ObjectOutputStream outer = new ObjectOutputStream(bytetOut);
            outer.writeObject(obj);
            outer.flush();
            outer.close();
            bytetOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type
     * @param fileName
     * @author miaoxin.ye
     * @createdate 2012-10-11 上午10:05:29
     * @Description: 删除序列化数据
     */
    public static void delSerializableData(int type, String fileName) {
        String dir;
        if (CoreConstant.CACHE_DIR_SD == type) {  //缓存在SD卡中
            dir = CoreApplication.CACHE_DIR_SD;
        } else {  //缓存在SYSTEM文件中
            dir = CoreApplication.CACHE_DIR_SYSTEM;
        }
        File d = new File(dir);
        if (!d.exists()) {
            d.mkdirs();
        }
        File f = new File(dir + md5(fileName));
        if (f.exists()) {
            f.delete();
        }
    }
}
