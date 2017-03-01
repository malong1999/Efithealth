package com.maxiaobu.healthclub;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hyphenate.chat.EMOptions;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.retrofitUtils.RetrofitUtil;
import com.maxiaobu.healthclub.utils.rx.RxBus;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.IRequest;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by 马小布 on 2016/9/2.
 */
public class App extends Application {
    public static final String APP_ID = "2882303761517525233";
    public static final String APP_KEY = "5361752551233";
    private static App instance;
    private static IRequest sIRequest;
    private static RetrofitUtil sRetrofitUtil;

    private static List<String> GroupIdList = new ArrayList<>();
    private static List<String> AvarList = new ArrayList<>();
    private static List<String> GroupIidList = new ArrayList<>();

    public static List<String> getAvarList() {
        return AvarList;
    }

    public static void setAvarList(List<String> avarList) {
        AvarList = avarList;
    }

    public static List<String> getGroupIdList() {
        return GroupIdList;
    }

    public static void setGroupIdList(List<String> groupIdList) {
        GroupIdList = groupIdList;
    }

    public static List<String> getGroupIidList() {
        return GroupIidList;
    }

    public static void setGroupIidList(List<String> groupIidList) {
        GroupIidList = groupIidList;
    }

    public static IRequest getRequestInstance() {
        return sIRequest;
    }

    public static App getInstance() {
        return instance;
    }

    public static RxBus rxBus = null;

    public static RxBus getRxBusSingleton() {
        if (rxBus == null) {
            rxBus = new RxBus();
        }
        return rxBus;
    }

    public static RetrofitUtil getRetrofitUtil() {
        if (sRetrofitUtil == null)
            sRetrofitUtil = new RetrofitUtil(UrlPath.URL_BASE);
        return sRetrofitUtil;
    }
    {
        PlatformConfig.setWeixin("wxbd0625462c614906", "a8d4e19fb4844d1c76d98f93078e3c4e");
        //没有注册
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("1105737533", "Lz6oQcRcNebEEjmf");
    }

    private Drawable mDrawableHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        sIRequest = new IRequest(this);
        SDKInitializer.initialize(this);
        UMShareAPI.get(this);
        Config.IsToastTip = false;
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        DemoHelper.getInstance().init(this);
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d("++", content, t);
            }

            @Override
            public void log(String content) {
                Log.d("++", content);
            }
        };
        Logger.setLogger(this, newLogger);
//
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        enabledStrictMode();
//        LeakCanary.install(this);
    }

    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public LocationClientOption getLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setAddrType("all");
        //1秒发一次
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);// 位置，一定要设置，否则后面得不到地址
        option.setOpenGps(true);// 打开GPS
        //option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
