package com.core.lib_core.utils.file;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-15 下午4:13:53
 * @Description: 流处理工具类
 */
public class StreamUtil {
	
	/**
	 * @Description: 从url获取输入流
	 * @param imageUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static InputStream getInputStreamFromUrl(String imageUrl){
		try {
			HttpURLConnection connection = (HttpURLConnection)new URL(imageUrl).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10 * 1000);
			return connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Description: 输入流转成BitmapDrawable
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static BitmapDrawable converToBitmapDrawable(InputStream is){
		BitmapDrawable bitmapDrawable = new BitmapDrawable(is);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmapDrawable;
	}
	
	/**
	 * @Description: 输入流转成Bitmap
	 * @param is 
	 * @return
	 * @throws IOException
	 */
	public static Bitmap convertBitmap(InputStream is){
		return converToBitmapDrawable(is).getBitmap();
	}
	
	/**
	 * @Description: 将输入流转成字节数组
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] convertToByteArray(InputStream is){
		try {
			byte[] cache = new byte[1 * 1024];
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			for (int length; (length = is.read(cache)) != -1;) {
			    buffer.write(cache, 0, length);
	        }
			is.close();
			return buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 输入流解析为String
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamToString(InputStream is){
		Closeable resource = is;
		try{
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			resource = reader; 
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}