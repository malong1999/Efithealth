package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/9/27.
 */
public class BeanUpdata {


    /**
     * latest : 1.0.2
     * url : http://efithealthresource.oss-cn-beijing.aliyuncs.com/AndroidUpdate/app-debug.apk
     * msgContent : �����¡�\r\n\r\n- ���ԣ������������ܼ��ȶ���\r\n- �ٻ��������û���ͼģʽ�޷�������΢�ż�����Ȧ
     * msgFlag : 1
     */

    private String latest;
    private String url;
    private String msgContent;
    private String msgFlag;

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }
}
