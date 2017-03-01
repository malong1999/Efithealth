package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/9/26.
 */
public class BeanMrelation {

    /**
     * msgFlag : 1
     * msgContent : 判断用户关系
     * isbind : 1
     * isfollow : 0
     * isblacker : 0
     */

    private String msgFlag;
    private String msgContent;
    private String isbind;
    private String isfollow;
    private String isblacker;

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

    public String getIsbind() {
        return isbind;
    }

    public void setIsbind(String isbind) {
        this.isbind = isbind;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public String getIsblacker() {
        return isblacker;
    }

    public void setIsblacker(String isblacker) {
        this.isblacker = isblacker;
    }
}
