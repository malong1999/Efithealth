package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

/**
 * 引导页
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.banner_guide_background)
    BGABanner mBannerGuideBackground;
    @Bind(R.id.banner_guide_foreground)
    BGABanner mBannerGuideForeground;
    @Bind(R.id.tv_guide_skip)
    TextView mTvGuideSkip;
    @Bind(R.id.btn_guide_enter)
    Button mBtnGuideEnter;
    /**
     * 当前版本名
     */
    private String mAppVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);


        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 在界面可见时给背景Banner设置一个白色背景，避免滑动过程中两个Banner都设置透明度后能看到Launcher
        mBannerGuideBackground.setBackgroundResource(android.R.color.white);
    }

    public void initView() {

    }

    private void initEvent() {
        mBannerGuideForeground.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == mBannerGuideForeground.getItemCount() - 2) {
                    ViewCompat.setAlpha(mBtnGuideEnter, positionOffset);
                    ViewCompat.setAlpha(mTvGuideSkip, 1.0f - positionOffset);

                    if (positionOffset > 0.5f) {
                        mBtnGuideEnter.setVisibility(View.VISIBLE);
                        mTvGuideSkip.setVisibility(View.GONE);
                    } else {
                        mBtnGuideEnter.setVisibility(View.GONE);
                        mTvGuideSkip.setVisibility(View.VISIBLE);
                    }
                } else if (position == mBannerGuideForeground.getItemCount() - 1) {
                    mTvGuideSkip.setVisibility(View.GONE);
                    mBtnGuideEnter.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mBtnGuideEnter, 1.0f);
                } else {
                    mTvGuideSkip.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mTvGuideSkip, 1.0f);
                    mBtnGuideEnter.setVisibility(View.GONE);
                }
            }
        });

    }

    public void initData() {
        processLogic();
    }

    @OnClick({R.id.tv_guide_skip, R.id.btn_guide_enter})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_guide_skip:
            case R.id.btn_guide_enter:
                SPUtils.putBoolean("enter_guide", false);

                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                GuideActivity.this.finish();
                break;

            default:
                break;
        }
    }

    /**
     * 俩viewPage的切换逻辑
     */
    private void processLogic() {
        mBannerGuideBackground.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mBannerGuideForeground.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // 初始化方式1：通过传入数据模型并结合Adapter的方式初始化
        mBannerGuideBackground.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                ((ImageView) view).setImageResource((int) model);
            }
        });
        mBannerGuideBackground.setData(Arrays.asList(R.mipmap.uoko_guide_background_1,
                R.mipmap.uoko_guide_background_2, R.mipmap.uoko_guide_background_3), null);
        List<View> views = new ArrayList<>();// 初始化方式2：通过直接传入视图集合的方式初始化
        views.add(BGABannerUtil.getItemImageView(this, R.mipmap.uoko_guide_foreground_1));
        views.add(BGABannerUtil.getItemImageView(this, R.mipmap.uoko_guide_foreground_2));
        views.add(BGABannerUtil.getItemImageView(this, R.mipmap.uoko_guide_foreground_3));
        mBannerGuideForeground.setData(views);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }
}
