package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/12/22：15:11.
 * introduction：我长得真他娘的磕碜，单身未娶，求包养
 * email：maxiaobu1216@gmail.com
 * 功能：
 * 伪码：
 * 待完成：
 */

public class BeanAppToken {

    /**
     * msgFlag : 1
     * msgContent : 获得Token成功
     * status : 200
     * AccessKeyId : STS.HCUckH1QVGPrGbzdpwLEAeB3e
     * AccessKeySecret : 4dV9pw3VYvzM3GNFWmYQd1bu5MqU6BSSXN1hzK9Q32Kz
     * SecurityToken : CAISrAN1q6Ft5B2yfSjIqYDgKNH83I538JKZRUTLgHAiQMptioeYhzz2IHFOdXZvBu0Ztfg2lG5U6/sTlqB6T55OSAmcNZIoLRCnM/niMeT7oMWQweEuEvTHcDHhj3eZsebWZ+LmNjm0GJOEYEzFkSle2KbzcS7YMXWuLZyOj+xlDLEQRRLqQjdaI91UKwB+yqodLmCDDeuxFRToj2HMbjJvoREupW575Liy5cee5xHC7jj90fRHg4XqPqCtddV3Xud4SMzn9eZxbLbkzSpM6gBD7rtLlKhD8Dul2daGGAt14g6aFODW/9ZzN3VPBM4AFrVDseL3mNBhp+XXjP6X8RtWOvxPWCmtbuLoycDfSuSyLYR7J/SpNnrMztCCP53psxkpenUGLghHPJhDXHZ7Ek4rUSqIaP3lql3EYxujRqGBzKEs3IFy0k/j8PjyfgXWHu/Ejn5FYcVhMhp4aiR7hzKxLvU0FCVXaE5iCrqvSoF+UQtTs6uAv3eJB3cxkS4I4qKlO6qG5PxCNNTlLZtC0JsAYpNdqHctTFnxRL+oh10daGt/W7FS3bkU/WtkZBQ1qhqAAaN66Lrk8kneIpolO4uFbwXh64GI4xZLaO0M6JSQUOq8B0MYZ616UFYTkvNSAClSOqZuz/ZNUfPnOJk/Osq4yBcwiqvE1bLyLd2VccONOuKJjvz0ZMiGQiKv3pMtS6AmoutxjTdhCHBSIyANQEoQVG2eu7KvzhBrVvwDRbYw4cFX
     * Expiration : 2016-12-22T06:44:35Z
     */

    private String msgFlag;
    private String msgContent;
    private String status;
    private String AccessKeyId;
    private String AccessKeySecret;
    private String SecurityToken;
    private String Expiration;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }
}
