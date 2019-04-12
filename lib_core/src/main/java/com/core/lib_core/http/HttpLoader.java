package com.core.lib_core.http;

import android.annotation.SuppressLint;
import android.os.Message;

import com.core.lib_core.activity.HandlerMgr;
import com.core.lib_core.net.NetReceiver;
import com.core.lib_core.utils.NiceLogUtil;

import java.util.Vector;


/**
 * @author coffee
 * <p>
 * 2013年12月17日下午2:40:24
 */
@SuppressLint("UseSparseArrays")
public class HttpLoader {

    public final static int STATE_FAILED = -1;
    public final static int STATE_PREPARE = 0; // 初始状态
    public final static int STATE_START = 1;
    public final static int STATE_SUCCESS = 2;

    // 加载文本
    public final static int TYPE_TEXT = 0;
    // 上传(图片|文件)和其他参数
    public final static int TYPE_upload = 1;

    public static Vector<HttpReq> textReq = new Vector<HttpReq>();

    private static HttpLoader instance;

    public static HttpLoader getInstance() {
        if (instance == null) {
            instance = new HttpLoader();
        }
        return instance;
    }

    public void sendReq(final HttpReq req) {
        if (req.getParam(HttpParam.toastNet) == false) {
            // 如果指定不需要提示的话,则跳过网络是否可用的检查
        } else {
            if (NetReceiver.isConnected() == false) {
                Message msg = Message.obtain();
                msg.what = MsgID.net_unavailable;
                msg.obj = "网络不可用";
                HandlerMgr.removeCallbacksAndMessages();
                HandlerMgr.sendMessage(msg, 1000);
            }
        }
        // 如果线程池中存在该请求
        boolean has = false;
        for (int i = 0; i < textReq.size(); i++) {
            if (textReq.get(i).getMsgId() == req.getMsgId() && req.getState() != STATE_FAILED && textReq.get(i).getArg1() == req.getArg1()) {
                has = true;
                break;
            }
        }
        // 该请求中线程池中已经存在
        if (has) {
            return;
        } else {
            textReq.add(0, req); // ////////////不同点，文本请求有优先级
        }
        req.setState(STATE_START);
        // 默认的数据加载方式
        NiceLogUtil.w("http:url_" + req.getMsgId() + "：" + req.getUrl() + "\n" + req.getParams(), null);
        if (req.getType() == TYPE_upload) {
            req.getParams().remove(HttpParam.postText);
            req.getParams().remove(HttpParam.toastNet);
            new HttpClient().uploadFileByImageName(req);
        } else {
            if (req.getParam(HttpParam.postText)) {
                req.getParams().remove(HttpParam.postText);
                req.getParams().remove(HttpParam.toastNet);
                new HttpClient().post(req);
            } else {
                req.getParams().remove(HttpParam.postText);
                req.getParams().remove(HttpParam.toastNet);
                HttpClient client = new HttpClient();
                req.setHttpClient(client);
                client.get(req);
            }
        }
    }
}
