package com.maxiaobu.healthclub;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.utils.pay.PayResult;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：测试用的Activity没用
 * 伪码：
 * 待完成：
 */
public class MainActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.bt)
    Button mBt;
    @Bind(R.id.iv)
    ImageView mIv;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Log.d("MainActivity", "SDK_PAY_FLAG");
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    Log.d("MainActivity", payResult.getResult());
                     /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.d("MainActivity", resultInfo);
                    Log.d("MainActivity", resultStatus);
//                    // 判断resultStatus 为9000则代表支付成功
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    Log.d("MainActivity", "SDK_AUTH_FLAG");
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        // 其他状态值则为授权失败
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
//
//                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();


    }

    @Override
    public void initView() {
        int a=0;
        for (int i = 0; i < 1000; i++) {
            a+=i;
        }

//        mBt.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

//    private Bitmap generateBitmap(String content, int width, int height) {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        Map<EncodeHintType, String> hints = new HashMap<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        try {
//            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            int[] pixels = new int[width * height];
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    if (encode.get(j, i)) {
//                        pixels[i * width + j] = 0xff000000;
//                    } else {
//                        pixels[i * width + j] = 0x00eeeeee;
//                    }
//                }
//            }
//            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
//        int qrBitmapWidth = qrBitmap.getWidth();
//        int qrBitmapHeight = qrBitmap.getHeight();
//        int logoBitmapWidth = logoBitmap.getWidth();
//        int logoBitmapHeight = logoBitmap.getHeight();
//        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(blankBitmap);
//        canvas.drawBitmap(qrBitmap, 0, 0, null);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        float scaleSize = 1.0f;
//        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
//            scaleSize *= 2;
//        }
//        float sx = 1.0f / scaleSize;
//        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
//        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
//        canvas.restore();
//        return blankBitmap;
//    }

    @OnClick({R.id.bt})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(MainActivity.this);
//                        Map<String, String> result = alipay.payV2("app_id=2017021105623392&biz_content={\"button\":[{\"actionParam\":\"ZFB_HFCZ\",\"actionType\":\"out\",\"name\":\"话费充值\"},{\"name\":\"查询\",\"subButton\":[{\"actionParam\":\"ZFB_YECX\",\"actionType\":\"out\",\"name\":\"余额查询\"},{\"actionParam\":\"ZFB_LLCX\",\"actionType\":\"out\",\"name\":\"流量查询\"},{\"actionParam\":\"ZFB_HFCX\",\"actionType\":\"out\",\"name\":\"话费查询\"}]},{\"actionParam\":\"http://m.alipay.com\",\"actionType\":\"link\",\"name\":\"最新优惠\"}]}&charset=UTF-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2014-07-24 03:07:50&version=1.0&" +
//                                 "sign=JmARJZ5ovm3IRHtA0AQqy7AQUEbQA7i5oS+g3zRspBYtdfnXOVQ1PJ0hw0RTFL7cjmzfPxxrgZw7uRR41sph6Db7iDNm+kaypQCOrnfm2LHvpv0lnqwYcsAs9QJHzVRI3Ff2BfsoKFwP/lEw1wW39O7SpfpI2K5QDlCct7WhHsqDUTMg2DvRWYc1DVFOjF1MiK/facLohIhef4z5p/ZddUI5krGp0D8Ut5cft/uG0tckAYpPFERaMwY40HPcoMVUbnDi96DjzNbfcx+H/A51gdD/mJgVO5vJk19OIZnSGGEvnbCPAbkdeaK6OcfdprV2KmHJQohX7M8Gmly1agkWyA==", true);
//                        Map<String, String> result = alipay.payV2("biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22021716243813493%22%7D&method=alipay.trade.app.pay&charset=utf-8&version=1.0&app_id=2017021105623392&timestamp=2016-07-29+16%3A55%3A53&sign_type=RSA2"
//                              +"&"  + "sign=bkln54P51nfBHDgjGWUGkqGEiBYCN%2B%2BNAi9yRzjqUZVn248367G%2FtQMjYBPiBm%2Bd9JYRbI7O38MJ9taKUt8nyB8FHTsch%2BeFuSYL4gYIUJ3URslZ1mCz6nMyvrr0lj5BdoBmOYWwbMpHDnHZk2Dl77Q%2BfWwnDOX0ziJdU5W5zQaN0TsAEDEDbL4Krgz94wSY3FIwrCsPzpo6qlyGgxmCS%2Fgmq1Sg31ZElH2vQFIIUTnMJhCGrt5vgsZYAG9bAdnU%2B2SdPxDrhzrFvYohPWGZAVMYwGK%2FMQ0qC3KR6frur6Pgj%2FM4UKzV6mMYblO7ck9df8TSmZigYMY24FFAZEn90A%3D%3D", true);
//                        bkln54P51nfBHDgjGWUGkqGEiBYCN%2B%2BNAi9yRzjqUZVn248367G%2FtQMjYBPiBm%2Bd9JYRbI7O38MJ9taKUt8nyB8FHTsch%2BeFuSYL4gYIUJ3URslZ1mCz6nMyvrr0lj5BdoBmOYWwbMpHDnHZk2Dl77Q%2BfWwnDOX0ziJdU5W5zQaN0TsAEDEDbL4Krgz94wSY3FIwrCsPzpo6qlyGgxmCS%2Fgmq1Sg31ZElH2vQFIIUTnMJhCGrt5vgsZYAG9bAdnU%2B2SdPxDrhzrFvYohPWGZAVMYwGK%2FMQ0qC3KR6frur6Pgj%2FM4UKzV6mMYblO7ck9df8TSmZigYMY24FFAZEn90A%3D%3D
                        Map<String, String> result = alipay.payV2("app_id=2017021105623392&biz_content={\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"0216164024-1334\"}&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2016-07-29  16:55:53&version=1.0&sign=CyPGguJaCP3w05TWSFnnAtll%2FCBRDqzAs9C%2FUhVOdhVHNS8lapqjrg2IruDij3CyBfXcpcl5%2FV6rUVOGkBEUlUhK6Z%2F3aYcoQmmmwDoKKMy1I6Xp44wj1lwfpBpWgXvDrRy8HJMEm%2FaESwOiqwrEZqi5ZhZpONX2QrhEc3KxtbGNepNPH1wapLtgiwtP5waRG0YyGkZZ74Zow%2F8Zoy80nfH%2B7oP3AsOivGozohJSkh4rEt0O6AZOQnaGPtMQ%2BEkBCoyKI0R562QEVFGQEMkp1VxkGcNGmvCHdBOsgvCrHo0xMbBQeBjRCN0l%2FxDLZ75szm1QanrXzvHfbBYso1Ahkw%3D%3D", true);
//                        Map<String, String> result = alipay.payV2("biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22021716243813493%22%7D&method=alipay.trade.app.pay&charset=utf-8&version=1.0&app_id=2017021105623392&timestamp=2016-07-29+16%3A55%3A53&sign_type=RSA2"
//                                +"&"  + "sign=bkln54P51nfBHDgjGWUGkqGEiBYCN%2B%2BNAi9yRzjqUZVn248367G%2FtQMjYBPiBm%2Bd9JYRbI7O38MJ9taKUt8nyB8FHTsch%2BeFuSYL4gYIUJ3URslZ1mCz6nMyvrr0lj5BdoBmOYWwbMpHDnHZk2Dl77Q%2BfWwnDOX0ziJdU5W5zQaN0TsAEDEDbL4Krgz94wSY3FIwrCsPzpo6qlyGgxmCS%2Fgmq1Sg31ZElH2vQFIIUTnMJhCGrt5vgsZYAG9bAdnU%2B2SdPxDrhzrFvYohPWGZAVMYwGK%2FMQ0qC3KR6frur6Pgj%2FM4UKzV6mMYblO7ck9df8TSmZigYMY24FFAZEn90A%3D%3D", true);
//                                                 BFu0eaB8HIHUVqcUmSYgqzi3XdwtyaGzQXV6GXslA4UR33FsPXWbOh%2FGq81wE1DFwIGSqTrNHaN2kii66XaFDjOtj%2F8mIkrZGRtg7wjtbJYBPDvoUIWvUmcaS%2B0QwjICXFLTqgxNgpJ4z53bIoXPyjKFbJjm44xJybIN7l7r5UMb%2BaO8%2BbgvI1idNtAsuCoR4k4FtzEoA6l%2Bl8%2FgycZ%2FHc0bsd1NOMSwT69Cf6xPFcEDYUI9DKfEf3eKgajrLna5ZFcGWp%2FT%2B0AbkzCBGPfH0gMMhKLxDMxIEnSHeF6kyWc%2FkMnmakifkXzEhYXlAYqQdfACxJrkl7Ym%2FZrUAubFEA%3D%3D
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();

                break;
            default:
                break;
        }



    }
}
