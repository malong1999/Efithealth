package com.maxiaobu.healthclub.utils.rx;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

/**
 * Created by 马小布 on 2016/11/14.
 */

public class UrlRetrofitService {

   /* private UrlRetrofitService() {
    }

    public static UrlRetrofitApi createUrlRetrofitService(final String githubToken) {
        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//指定一个factory来对响应反序列化，在这里converters被添加的顺序将是它们被Retrofit尝试的顺序
                .baseUrl("https://api.github.com");
*//*.addInterceptor(chain -> {//实现实际的底层的请求和响应日志 log响应信息
                Request request = chain.request();
                Request newReq = request.newBuilder()
                        .addHeader("Authorization", format("token %s", githubToken))
                        .build();
                return chain.proceed(newReq);
            }).build();*//*
        if (!TextUtils.isEmpty(githubToken)) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain ->{
                Request request = chain.request();
                Request newReq = request.newBuilder()
                        .addHeader("Authorization", format("token %s", githubToken))
                        .build();
                return chain.proceed(newReq);
            }).build();

            builder.client(client);
        }

        return builder.build().create(UrlRetrofitApi.class);
    }*/
}
