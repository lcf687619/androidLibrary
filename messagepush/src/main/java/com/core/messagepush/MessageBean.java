package com.core.messagepush;

import java.io.Serializable;


/**
 * @author coffee<br>
 *         2014年6月20日上午11:43:05
 */
public class MessageBean implements Serializable, Cloneable {
    //	{type:1,message:您的拼包3邮票还有10天过期，请尽快使用，点击查看详情！}
    private int type;
    private String message;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}