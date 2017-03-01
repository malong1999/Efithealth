package com.maxiaobu.healthclub.ui.weiget.refresh;


import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.maxiaobu.healthclub.R;
import com.nineoldandroids.view.ViewHelper;

import butterknife.Bind;

import static android.content.ContentValues.TAG;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.maxiaobu.healthclub.R.id.view;

/**
 * Created by 马小布 on 2016/8/1.
 */
public class RefreshHeaderView extends FrameLayout implements SwipeRefreshTrigger, SwipeTrigger {
    ImageView mIvArrow;
    ImageView mIvChrysanthemum;
    private View mRootView;
    private AnimationDrawable mHeaderChrysanthemumAd;
    int state=0;
    private RotateAnimation mRotateAnimation;
    private RotateAnimation mRotateAnimation1;

    public RefreshHeaderView(Context context) {
        super(context);

    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_pull_header, this);
        mIvArrow = (ImageView) mRootView.findViewById(R.id.iv_normal_refresh_header_arrow);
        mIvChrysanthemum= (ImageView) mRootView.findViewById(R.id.iv_normal_refresh_header_chrysanthemum);

        mHeaderChrysanthemumAd = (AnimationDrawable) mIvChrysanthemum.getDrawable();
        mRotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(200);
        mRotateAnimation.setFillAfter(true);

        mRotateAnimation1 = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation1.setDuration(200);
        mRotateAnimation1.setFillAfter(true);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {
        mIvArrow.clearAnimation();
        mIvArrow.setVisibility(GONE);
        mIvChrysanthemum.setVisibility(VISIBLE);
        mHeaderChrysanthemumAd.start();

    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

//        Log.d("RefreshHeaderView", "i:" + i);
        if (!b) {
            if (i >= getHeight()) {
                if (state != 1) {
                    mIvArrow.startAnimation(mRotateAnimation);
                    state=1;
                }
//                ViewHelper.setRotation(mIvArrow,180);
//                Log.d("RefreshHeaderView", "释放刷新yScrolled >= getHeight()");
            } else {
                if (state ==1) {
//                    Log.e(TAG, "onMove: 1"+ "haha");
                    mIvArrow.startAnimation(mRotateAnimation1);
                    state = 0;
                }
//                if (i>30&&(i-30)*1.5<=185)
//                ViewHelper.setRotation(mIvArrow, (float) ((i-30)*1.5));
//                Log.d("RefreshHeaderView", "下拉刷新yScrolled < getHeight()");
            }
        } else {
//            Log.d("RefreshHeaderView", "isComplete =true");
        }
    }

    @Override
    public void onRelease() {
//        Log.d("RefreshHeaderView", "onRelease");
    }

    @Override
    public void onComplete() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        RotateAnimation rotateAnimation = new RotateAnimation(180, 0, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        animationSet.setDuration(200);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        mIvArrow.startAnimation(animationSet);
        mIvArrow.setVisibility(VISIBLE);
        mIvChrysanthemum.setVisibility(INVISIBLE);
        mHeaderChrysanthemumAd.stop();
//        Log.d("RefreshHeaderView", "onComplete");
    }

    @Override
    public void onReset() {
//        ViewHelper.setRotation(mIvArrow,180);
        state = 0;


//        Log.d("RefreshHeaderView", "onReset");
    }

//
//    public RefreshHeaderView(Context context) {
//        super(context);
//    }
//    public RefreshHeaderView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//    @Override
//    public void onRefresh() {
//        setText("onRefresh...");
//    }
//    @Override
//    public void onPrepare() {
//        setText("onPrepare");
//    }
//    @Override
//    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
//        if (!isComplete) {
//            if (yScrolled >= getHeight()) {
//                setText("释放刷新yScrolled >= getHeight()");
//            } else {
//                setText("下拉刷新yScrolled < getHeight()");
//            }
//        } else {
//            setText("isComplete =true");
//        }
//    }
//    @Override
//    public void onRelease() {
//        setText("onRelease");
//    }
//    @Override
//    public void onComplete() {
//        setText("onComplete");
//    }
//    @Override
//    public void onReset() {
//        setText("onReset");
//    }
}
