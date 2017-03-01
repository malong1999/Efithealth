package com.maxiaobu.healthclub.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.utils.HealthUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/22.
 * 动态大图
 */
public class ImageCheckActivity extends AppCompatActivity {
    @Bind(R.id.pager)
    ViewPager mPager;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_image_check);
        ButterKnife.bind(this);
        String imageStr = getIntent().getStringExtra("image");
        mDialog = HealthUtil.getChrysanthemumDialog(this);
        mDialog.show();

        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView mImageCheck = new PhotoView(ImageCheckActivity.this);
                mImageCheck.enable();
                mImageCheck.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (imageStr != null) {
                    Glide.with(ImageCheckActivity.this).load(imageStr).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Toast.makeText(ImageCheckActivity.this, "图片加载异常", Toast.LENGTH_SHORT).show();
//                          mDialog.dismiss();
                            return false;
                        }

                        //这个用于监听图片是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mDialog.dismiss();
                            return false;
                        }
                    }).into(mImageCheck);
                } else {
                    Glide.with(ImageCheckActivity.this).load("http://pic33.nipic.com/20130916/3420027_192919547000_2.jpg").listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Toast.makeText(ImageCheckActivity.this, "图片加载异常", Toast.LENGTH_SHORT).show();
//                          mDialog.dismiss();
                            return false;
                        }

                        //这个用于监听图片是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mDialog.dismiss();
                            return false;
                        }
                    }).into(mImageCheck);
                }

                mImageCheck.setOnClickListener(view -> {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                });

                container.addView(mImageCheck);
                return mImageCheck;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

    }

}
