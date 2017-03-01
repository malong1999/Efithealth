package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/9/7.
 */
public class BeanMrsendCode {

    /**
     * msgFlag : 1
     * msgContent : [18624616670]短信发送成功!
     */

    private String msgFlag;
    private String msgContent;

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
