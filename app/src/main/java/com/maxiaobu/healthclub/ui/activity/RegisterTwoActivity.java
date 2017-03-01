package com.maxiaobu.healthclub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMrsendCode;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：注册第二篇
 * 伪码：
 * 待完成：
 */
public class RegisterTwoActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.et_config_password)
    EditText mEtConfigPassword;
    @Bind(R.id.et_nike_name)
    EditText mEtNikeName;
    @Bind(R.id.et_gender)
    EditText mEtGender;
    @Bind(R.id.bt_go)
    Button mBtGo;
    @Bind(R.id.cv_add)
    CardView mCvAdd;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.til_gender)
    TextInputLayout mTilGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    animateRevealClose();
                }else {
                    onBackPressed();
                }

            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_go, R.id.ly_gender})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_go:
//                RegisterActivity.this.finish();
//                startActivity(new Intent(RegisterActivity.this,RegisterTwoActivity.class));
                regist();
                break;
            case R.id.ly_gender:
                final String[] genders = new String[]{"男", "女"};
                new MaterialDialog.Builder(this)
                        .items(genders)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mEtGender.setText(genders[which]);
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            animateRevealClose();
        }else {
            super.onBackPressed();
        }

    }

    private void ShowEnterAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
            getWindow().setSharedElementEnterTransition(transition);

            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    mCvAdd.setVisibility(View.GONE);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        transition.removeListener(this);
                    }

                    animateRevealShow();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }

    }

    public void animateRevealShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mFab.getWidth() / 2, mCvAdd.getHeight());
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    mCvAdd.setVisibility(View.VISIBLE);
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
    }

    public void animateRevealClose() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mCvAdd.getHeight(), mFab.getWidth() / 2);
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCvAdd.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    mFab.setImageResource(R.mipmap.ic_plus);
                    RegisterTwoActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
    }

    private void regist() {
        if (TextUtils.isEmpty(mEtPassword.getText().toString().trim())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mEtConfigPassword.getText().toString().trim())) {
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mEtNikeName.getText().toString().trim())) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mEtGender.getText().toString().trim())) {
            Toast.makeText(this, "请输入性别", Toast.LENGTH_SHORT).show();
        } else if (!mEtPassword.getText().toString().trim().equals(mEtConfigPassword.getText().toString().trim())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            String gender;
            if (mEtGender.getText().toString().trim().equals("女")) {
                gender = "0";
            } else {
                gender = "1";
            }
            final String userPhone = getIntent().getStringExtra("mobphone");
            RequestParams params = new RequestParams();
            params.put("mempass", mEtPassword.getText().toString().trim());
            params.put("nickname", mEtNikeName.getText().toString().trim());
            params.put("gender", gender);
            params.put("mobphone", userPhone);
            App.getRequestInstance().post(this, UrlPath.URL_REGISTER, params, new RequestListener() {
                @Override
                public void requestSuccess(String s) {
                    BeanMrsendCode data = JsonUtils.object(s, BeanMrsendCode.class);
//                    data.getMsgFlag();
                    Toast.makeText(RegisterTwoActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
                    if ("1".equals(data.getMsgFlag())) {
                        RegisterTwoActivity.this.setResult(1);
                        animateRevealClose();
                    } else {
                        Toast.makeText(RegisterTwoActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void requestError(VolleyError volleyError, String s) {

                }

                @Override
                public void noInternet(VolleyError volleyError, String s) {

                }

            });
        }
    }


    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 隐藏软键盘
    private void hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
