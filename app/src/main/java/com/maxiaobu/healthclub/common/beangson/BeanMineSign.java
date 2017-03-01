package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by Administrator on 2016/10/12.
 */

public class BeanMineSign {

    /**
     * msgFlag : 1
     * msgContent : 签到次数
     * count : 1
     */

    private String msgFlag;
    private String msgContent;
    private String count;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
