package com.maxiaobu.healthclub.service;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 莫小婷 on 2016/12/1.
 * 短信自动填写
 */
public class SMSContentObserver extends ContentObserver {
    private int MSG_OUTBOXCONTENT = 2;
    //发送验证码的号码
    private static final String VERIFY_CODE_FROM = "10690129887184";
    //4位纯数字验证码
    private static final String PATTERN_CODER = "(?<!\\d)\\d{4}(?!\\d)";

    private Context mContext;
    private Handler mHandler;   //更新UI线程

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SMSContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法
     *
     * @param selfChange 此值意义不大 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Uri inBoxUri = Uri.parse("content://sms");
        Cursor c = mContext.getContentResolver().query(inBoxUri, null,
                null, null, "date desc");
        if (c != null) {
            //只取最新的一条短信
            Log.e("++", "c.getCount():" + c.getCount());
            while (c.moveToNext()) {
                String number = c.getString(c.getColumnIndex("address"));//手机号
                Log.e("++", "number:" + number);
                String body = c.getString(c.getColumnIndex("body"));
//                String read = c.getString(c.getColumnIndex("read"));
                if (number.equals(VERIFY_CODE_FROM)) {
                    String verifyCode = patternCode(body);
                    Log.e("++", verifyCode);
                    Message msg = Message.obtain();
                    msg.what = MSG_OUTBOXCONTENT;
                    msg.obj = verifyCode;
                    mHandler.sendMessage(msg);
                    break;
                }
            }

            c.close();
        }
    }

    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(PATTERN_CODER);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
