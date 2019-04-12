package com.core.lib_core.utils.file;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.utils.MD5Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-16 下午2:12:14
 * @Description: 文件缓存工具类
 */
public class FileCacheUtil {
	
	/**
	 * @Description: 从SD卡缓存中加载String数据,默认缓存1小时
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String loadString(String url)throws Exception {
		File file = new File(CoreApplication.FILE_DIR + MD5Util.md5(url));
		long expiredTime = 600000;
		if (file.exists() && System.currentTimeMillis() - file.lastModified() < expiredTime) {
			InputStream is = new FileInputStream(file);
			return StreamUtil.InputStreamToString(is);
		}
		return null;
	}

	/**
	 * @Description: 将String数据写入SD卡缓存
	 * @param url
	 * @param str
	 */
	public static void saveString(String url, String str) {
		if(!FileUtil.isExistSD()){
			return;
		}
		File file = new File(CoreApplication.FILE_DIR + MD5Util.md5(url));
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(str.getBytes("UTF-8"));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 获取序列化数据
	 * @param type  缓存文件在SD卡中,还是在SYSTEM中
	 * @param fileName
	 * @return
	 */
	public static Object getSerializableData(int type, String fileName) {
		String dir = FileUtil.getCacheFileDir(type);
		Object obj = null;
		try {
			FileUtil.checkDir(dir);
			File file = new File(dir +MD5Util.md5(fileName));
			if (!(file.exists() && file.length()>0)){
				return null;
			}
			InputStream is = new FileInputStream(file);
			ObjectInputStream out = new ObjectInputStream(is);
			obj=out.readObject();
			out.close();
			is.close();
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
	 * @Description: 获取序列化数据
	 * @param type 缓存文件在SD卡中,还是在SYSTEM中
	 * @param fileName
	 * @param time 缓存文件有效时间
	 * @return
	 */
	public static Object getSerializableData(int type, String fileName, long time) {
		String dir = FileUtil.getCacheFileDir(type);
		Object obj = null;
		try {
			FileUtil.checkDir(dir);
			File file = new File(dir +MD5Util.md5(fileName));
			if (!(file.exists() && file.length()>0)){
				return null;
			}
			long lastTime = file.lastModified();
			long nowTime= System.currentTimeMillis();
			if(nowTime-lastTime>time){
				return null;
			}
			InputStream is = new FileInputStream(file);
			ObjectInputStream out = new ObjectInputStream(is);
			obj=out.readObject();
			out.close();
			is.close();
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
	 * @Description: 序列化数据
	 * @param type 缓存文件在SD卡中,还是在SYSTEM中
	 * @param obj
	 * @param fileName
	 */
	public static void setSerializableData(int type, Object obj, String fileName) {
		String dir = FileUtil.getCacheFileDir(type);
		File file = new File(dir + MD5Util.md5(fileName));
		try {
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(out);
			objectOut.writeObject(obj);
			objectOut.flush();
			objectOut.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 删除序列化数据
	 * @param type 缓存文件在SD卡中,还是在SYSTEM中
	 * @param fileName
	 */
	public static void delSerializableData(int type, String fileName){
		String dir = FileUtil.getCacheFileDir(type);
		FileUtil.checkDir(dir);
		File file = new File(dir + MD5Util.md5(fileName));
		if (file.exists()){
			file.delete();
		}
	}
	
}
