package com.maxiaobu.healthclub.utils.rx;

import com.maxiaobu.healthclub.common.beangson.BeanMe;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 马小布 on 2016/11/14.
 */

public interface UrlRetrofitApi {
    @GET("mme.do" )
    Observable<BeanMe> postUrlMe(
            @Query("memid") String memid
    );
}
