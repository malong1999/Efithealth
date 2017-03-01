package com.maxiaobu.healthclub.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.FileUtils;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 马小布 on 2016/12/26.
 * email：maxiaobu1216@gmail.com
 * 功能：项目工具类
 * 伪码：
 * 待完成：
 */
public class HealthUtil {
    public static final String TAG = "HealthUtil";

    public static void showNONetworkDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("无网连接")
                .negativeColor(Color.parseColor("#Fb8435"))
                .positiveColor(Color.parseColor("#Fb8435"))
                .content("请检查网络设置")
                .negativeText("取消")
                .positiveText("设置")
                .onPositive((dialog, which) -> NetworkUtils.openWirelessSettings(context))
                .onNegative((dialog, which) -> dialog.dismiss()).show();
    }

    public static Dialog getChrysanthemumDialog(Context mContext) {
        int layout = R.layout.progress_image;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(layout, null);
        RelativeLayout lay = (RelativeLayout) v.findViewById(R.id.ly_root);
        ImageView spacesshipImage = (ImageView) v.findViewById(R.id.loading_process_dialog_progressBar);
        AnimationDrawable mHeaderChrysanthemumAd = (AnimationDrawable) spacesshipImage.getDrawable();
        mHeaderChrysanthemumAd.start();
        Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.setContentView(lay, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        return dialog;
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    public static long ipStrToLong(String ipaddress) {
        long[] ip = new long[4];
        int position1 = ipaddress.indexOf(".");
        int position2 = ipaddress.indexOf(".", position1 + 1);
        int position3 = ipaddress.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(ipaddress.substring(0, position1));
        ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 设置当前用户头像给imageview
     *
     * @param activity
     * @param imageView
     * @param url
     */
    public static void setAvator(Activity activity, ImageView imageView, String url, boolean cir) {
        //http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000455_1482473304503.png@!BMEMBER_P
        String[] split = url.split("/");
        String s = split[split.length - 1];//M000455_1482473304503.png@!BMEMBER_P
        String[] split1 = s.split("@");
        String imgpfilename = split1[0];
//        Log.d(TAG, Constant.CACHE_DIR_IMAGE + imgpfilename + ".png");
//        FileUtils.isFileExists(Constant.CACHE_DIR_IMAGE + "/" + imgpfilename + ".png");
        boolean fileExists = FileUtils.isFileExists(Constant.CACHE_DIR_IMAGE + imgpfilename);
        Log.d("HealthUtil", Constant.CACHE_DIR_IMAGE + imgpfilename);
        if (fileExists) {
            if (cir) {
                Glide.with(activity).load(new File(Constant.CACHE_DIR_IMAGE + imgpfilename))
                        .transform(new GlideCircleTransform(activity)).into(imageView);
            } else {
                Glide.with(activity).load(new File(Constant.CACHE_DIR_IMAGE + imgpfilename)).into(imageView);
            }

        } else {
            String picFileName = Constant.CACHE_DIR_IMAGE + imgpfilename;
            Observable<ResponseBody> bodyObservable = App.getRetrofitUtil().getRetrofit().downloadPicFromNet(url);
            bodyObservable.subscribeOn(Schedulers.io())
                    .map(responseBody -> writeResponseBodyToDisk(responseBody, picFileName))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (cir) {
                                Glide.with(activity).load(url).transform(new GlideCircleTransform(activity)).into(imageView);
                            } else {
                                Glide.with(activity).load(url).into(imageView);
                            }
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                if (cir) {
                                    Glide.with(activity).load(new File(picFileName))
                                            .transform(new GlideCircleTransform(activity)).into(imageView);
                                } else {
                                    Glide.with(activity).load(new File(picFileName)).into(imageView);
                                }

                            } else {
                                if (cir) {
                                    Glide.with(activity).load(url).transform(new GlideCircleTransform(activity)).into(imageView);
                                } else {
                                    Glide.with(activity).load(url).into(imageView);
                                }
                            }
                        }
                    });
        }
    }

    public static boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            // todo change the file location/name according to your needs
            File parent = new File(Constant.CACHE_DIR_IMAGE);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileUtils.createFileByDeleteOldFile(fileName);
            File futureStudioIconFile = new File(fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 同步服务器个人信息
     */
    /*public static void update_local_myinfo() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getInstance().getRequestInstance().post(UrlPath.URL_MYINFO, App.getInstance(), params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.getInstance(), "同步服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getString("msgFlag").equals("1")) {
                            JSONObject memberobj = data.getJSONObject("member");
                            String nickname = memberobj.getString("nickname");
                            String brithday = memberobj.getString("birth").toString();
                            String sex = memberobj.getString("gender");
                            String mysign = memberobj.getString("signature");
                            String addressname = memberobj.getString("recname");
                            String addresphone = memberobj.getString("recphone");
                            String address = memberobj.getString("recaddress");
                            String memrole = memberobj.getString("memrole");
                            String headpage = memberobj.getString("headpage");
                            String headImgUrl = UrlPath.URL_RESOURCE + memberobj.getString("imgpfile");
                            SPUtils.putString("nickname", nickname);
                            SPUtils.putString(Constant.BRITHDAY, brithday);
                            SPUtils.putString(Constant.GENDER, sex);
                            SPUtils.putString(Constant.MY_SIGN, mysign);
                            SPUtils.putString(Constant.REC_NAME, addressname);
                            SPUtils.putString(Constant.REC_PHONE, addresphone);
                            SPUtils.putString(Constant.REC_ADDRESS, address);
                            SPUtils.putString("headImgUrl", headImgUrl);
                            SPUtils.putString(Constant.MEMROLE, memrole);
                            SPUtils.putString("headpage", headpage);

                            DemoHelper instance = DemoHelper.getInstance();
                            instance.getUserProfileManager().updateCurrentUserNickName(nickname);
                            instance.getUserProfileManager().setCurrentUserAvatar(headImgUrl);
                            JSONArray imglist_top = data.getJSONArray("istopfile");
                            String topImglist = "";
                            String topidlist = "";
                            for (int i = 0; i < imglist_top.length(); i++) {
                                topImglist += imglist_top.getJSONObject(i).getString("imgpfilename");
                                topidlist += imglist_top.getJSONObject(i).getString("medid");
                                if (i != imglist_top.length() - 1) {
                                    topImglist += ",";
                                    topidlist += ",";
                                }
                            }
                            String moreImglist = "";
                            String moreidlist = "";
                            JSONArray imglist_more = data.getJSONArray("imagefile");
                            for (int i = 0; i < imglist_more.length(); i++) {
                                moreImglist += imglist_more.getJSONObject(i).getString("imgpfilename");
                                moreidlist += imglist_more.getJSONObject(i).getString("medid");
                                if (i != imglist_more.length() - 1) {
                                    moreImglist += ",";
                                    moreidlist += ",";
                                }
                            }

                            SPUtils.putString("topImgList", topImglist);
                            SPUtils.putString("moreImgList", moreImglist);
                            SPUtils.putString("topidlist", topidlist);
                            SPUtils.putString("topidlist", topidlist);
                            Log.e("MyApplication", "照片墙信息更新成功");

                            Log.e("MyApplication", "个人信息更新成功");

                        } else {
                            Toast.makeText(App.getInstance(), "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {

            }
        });
    }

    *//**
     * 同步服务器照片墙
     *//*
    public static void update_loacl_imgWall() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(App.getInstance(), UrlPath.URL_MPHOTOWALL, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.getInstance(), "同步服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getString("msgFlag").equals("1")) {
                            JSONArray imglist_top = data.getJSONArray("istopfile");
                            String topImglist = "";
                            String topidlist = "";
                            for (int i = 0; i < imglist_top.length(); i++) {
                                topImglist += imglist_top.getJSONObject(i).getString("imgpfilename");
                                topidlist += imglist_top.getJSONObject(i).getString("medid");
                                if (i != imglist_top.length() - 1) {
                                    topImglist += ",";
                                    topidlist += ",";
                                }
                            }
                            String moreImglist = "";
                            String moreidlist = "";
                            JSONArray imglist_more = data.getJSONArray("imagefile");
                            for (int i = 0; i < imglist_more.length(); i++) {
                                moreImglist += imglist_more.getJSONObject(i).getString("imgpfilename");
                                moreidlist += imglist_more.getJSONObject(i).getString("medid");
                                if (i != imglist_more.length() - 1) {
                                    moreImglist += ",";
                                    moreidlist += ",";
                                }
                            }
                            SPUtils.putString("topImgList", topImglist);
                            SPUtils.putString("moreImgList", moreImglist);
                            SPUtils.putString("topidlist", topidlist);
                            SPUtils.putString("moreidlist", moreidlist);
                            Log.e("MyApplication", "照片墙信息更新成功");
                        } else {
                            Toast.makeText(App.getInstance(), "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(App.getInstance(), "json解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();
            }
        });
    }

    // 异步更新好友信息
    public static void update_loacl_friend() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(App.getInstance(), UrlPath.URL_MGETFRIENDS, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.getInstance(), "同步服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getString("msgFlag").equals("1")) {
//                            Log.i("djy", data.toJSONString());
                            JSONArray jsonfriends = data.getJSONArray("friendslist");
                            Map<String, EaseUser> localUsers = DemoHelper.getInstance().getContactList();

                            for (int i = 0; i < jsonfriends.length(); i++) {
                                String f_memeid = jsonfriends.getJSONObject(i).getString("memid").toLowerCase();
                                String f_nickname = jsonfriends.getJSONObject(i).getString("nickname");
                                String f_headimg = jsonfriends.getJSONObject(i).getString("imgsfile");
                                EaseUser f_user = DemoHelper.getInstance().getUserInfo(f_memeid);
                                f_user.setAvatar(f_headimg);
                                f_user.setNick(f_nickname);
                                localUsers.put(f_memeid, f_user);
                            }
                            ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
                            mList.addAll(localUsers.values());
                            DemoHelper.getInstance().updateContactList(mList);
                            Log.e("MyApplication", "好友信息更新成功");
                        } else {
                            Toast.makeText(App.getInstance(), "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(App.getInstance(), "json解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();
            }
        });
    }

    // 异步获取首页信息
    public static void update_loacl_indexdata() {
        RequestParams params = new RequestParams();
        params.put("longitude",SPUtils.getString(Constant.LONGITUDE));
        params.put("latitude",SPUtils.getString(Constant.LATITUDE));
        params.put("memid",SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(App.getInstance(), UrlPath.URL_MINDEX, BeanMindex.class, params, new RequestJsonListener<BeanMindex>() {
            @Override
            public void requestSuccess(BeanMindex beanMindex) {
                if (beanMindex.getMsgFlag().equals("1")){
                    List<BeanMindex.PageHImageListBean> small_imgArry = beanMindex.getPageHImageList();
                    String samll_img_url_1 = small_imgArry.get(0).getImgpfile();
                    String samll_img_url_2 = small_imgArry.get(1).getImgpfile();
                    String samll_img_url_3 = small_imgArry.get(2).getImgpfile();
                    String samll_img_url_4 = small_imgArry.get(3).getImgpfile();
                    List<BeanMindex.PageVImageListBean> big_imgArry = beanMindex.getPageVImageList();

                    String big_img = "";
                    for (int i = 0; i < big_imgArry.size(); i++) {
                        big_img += big_imgArry.get(i).getImgpfile();
                        if (i != big_imgArry.size() - 1) {
                            big_img += ",";
                        }
                    }
                    SPUtils.putString("big_img_url",big_img);
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();

            }
        });

    }
*/


}