//        option.setScanSpan(180000);// 多长时间进行一次请求       option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度
        return option;
    }

    public void setDrawableHolder(Drawable drawable) {
        mDrawableHolder = drawable;
    }

    public Drawable getDrawableHolder() {
        Drawable drawable = mDrawableHolder;
        mDrawableHolder = null;
        return drawable;
    }

    private LocationClient mLocationClient;
    public BDLocationListener myListener;
    private long mTime;
    private Boolean flag = true;

    private LatLng clubLatLng;

    //误差次数
    Double max = 0.0, min = 0.0, sum = 0.0;
    private int times = 1;

    public void moxiaoting() {
        mTime = System.currentTimeMillis();
        //已经不是当天了 或者 第一次进入程序。
        if ((mTime - SPUtils.getLong(com.maxiaobu.healthclub.common.Constant.foorTime)) / (1000 * 60) >= 1440 || SPUtils.getLong(com.maxiaobu.healthclub.common.Constant.foorTime) == -1) {
            //结束循环
            SPUtils.putBoolean(com.maxiaobu.healthclub.common.Constant.foorBOOLEAN, false);
        }

        mLocationClient = new LocationClient(App.getInstance());
        myListener = new LocationListener();
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();
        //循环查询
        new Thread(() -> {
            while (true) {
                while (SPUtils.getBoolean(com.maxiaobu.healthclub.common.Constant.foorBOOLEAN)) {
                    mTime = System.currentTimeMillis();
                    //当程序第一次运行 以及 判断 上一次定位与本次定位的间隔
                    if (SPUtils.getLong(com.maxiaobu.healthclub.common.Constant.foorTime) == -1
                            || (mTime - SPUtils.getLong(com.maxiaobu.healthclub.common.Constant.foorTime)) / (1000 * 60) == 30) {
                        //相同时间内保证只循环一次
                        if (mTime - SPUtils.getLong(com.maxiaobu.healthclub.common.Constant.foorTime) == 0) {
                            flag = false;
                        } else {
                            flag = true;
                        }
                        if (flag) {

                            mLocationClient.start();

                            //上一次定位的时间
                            SPUtils.putLong(com.maxiaobu.healthclub.common.Constant.foorTime, System.currentTimeMillis());
                            flag = false;
                        }
                    }
                }
            }
        }).start();

        //为了循环查询
        if (SPUtils.getLong(com.maxiaobu.healthclub.common.Constant.foorTime) != -1 && SPUtils.getBoolean(com.maxiaobu.healthclub.common.Constant.foorBOOLEAN)) {
            //上一次定位的时间
            SPUtils.putLong(com.maxiaobu.healthclub.common.Constant.foorTime, mTime);
        }

    }

    // 设置百度MAP 定位相关参数
    public void setLocationOption() {
        mLocationClient.setLocOption(getLocationOption());// 使用设置
    }


    /**
     * Description:百度MAP 定位成功回调接口方法
     *
     * @author Xushd
     * @since 2016年2月20日上午11:14:36
     */
    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            SPUtils.putString(com.maxiaobu.healthclub.common.Constant.LATITUDE, location.getLatitude() + "");
            SPUtils.putString(com.maxiaobu.healthclub.common.Constant.LONGITUDE, location.getLongitude() + "");
            clubLatLng = new LatLng(Double.valueOf(SPUtils.getString(
                    com.maxiaobu.healthclub.common.Constant.clubLATITUDE)),
                    Double.valueOf(SPUtils.getString(com.maxiaobu.healthclub.common.Constant.clubLONGITUDE)));
            if (clubLatLng != null) {
                LatLng myLocal = new LatLng(location.getLatitude(), location.getLongitude());
                CoordinateConverter converter = new CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                // sourceLatLng待转换坐标
                converter.coord(myLocal);
                LatLng desLatLng = converter.convert();

                //算5次的平均数，精确位置
                getAverage(DistanceUtil.getDistance(desLatLng, clubLatLng));

                if (times == 5) {
                    if (((sum - max - min) / 3) >= 520) {

                        Log.d("myapp", "已经超出范围: " + (sum - max - min) / 3);
                        //关闭循环
                        SPUtils.putBoolean(com.maxiaobu.healthclub.common.Constant.foorBOOLEAN, false);
                        RequestParams params = new RequestParams();
                        params.put("memid", SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
                        params.put("clubid", SPUtils.getString(com.maxiaobu.healthclub.common.Constant.clubID));

                        App.getRequestInstance().post(UrlPath.URL_FOOR, instance, params, new RequestListener() {
                            @Override
                            public void requestSuccess(String s) {
                                Toast.makeText(instance, s, Toast.LENGTH_SHORT).show();
                                Toast.makeText(instance, "已经超出范围: " + (sum - max - min) / 3,
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void requestError(VolleyError volleyError, String s) {
                                Toast.makeText(App.this, s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void noInternet(VolleyError volleyError, String s) {
                                Toast.makeText(App.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("myapp", "您没有超出距离s:" + (sum - max - min) / 3);

                        times = 0;
                        sum = 0.0;
                        max = 0.0;
                        min = 0.0;
                    }
                    //停止百度定位
                    mLocationClient.stop();
                }
                times++;
            }
        }

        public void onReceivePoi(BDLocation location) {
        }
    }

    //求平均数 去掉一个最大值和最小值
    public double getAverage(Double n) {

        sum += n;
        if (max < n) max = n;
        if (min > n) min = n;

        return sum;
    }


}
