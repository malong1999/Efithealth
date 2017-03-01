package com.maxiaobu.healthclub.utils.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.activity.ChatActivity;
import com.maxiaobu.healthclub.ui.activity.WebActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.LoadingFragment;

//作用仅仅是屏蔽android lint错误   即屏蔽安卓版本问题的错误
@SuppressLint("NewApi")
public class BaseJsToAndroid {
    private Context context;
    public LoadingFragment dialog = LoadingFragment.getInstance();

    public BaseJsToAndroid(Context context) {
        this.context = context;
    }


    /**
     * 任何一页控制
     * <p>
     * 底栏
     */
    @JavascriptInterface
    public void popNewWindow(String page) {
//		essayReview.html?eid=E000074
//		"file:///android_asset/"
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", "file:///android_asset/" + page);
        intent.putExtra("open",true);
        context.startActivity(intent);

    }

    /**
     * ?tarid=M000021&role=coach&tabpage=course&memid=M000003&memphoto=http://
     * efithealthresource
     * .oss-cn-beijing.aliyuncs.com/image/bmember/M000021_1461571239735_s
     * .jpg&memnickname=羿健康男教练
     * <p>
     * 个人信息 "?tarid=" +memid +"&role="+memrole+"&memid="+memid
     */
    @JavascriptInterface
    public void memberIndex(String page) {
        /*if (page != null && page.length() > 0) {
			SharedPreferencesUtils.setParam(context, "page", page);
			Log.i("djy", page);
		}
//			Intent intent=new Intent(context,MemberCluMenubActivity.class);
//			intent.putExtra("url", page);
//			context.startActivity(intent);
		MainActivity.instance.setTabSelection(403);*/
        Toast.makeText(context, "web羿健康男教练", Toast.LENGTH_SHORT).show();
    }

    /**
     * 获得当前登录memid
     *
     * @return
     */
    @JavascriptInterface
    public String getmemid() {
        return SPUtils.getString(Constant.MEMID);
    }

    /**
     * HTML 页面提示
     *
     * @param content
     */
    @JavascriptInterface
    public void alertInfo(String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 提示信息
     *
     * @param type 提示类型:success成功，error错误，warn警告, message(无文本)
     */
    @JavascriptInterface
    public void alertInfo(String type, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 聊天
     *
     * @param memid
     * @param nickname
     * @param imgpath
     */
    @JavascriptInterface
    public void toChat(String memid, String nickname, String imgpath) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userId", memid.toLowerCase());
        intent.putExtra("nickname", nickname);
        intent.putExtra("headimg", imgpath);
        context.startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param phoneNumber 电话号码
     */
    @JavascriptInterface
    public void callUp(final String phoneNumber) {
        Toast.makeText(context, "web打电话", Toast.LENGTH_SHORT).show();

    }

    /**
     * 退出当前登录
     */
    @JavascriptInterface
    public void logout() {
//		context.startActivity(new Intent(context, ExitConfimActivity.class));
        Toast.makeText(context, "web退出当前登录", Toast.LENGTH_SHORT).show();
    }

    /**
     * 生成二维码
     */
    @JavascriptInterface
    public void getMyQrCode() {
		/*Intent intent = new Intent();
		intent.setClass(context, QRCodeActivity.class);
		context.startActivity(intent);*/
        Toast.makeText(context, "web生成二维码", Toast.LENGTH_SHORT).show();
    }

    /**
     * 课程管理
     *
     * @param memid
     */
    @JavascriptInterface
    public void courseManager(String memid) {
//		MainActivity.instance.setTabSelection(100);
        Toast.makeText(context, "web.课程管理", Toast.LENGTH_SHORT).show();
    }

    /**
     * 订单类型
     */
    @JavascriptInterface
    public void getOrderList() {
//		MainActivity.instance.setTabSelection(402);
        Toast.makeText(context, "web订单类型", Toast.LENGTH_SHORT).show();

    }

    @JavascriptInterface
    public void backExe() {
        Toast.makeText(context, "backExe", Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void Loading(boolean load) {
        if (load) {
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(),
                    "Loading");
        } else {
            if (dialog.isVisible() || dialog.isCancelable() || dialog.isResumed()) {
                //移除dialog
                dialog.dismissAllowingStateLoss();
            }
        }
    }


    @JavascriptInterface
    public void gotoBespeakList() {
        Toast.makeText(context, "gotoBespeakList", Toast.LENGTH_SHORT).show();
    }


}
