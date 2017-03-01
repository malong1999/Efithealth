package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanFoodDetail;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.DialogAction;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import maxiaobu.mxbutilscodelibrary.ScreenUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：配餐详情
 * 伪码：
 * 待完成：
 */
public class CateringDetailActivity extends BaseAty implements
        AppBarLayout.OnOffsetChangedListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {
    @Bind(R.id.iv_food)
    ImageView mIvFood;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_name)
    CollapsingToolbarLayout mCtlName;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.tv_food_name)
    TextView mTvFoodName;
    @Bind(R.id.tv_food_price)
    TextView mTvFoodPrice;
    @Bind(R.id.tv_food_sales)
    TextView mTvFoodSales;
    @Bind(R.id.tv_base_info)
    TextView mTvBaseInfo;
    @Bind(R.id.tv_material)
    TextView mTvMaterial;
    @Bind(R.id.iv_phone)
    ImageView mIvPhone;
    @Bind(R.id.tv_phone_num)
    TextView mTvPhoneNum;
    @Bind(R.id.rl_call_phone)
    RelativeLayout mRlCallPhone;
    @Bind(R.id.iv_food_detail)
    ImageView mIvFoodDetail;
    @Bind(R.id.rl_food_detail)
    RelativeLayout mRlFoodDetail;
    @Bind(R.id.tv_base_notice)
    TextView mTvBaseNotice;
    @Bind(R.id.tv_content_notice)
    TextView mTvContentNotice;
    @Bind(R.id.rl_order_notice)
    LinearLayout mRlOrderNotice;
    @Bind(R.id.ll_root_content)
    LinearLayout mLlRootContent;
    @Bind(R.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @Bind(R.id.iv_reduce)
    ImageView mIvReduce;
    @Bind(R.id.tv_food_num)
    TextView mTvFoodNum;
    @Bind(R.id.iv_add)
    ImageView mIvAdd;
    @Bind(R.id.tv_add_cart)
    TextView mTvAddCart;
    @Bind(R.id.tv_now_order)
    TextView mTvNowOrder;
    @Bind(R.id.root_layout)
    CoordinatorLayout mRootLayout;
    private BeanFoodDetail.BFoodmerBean mData;
    private int mFoodNum;
    private String mMerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_detail);
        ButterKnife.bind(this);
        mFoodNum = 1;
        initView();
        initData();
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAppBar.addOnOffsetChangedListener(this);
        mMerid = getIntent().getStringExtra("merid");
        mToolbar.setTitle("");
        mCtlName.setTitle("");
        CoordinatorLayout.LayoutParams linearParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
        int width = ScreenUtils.getScreenWidth(this);
        linearParams.height = width / 22 * 15;
        mAppBar.setLayoutParams(linearParams);
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String url = getIntent().getStringExtra("url");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(mActivity).load(url)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .placeholder(App.getInstance().getDrawableHolder()).into(mIvFood);
            mTvFoodName.setText(name);
            mTvFoodPrice.setText(price + "元");
        } else {
            Glide.with(mActivity).load(url)
                    .placeholder(R.mipmap.ic_place_holder_cater)
                    .into(mIvFood);
        }
    }

    @Override
    public void initData() {
        Observable<JsonObject> jsonGetFoodmers = App.getRetrofitUtil().getRetrofit().getJsonGetFoodmers(mMerid);
        jsonGetFoodmers.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            new MaterialDialog.Builder(mActivity)
                                    .title("网络加载失败")
                                    .negativeColor(Color.parseColor("#Fb8435"))
                                    .positiveColor(Color.parseColor("#Fb8435"))
                                    .content("请检查网络设置")
                                    .negativeText("刷新")
                                    .positiveText("设置")
                                    .onPositive((dialog, which) -> NetworkUtils.openWirelessSettings(mActivity))
                                    .onNegative((dialog, which) -> {
                                        dialog.dismiss();
                                        initData();
                                    }).show();
                        } catch (AndroidRuntimeException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d("CateringDetailActivity", jsonObject.toString());
                        BeanFoodDetail object = new Gson().fromJson(jsonObject, BeanFoodDetail.class);
                        mData = object.getBFoodmer();
                        mTvFoodSales.setText(mData.getCreatetime().getDay() + "天已售" + mData.getSalenum() + "份");
                        mTvBaseInfo.setText(mData.getEnergydescr());
                        mTvMaterial.setText(mData.getCompodescr());
                        mTvPhoneNum.setText(mData.getFplatConphone());
                        mTvContentNotice.setText(mData.getOrdernotice());
                    }
                });
    }

    @OnClick({R.id.rl_call_phone, R.id.iv_reduce, R.id.iv_add,
            R.id.tv_add_cart, R.id.tv_now_order, R.id.rl_food_detail})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //打电话
            case R.id.rl_call_phone:
                ringUp();
                break;
            //减号
            case R.id.iv_reduce:
                if (mFoodNum > 1) {
                    mFoodNum--;
                    mTvFoodNum.setText(String.valueOf(mFoodNum));
                }
                break;
            //加号
            case R.id.iv_add:
                mFoodNum++;
                mTvFoodNum.setText(String.valueOf(mFoodNum));
                break;
            case R.id.tv_add_cart:
                // TODO: 2016/8/23 没写
                break;
            //立即下单
            case R.id.tv_now_order:
                if (mData != null) {
                    Intent intent = new Intent(this, CateringOrderConfirmActivity.class);
                    intent.putExtra("price", Integer.parseInt(mData.getMerprice()));
                    intent.putExtra("num", Integer.parseInt(mTvFoodNum.getText().toString()));
                    intent.putExtra("merid", mMerid);
                    intent.putExtra("phoneNum", mTvPhoneNum.getText());
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(mTvNowOrder,
                            v.getWidth() / 2, v.getHeight() / 2, v.getWidth() / 2, v.getHeight() / 2);
                    ActivityCompat.startActivity(this, intent,
                            compat.toBundle());
                }
                break;
            case R.id.rl_food_detail:
                Toast.makeText(this, "尽请期待", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * toolbar滑动监听
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset <= -mCtlName.getHeight() + mToolbar.getHeight() + 180) {
            if (mData != null)
                mCtlName.setTitle(mData.getMername());
        } else {
            mCtlName.setTitle("");
        }
    }

    /**
     * 打电话确认
     */
    @AfterPermissionGranted(122)
    private void ringUp() {
        new MaterialDialog.Builder(this)
                .title("呼叫")
                .content(mTvPhoneNum.getText())
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    if (EasyPermissions.hasPermissions(CateringDetailActivity.this, Manifest.permission.CALL_PHONE)) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + mTvPhoneNum.getText());
                        intent.setData(data);
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    } else {
                        EasyPermissions.requestPermissions(CateringDetailActivity.this, "需要打电话的权限",
                                122, Manifest.permission.CALL_PHONE);//让easyPermission去请求权限
                    }
                })
                .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * 没有需要设置的地方，复制就行，但必须有
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions   将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 用户给权限了
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Log.i(TAG, "onPermissionsDenied: "+ requestCode + ":" + perms.size());
    }

    /**
     * 权限被拒绝胃
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "点设置---》权限---》访问相机的权限给我", R.string.setting, R.string.cancel, perms);
    }
}
