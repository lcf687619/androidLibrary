package com.core.lib_core.http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.text.TextUtils;

import com.core.lib_core.activity.HandlerMgr;
import com.core.lib_core.http.okhttp.OkHttpUtils;
import com.core.lib_core.http.okhttp.builder.PostFormBuilder;
import com.core.lib_core.http.okhttp.callback.Callback;
import com.core.lib_core.utils.NiceLogUtil;
import com.core.lib_core.utils.file.FileLocalCache;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.core.lib_core.http.HttpLoader.STATE_FAILED;

/**
 * 通用的http工具类<br>
 * 每次网络请求由一个单独的HttpClient对象处理<br>
 * 每个HttpClient不管是get还是post都关联一个HttpReq或者HttpPost对象
 *
 * @author coffee
 */
public class HttpClient {
    private String charset = "UTF-8";

    // 检查网络类型
    public synchronized static boolean checkNetworkStatus(final Activity context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connManager.getActiveNetworkInfo();
        // 网络状态
        boolean netSataus = false;
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null) {
            netSataus = info.isAvailable();
        }
        return netSataus;
    }

    private String errorString(String errorStr) {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("code:'").append(0).append("',").append("success:'").append("false")
                .append("',").append("msg:'").append(errorStr).append("',").append("data:'").append("")
                .append("'}");
        return builder.toString();
    }

    private void notifyMessage(HttpReq req, String result) {
        //
        Message msg = Message.obtain();
        msg.what = req.getMsgId();
        msg.obj = result;
        msg.arg1 = req.getArg1();
        msg.arg2 = req.getArg2();
        // Looper.prepare();
        HandlerMgr.sendMessage(msg, 0);
    }

    private class OkHttpCallBack extends Callback<Object> {
        private HttpReq httpReq;

        public OkHttpCallBack(HttpReq httpReq) {
            this.httpReq = httpReq;
        }

        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            NiceLogUtil.D("NiceOkHttpCallBack parseNetworkResponse   = " + response.body().toString());
            return response.body().string();
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(httpReq.getUrl());
            stringBuffer.append("?");
            for (Map.Entry<String, String> entry : httpReq.getParams().entrySet()) {
                stringBuffer.append(entry.getKey());
                stringBuffer.append("=");
                stringBuffer.append(entry.getValue());
                stringBuffer.append("&");
            }
            NiceLogUtil.response(httpReq.getUrl(), stringBuffer.toString(), e.getMessage());
            boolean success = HttpLoader.textReq.remove(httpReq);
            if (success == false) {// 从缓存中删除失败
                httpReq.setState(STATE_FAILED);
            }
            String message = "";
            if (e instanceof UnknownHostException) {
                message = "网络连接异常,请检查网络设置";
                NiceLogUtil.e("网络连接异常,请检查网络设置");
            } else if (e instanceof IOException) {
                message = "服务器连接异常,请稍后再试";
                NiceLogUtil.e("服务器连接异常,请稍后再试");
            } else {
                message = "未知错误,请稍后再试";
                NiceLogUtil.e("未知错误,请稍后再试");
            }
            notifyMessage(httpReq, errorString(message));
            if (httpReq.getHttpCallback() != null) {
                httpReq.getHttpCallback().callback(errorString(message), httpReq);
            }
        }

        @Override
        public void onResponse(Object objResult, int id) {
            // 先移除请求,
            // 因为在notifyMessage的时候Activity#handleMessage中处理dialog以后。线程会卡主。
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(httpReq.getUrl());
            stringBuffer.append("?");
            for (Map.Entry<String, String> entry : httpReq.getParams().entrySet()) {
                stringBuffer.append(entry.getKey());
                stringBuffer.append("=");
                stringBuffer.append(entry.getValue());
                stringBuffer.append("&");
            }
            stringBuffer.append("debug=1");
            NiceLogUtil.response(httpReq.getUrl(), stringBuffer.toString(), objResult.toString());
            FileLocalCache.saveFile(httpReq.getUrl(), objResult.toString());
            boolean success = HttpLoader.textReq.remove(httpReq);
            if (success == false) {// 从缓存中删除失败
                httpReq.setState(STATE_FAILED);
            }
            // http请求超时
            if (Integer.valueOf(MsgID.http_time_out).equals(objResult)) {
                // 通知界面请求超时
                HandlerMgr.sendMessage(httpReq.getMsgId(), MsgID.http_time_out, httpReq.getMsgId());
            } else {
                String result = objResult + "";
                notifyMessage(httpReq, result);
            }
            if (httpReq.getHttpCallback() != null) {
                httpReq.getHttpCallback().callback(objResult, httpReq);
            }
        }
    }

    /**
     * okhttp3的post方式提交数据
     */
    public void post(HttpReq httpReq) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();
        OkHttpUtils.initClient(okHttpClient)
                .post()
                .url(httpReq.getUrl())
                .params(httpReq.getParams())
                .tag(httpReq.getTag())
                .build()
                .execute(new OkHttpCallBack(httpReq));
    }

    /**
     * @throws IOException java.net.UnknownHostException: Host is unresolved: not-a-legal-address:80
     */
    public void get(HttpReq httpReq) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();
        OkHttpUtils.initClient(okHttpClient)
                .get()
                .url(httpReq.getUrl())
                .params(httpReq.getParams())
                .tag(httpReq.getTag())
                .build()
                .execute(new OkHttpCallBack(httpReq));
    }

    /**
     * 上传文件和其他的普通参数
     *
     * @param httpReq
     */
    public void uploadFileByImageName(HttpReq httpReq) {
        PostFormBuilder post = OkHttpUtils.post();
        if (httpReq.getPathList() != null) {
            for (Map.Entry<String, String> entry : httpReq.getPathList().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(httpReq.getUploadFileParamsName())) {
                    post.addFile(key, key, new File(value));
                } else {
                    post.addFile(httpReq.getUploadFileParamsName(), key, new File(value));
                }
            }
        }
        post.url(httpReq.getUrl())
                .params(httpReq.getParams())
                .tag(httpReq.getTag())
                .build()
                .execute(new OkHttpCallBack(httpReq));
    }

    /**
     * android上传文件到服务器
     * <p>
     * 下面为 http post 报文格式
     * <p>
     * POST/logsys/home/uploadIspeedLog!doDefault.html HTTP/1.1 Accept: text/plain, Accept-Language: zh-cn Host: 192.168.24.56
     * Content-Type:multipart/form-data;boundary=-----------------------------7db372eb000e2 User-Agent: WinHttpClient Content-Length: 3693 Connection: Keep-Alive 注：上面为报文头
     * -------------------------------7db372eb000e2 Content-Disposition: form-data; name="file"; filename="kn.jpg" Content-Type: image/jpeg (此处省略jpeg文件二进制数据...）
     * -------------------------------7db372eb000e2--
     *
     * @param picPaths   需要上传的文件路径集合
     * @param requestURL 请求的url
     * @return 返回响应的内容
     */
    public static String postFiles(String requestURL, String... picPaths) {
        String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
        String prefix = "--", end = "\r\n";
        String content_type = "multipart/form-data"; // 内容类型
        String CHARSET = "utf-8"; // 设置编码
        int TIME_OUT = 60 * 1000; // 超时时间
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", content_type + ";boundary=" + boundary);
            /**
             * 当文件不为空，把文件包装并且上传
             */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);

            String name = "userFile";

            for (int i = 0; i < picPaths.length; i++) {
                if (picPaths[i] == null) {
                    continue;
                }
                File file = new File(picPaths[i]);
                if (file.exists() == false) {
                    continue;
                }
                StringBuffer sb = new StringBuffer();
                sb.append(prefix);
                sb.append(boundary);
                sb.append(end);

                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"" + end);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + end);
                sb.append(end);
                dos.write(sb.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[8192];// 8k
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(end.getBytes());// 一个文件结束标志
            }
            byte[] end_data = (prefix + boundary + prefix + end).getBytes();// 结束 http 流
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            if (res == 200) {
                // 获取响应流
                StringBuffer stringBuffer1 = new StringBuffer();
                InputStream ins = conn.getInputStream();
                int ch;
                while ((ch = ins.read()) != -1) {
                    stringBuffer1.append((char) ch);
                }
                ins.close();
                return stringBuffer1.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getParamsString(Map<String, String> params) {
        if (params == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value == null) {
                    continue;
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
                sb.append("&");
            }
            String result = sb.toString();
            return result;
        }
    }

    /**
     * 获取编码后的变量值
     *
     * @return
     */
    public static String getEncodeParam(String param) {
        if (param == null) {
            return null;
        }
        try {
            String encodeStr = URLEncoder.encode(param, "UTF-8");
            return encodeStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

}
