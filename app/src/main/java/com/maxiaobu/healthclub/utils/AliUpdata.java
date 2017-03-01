package com.maxiaobu.healthclub.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.common.UrlPath;

import static android.R.attr.path;

/**
 * Created by 马小布 on 2016/12/22.
 * introduction: 真他娘的不知道说点啥
 * email:maxiaobu1999@163.com
 * 功能：阿里上传图片
 * 伪码：
 * 待完成：
 */
public class AliUpdata {
    private static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static final String bucket = "efithealthresource";


    public static OssService getOssService() {
        OssService ossService = initOSS( endpoint, bucket);
        return ossService;


    }

     static OssService initOSS( String endpoint, String bucket) {
        OSSCredentialProvider credentialProvider;
        credentialProvider = new STSGetter();
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(App.getInstance(), endpoint, credentialProvider, conf);
        return new OssService( oss, bucket);
    }
}
