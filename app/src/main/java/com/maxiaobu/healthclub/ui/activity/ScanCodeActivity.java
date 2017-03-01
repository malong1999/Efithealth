package com.maxiaobu.healthclub.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterScanCardAty;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanWallet;
import com.maxiaobu.healthclub.utils.rx.RxBus;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.R.attr.data;

/**
 * Created by 马小布 on ${DATE}：${HOUR}:${MINUTE}.
 * email：maxiaobu1216@gmail.com
 * 功能：扫码
 * 伪码：
 * 待完成：
 */
public class ScanCodeActivity extends BaseAty implements QRCodeView.Delegate {
    private static final String TAG = ScanCodeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @Bind(R.id.zxingview)
    QRCodeView mQRCodeView;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.ly_text)
    TextView mLyText;
    @Bind(R.id.iv_photo)
    ImageView mIvPhoto;
    @Bind(R.id.iv_card)
    ImageView mIvCard;
    @Bind(R.id.iv_light)
    ImageView mIvLight;
    @Bind(R.id.activity_scan_code)
    FrameLayout mActivityScanCode;
    private boolean isLight;
    String mOrdno = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        mQRCodeView.startCamera();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        mQRCodeView.stopCamera();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String resultString) {
        vibrate();
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(this, "扫描失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        String s = resultString;
        if (s.length() > 19 && s.substring(0, 19).equals("interface://maccess")
                && (mLyText.getVisibility() != View.GONE
                || SPUtils.getString(Constant.MEMROLE).equals("coach"))) {
            //interface://maccess!checkAccessCode.do?code.clubid=C000001&code.resid=R000001&code.restype=site&code.memid=$memid&code.ordno=$ordno&code.longitude=$longitude&code.latitude=$latitude
            String p = resultString.replace("interface://", UrlPath.URL_BASE)
                    .replace("$memid", SPUtils.getString(Constant.MEMID))
                    .replace("$ordno", mOrdno)
                    .replace("$longitude", SPUtils.getString(Constant.LONGITUDE))
                    .replace("$latitude", SPUtils.getString(Constant.LATITUDE));
            String[] split = resultString.split("&");
            String[] clubid = split[0].split("=");
            SPUtils.putString(Constant.clubID, clubid[1]);
            Log.d(TAG, p);
            App.getRequestInstance().post(this, p, null, new RequestListener() {
                @Override
                public void requestSuccess(String s) {
                    Log.d("ScanCodeActivity", s);
                    LatLng latLng = new LatLng(Double.valueOf(SPUtils.getString(Constant.LATITUDE)), Double.valueOf(SPUtils.getString(Constant.LONGITUDE)));
                    CoordinateConverter converter = new CoordinateConverter();
                    converter.from(CoordinateConverter.CoordType.GPS);
                    // sourceLatLng待转换坐标
                    converter.coord(latLng);
                    LatLng desLatLng = converter.convert();
                    SPUtils.putString(Constant.clubLATITUDE, String.valueOf(desLatLng.latitude));
                    SPUtils.putString(Constant.clubLONGITUDE, String.valueOf(desLatLng.longitude));
                    try {
                        JSONObject object = new JSONObject(s);
                        String msgFlag = (String) object.get("msgFlag");
                        String msgContent = (String) object.get("msgContent");
                        if (msgFlag.equals("1")) {
                            new MaterialDialog.Builder(mActivity)
                                    .title("提示")
                                    .content(msgContent)
                                    .dismissListener(dialog ->
                                            ScanCodeActivity.this.finish()
                                    )
                                    .positiveText("确定")
                                    .onPositive((dialog1, which) -> dialog1.dismiss()).show();
                        } else {
                            new MaterialDialog.Builder(mActivity)
                                    .title("提示")
                                    .content("您不是此俱乐部会员，无法进入")
                                    .positiveText("取消")
                                    .negativeText("再试一次")
                                    .onNegative((dialog, which) -> ScanCodeActivity.this.recreate())
                                    .onPositive((dialog1, which) -> ScanCodeActivity.this.finish()).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void requestError(VolleyError volleyError, String s) {
                    Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                    new MaterialDialog.Builder(mActivity)
                            .title("接口失败了")
                            .content(s)
                            .neutralText("kjhkj")
                            .dismissListener(dialog -> {
                                ScanCodeActivity.this.finish();
                            })
                            .show();
                }

                @Override
                public void noInternet(VolleyError volleyError, String s) {
                    Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                }
            });
        }else if ("123".equals(s)){
            //若扫码内容为123进入商品详情
            startActivity(new Intent(mActivity,GoodsInfoActivity.class));
            ScanCodeActivity.this.finish();
        }

        else {
            String scanResult = resultString;
            try {
                String[] str = scanResult.split("//");
                if (str[0].equals("interface:")) {
                    String url = UrlPath.URL_BASE + str[1] + "&memid=" + SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID);
                    Log.d("HomeActivity", url);
                    Observable<JsonObject> observable = App.getRetrofitUtil().getRetrofit().getStringScan(url);
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<JsonObject>() {
                                @Override
                                public void onCompleted() {
                                    Log.d("HomeActivity", "onCompleted");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
                                    Log.d("HomeActivity", e.toString());
                                }

                                @Override
                                public void onNext(JsonObject object) {
                                    String msgFlag = String.valueOf(object.get("msgFlag"));
                                    String msgContent = String.valueOf(object.get("msgContent"));
//                                    Log.d("HomeActivity", msgContent);
//                                    Log.d("HomeActivity", msgFlag);
                                    if (msgFlag.equals("\"1\"")) {
                                        Intent intent = new Intent(ScanCodeActivity.this, MyCardActivity.class);
//                                        startActivity(intent);
                                        intent.putExtra("san2order", true);
                                        startActivity(intent);
                                        ScanCodeActivity.this.finish();
                                    } else if (msgFlag.equals("\"0\"")) {
                                        if (msgContent != null) {
                                            Toast.makeText(ScanCodeActivity.this, msgContent, Toast.LENGTH_SHORT).show();
                                            ScanCodeActivity.this.finish();
                                        }
                                    }
                                }
                            });
                } else if (str[0].equals("http:")) {
                    Intent intent1 = new Intent(this, WebActivity.class);
                    intent1.putExtra("url", scanResult);
                    startActivity(intent1);
                    ScanCodeActivity.this.finish();
                } else if (str[0].equals("member:")) {
                    Toast.makeText(this, "社交功能即将开放", Toast.LENGTH_SHORT).show();
                    ScanCodeActivity.this.finish();
                }else {
                    Toast.makeText(mActivity, resultString, Toast.LENGTH_SHORT).show();
                    mActivity.finish();
                }
            } catch (NullPointerException e) {
                Toast.makeText(this, "二维码错误", Toast.LENGTH_SHORT).show();
                ScanCodeActivity.this.finish();
            }
        }
//        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mQRCodeView.startSpot();
    }


    @Override
    public void initView() {
        mQRCodeView.setDelegate(this);
        mIvBack.setOnClickListener(v1 -> {
            Intent intent = new Intent();
            intent.putExtra("data", "");
            setResult(1, intent);
            finish();
        });
        mIvPhoto.setOnClickListener(v -> startActivityForResult(BGAPhotoPickerActivity.newIntent(mActivity, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY));

        mIvLight.setOnClickListener(v -> {
            if (!isLight) {
                mQRCodeView.openFlashlight();
            } else {
                mQRCodeView.closeFlashlight();
            }
            isLight = !isLight;
        });
    }

    @Override
    public void initData() {
        App.getRequestInstance().post(UrlPath.URL_WALLET, this
                , new RequestParams("memid", SPUtils.getString(Constant.MEMID)), new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
//                        Log.d("ScanCodeActivity", s);
                        BeanWallet object = JsonUtils.object(s, BeanWallet.class);
                        if (object.getCardList().size() != 0) {
                            mLyText.setText(Html.fromHtml("扫描门禁时，将默认使用<font color='#Fb8435'>「"
                                    + object.getCardList().get(0).getCoursename() + "」</font>。或点击卡包更换"));
                            mOrdno = object.getCardList().get(0).getOrdno();
                        } else if (object.getCorderList().size() != 0) {
                            mLyText.setText(Html.fromHtml("扫描门禁时，将默认使用<font color='#Fb8435'>「"
                                    + object.getCorderList().get(0).getCoursename() + "」</font>。或点击卡包更换"));
                            mOrdno = object.getCorderList().get(0).getOrdno();
                        } else {
                            mLyText.setVisibility(View.GONE);
                            mOrdno = "-1";
                        }
                        mIvCard.setOnClickListener(v -> {
                            Intent intent = new Intent();
                            intent.putExtra("data", s);
                            intent.setClass(mActivity, ScanCardActivity.class);
                            startActivity(intent);
                        });
                    }

                    @Override
                    public void requestError(VolleyError volleyError, String s) {
                        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noInternet(VolleyError volleyError, String s) {
                        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                    }
                });

        RxBus rxBus = App.getRxBusSingleton();
        CompositeSubscription subscriptions = new CompositeSubscription();
        ConnectableObservable<Object> tapEventEmitter = rxBus.asObservable().publish();
        subscriptions//
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof AdapterScanCardAty.TapEvent) {
                        AdapterScanCardAty.TapEvent tapEvent = (AdapterScanCardAty.TapEvent) event;
                        if (tapEvent.getData() != null) {
                            mLyText.setText(Html.fromHtml("扫描门禁时，将默认使用<font color='#Fb8435'>「"
                                    + tapEvent.getData().getCoursename() + "」</font>。或点击卡包更换"));
                            mOrdno = tapEvent.getData().getOrdno();
                        } else if (tapEvent.getData1() != null) {
                            mLyText.setText(Html.fromHtml("扫描门禁时，将默认使用<font color='#Fb8435'>「"
                                    + tapEvent.getData1().getCoursename() + "」</font>。或点击卡包更换"));
                            mOrdno = tapEvent.getData1().getOrdno();
                        }
                    }
                }));
        subscriptions.add(tapEventEmitter.connect());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mQRCodeView.showScanRect();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
//            Toast.makeText(mActivity, picturePath, Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                String s = QRCodeDecoder.syncDecodeQRCode(picturePath);
                runOnUiThread(() -> Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show());

            }).start();

        }
    }
}
