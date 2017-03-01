package com.maxiaobu.healthclub.common;

import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.common.beangson.BeanGroupList;
import com.maxiaobu.healthclub.common.beangson.BeanLunchOrderList;
import com.maxiaobu.healthclub.common.beangson.BeanMessayTypeList;
import com.maxiaobu.healthclub.common.beangson.BeanMe;
import com.maxiaobu.healthclub.common.beangson.BeanMineSign;
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;
import com.maxiaobu.healthclub.common.beangson.BeanUpdata;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 马小布 on 2016/12/1.
 */
public interface RetrofitService {
    //下载文件
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);
    @POST("mme.do")
    Observable<BeanMe> getBeanMe(@Query("memid") String memid);

    @POST("mversionupd.do")
    Observable<BeanUpdata> getBeanUpdata();

    @GET
    Observable<JsonObject> getStringScan(@Url String url);

    /**
     * 全部课程->分类集合
     * http://192.168.1.121:8080/efithealth/messayTypeList.do
     */
    @POST("messayTypeList.do")
    Observable<BeanMessayTypeList> getBeanMessayTypeList();

    /**
     * 登录
     * http://192.168.1.182:8080/efithealth/mlogin.do?mobphone=18624616670&mempass=123456&phonedeviceno=
     * mobphone  用户名
     * mempass      密码
     * phonedeviceno   设备id
     */
    @POST("mlogin.do")
    Observable<JsonObject> getJsonLogin(@Query("mobphone") String mobphone,
                                        @Query("mempass") String mempass,
                                        @Query("phonedeviceno") String phonedeviceno);


    /**
     * 注册环信
     * http://192.168.1.182:8080/efithealth/mregisterEase.do?memid=M000455
     * memid
     */
    @POST("mregisterEase.do")
    Observable<JsonObject> getJsonRegisterEase(@Query("memid") String memid,@Query("mempass") String password);

    /**
     * 群列表
     * http://192.168.1.182:8080/efithealth/mgroup!mygrouplist.do?memid=m000455
     */
    @POST("mgroup!mygrouplist.do")
    Observable<BeanGroupList> getBeanMygrouplist(@Query("memid") String memid);

    /**
     * 群列表
     * http://192.168.1.182:8080/efithealth/mgroup!mygrouplist.do?memid=m000455
     */
    @POST("msinginsave.do")
    Observable<BeanMineSign> getBeanSinginSave(@Query("memid") String memid);

    /**
     * 保存个人信息
     * @param memid
     * @return
     */
    @FormUrlEncoded
    @POST("mupdateMember.do")
    Observable<JsonObject> getJsonUpdateMember(@Field("imgpath") String imgpath,
                                               @Field("memid") String memid,
                                               @Field("gender") String gender,
                                               @Field("nickname") String nickname,
                                               @Field("signature") String signature,
                                               @Field("birthday") String birthday);
    /**
     * 教练列表coaches_list
     * pageIndex:页码(1开始),
     * memid: 当前用户id ,
     * latitude: 纬度,
     * longitude: 经度,
     * sorttype: 不限；按距离、按好评(evascore)、按热度(coursetimes),
     * gender:不限；男(1)；女(0)
     * <p>
     * http://192.168.1.121:8080/efithealth/mcoachList.do?memid=M000439&pageIndex=1&latitude=41.811237&longitude=123.432856&sorttytpe=distance&gender=all
     */
    @POST("mcoachList.do")
    Observable<JsonObject> getJsonCoachList(@Query("pageIndex") String pageIndex,@Query("memid") String memid,
                                        @Query("latitude") String latitude,@Query("longitude") String longitude,
                                        @Query("sorttytpe") String sorttytpe,@Query("gender") String gender);

    /**
     * 订餐列表
     * http://192.168.1.182:8080/efithealth/mbFoodmers.do?memid=M000439&mertype=all&pageIndex=1&sorttype=sorttype
     */
    @POST("mbFoodmers.do")
    Observable<JsonObject> getJsonFoodmers(@Query("memid") String memid,@Query("pageIndex") String pageIndex,
                                            @Query("sorttype") String sorttype,@Query("mertype") String mertype);

    /**
     * 会员动态列表
     * pageIndex:页码(1开始),
     * tarid:教练ID,
     * memid:用户ID
     * http://192.168.1.121:8080/efithealth/mDynamicList.do?pageIndex=1&tarid=M000440&memid=M000439
     */
    @POST("mgetSociarelByMemid.do")
    Observable<JsonObject> getJsonDynamicList(@Query("socialrel.memid") String tarid,
                                            @Query("memid") String memid);
    /**
     * 订餐详情
     * 商品id
     * http://192.168.1.182:8080/efithealth/mgetFoodmers.do?merid=M000019
     */
    @POST("mgetFoodmers.do")
    Observable<JsonObject> getJsonGetFoodmers(@Query("merid") String merid);
    /**
     * 配餐订单列表+课程订单列表
     * pageIndex: pageIndex++, //当前页码forderlist
     * listtype: "forderlist","corderlist"//写死，就写他，区分出订餐订单
     * http://192.168.1.182:8080/efithealth/morderlist.do?memid=M000455&listtype=corderlist&pageIndex=1
     * memid: getMemid()//用户id
     */
    @POST("morderlist.do")
    Observable<BeanCorderList> getBeanCorderList(@Query("pageIndex") String pageIndex,
                                                 @Query("listtype") String corderlist,
                                                 @Query("memid") String memid);

    /**
     * 配餐订单列表+课程订单列表
     * pageIndex: pageIndex++, //当前页码forderlist
     * listtype: "forderlist","corderlist"//写死，就写他，区分出订餐订单
     * http://192.168.1.182:8080/efithealth/morderlist.do?memid=M000455&listtype=corderlist&pageIndex=1
     * memid: getMemid()//用户id
     */
    @POST("morderlist.do")
    Observable<BeanLunchOrderList> getBeanLunchOrderList(@Query("listtype") String listtype,
                                                     @Query("memid") String memid,
                                                     @Query("pageIndex") String pageIndex);
    /**
     * 取消课程订单
     * http://192.168.1.121:8080/efithealth/mdeleteByList.do?ordno=FO-20160726-170&listtype=corderlist
     * ordno: "'" + orderid + "'",
     * listtype: "corderlist"
     * {"msgFlag":"1","msgContent":"取消订单成功"}
     */
    @POST("mdeleteByList.do")
    Observable<JsonObject> getJsonDeleteByList(@Query("ordno") String ordno,
                                               @Query("listtype") String listtype);

    /**
     * 取消订单
     * http://192.168.1.121:8080/efithealth/mcancelForder.do?ordno=FO-20160726-170
     * {"msgFlag":"1","msgContent":"取消订单成功"}
     */
    @POST("mcancelForder.do")
    Observable<JsonObject> getJsonCancelForder(@Query("ordno") String ordno);

    /**
     * 延期
     * http://192.168.1.121:8080/efithealth/mextension.do?ordno=FO-20160726-170
     * {"msgFlag":"1","msgContent":"延期成功"}
     */
    @POST("mextension.do")
    Observable<JsonObject> getJsonExtension(@Query("ordno") String ordno);

    /**
     * 我的预约
     * http://192.168.1.182:8080/efithealth/mmyBespeak.do?memid=M000439
     */
    @POST("mmyBespeak.do")
    Observable<BeanMmyBespeak> getBeanMyBespeak(@Query("pageIndex") String pageIndex,
                                                @Query("memid") String memid);
}
