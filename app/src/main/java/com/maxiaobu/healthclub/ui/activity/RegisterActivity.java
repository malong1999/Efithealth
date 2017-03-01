package com.maxiaobu.healthclub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.mapapi.http.AsyncHttpClient;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMlogin;
import com.maxiaobu.healthclub.common.beangson.BeanMrsendCode;
import com.maxiaobu.healthclub.service.SMSContentObserver;
import com.maxiaobu.healthclub.utils.RegularUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：注册
 * 伪码：
 * 待完成：
 */
public class RegisterActivity extends BaseAty
        implements View.OnClickListener ,EasyPermissions.PermissionCallbacks{
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_username)
    EditText mEtUsername;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_get_code)
    TextView mTvGetCode;
    @Bind(R.id.et_repeatpassword)
    EditText mEtRepeatpassword;
    @Bind(R.id.bt_go)
    Button mBtGo;
    @Bind(R.id.cv_add)
    CardView mCvAdd;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initData();
        moxiaoting();
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

    @OnClick({R.id.bt_go, R.id.tv_get_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_go:
//                RegisterActivity.this.finish();
//                startActivity(new Intent(RegisterActivity.this,RegisterTwoActivity.class));
                if (!RegularUtils.isMobile(mEtUsername.getText().toString().trim())) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEtCode.getText().toString().trim())) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    iscodeok(mEtUsername.getText().toString().trim(), mEtCode.getText().toString().trim());
                }
                break;
            case R.id.tv_get_code:
                if (RegularUtils.isMobile(mEtUsername.getText().toString().trim())) {
                    tvSmsCaptchaCountDown(RegisterActivity.this, mTvGetCode, 60);
                    getCode(mEtUsername.getText().toString().trim());
                } else {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
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
                    RegisterActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
    }

    /**
     * 短信验证码倒计时
     */
    public void tvSmsCaptchaCountDown(final Context context, final TextView tv, int smsTime) {
        tv.setOnClickListener(null);
        tv.setActivated(false);
        tv.setClickable(false);
        new CountDownTimer(smsTime * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv.setText("重新获取(" + millisUntilFinished / 1000 + ")");
            }

            public void onFinish() {
                tv.setClickable(true);
                tv.setText("获取验证码");
                tv.setActivated(true);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (RegularUtils.isMobile(mEtUsername.getText().toString().trim())) {
                            tvSmsCaptchaCountDown(RegisterActivity.this, mTvGetCode, 60);
                            getCode(mEtUsername.getText().toString().trim());
                        } else {
                            Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 获取验证码
     */
    private void getCode(final String userPhone) {
        RequestParams params = new RequestParams();
        params.put("mobphone", userPhone);
        App.getRequestInstance().post(this, UrlPath.URL_SENDCODE, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
//                Log.d("LoginActivity", s);
                BeanMrsendCode data = JsonUtils.object(s, BeanMrsendCode.class);
                data.getMsgFlag();
                Toast.makeText(RegisterActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
                if (!"1".equals(data.getMsgFlag())) {
                    // TODO: 2016/9/5 填写验证码
                } else {
                    Toast.makeText(RegisterActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
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

    /**
     * 验证验证码
     *
     * @param code 验证码
     */
    public void iscodeok(final String userPhone, final String code) {
        RequestParams params = new RequestParams();
        params.put("mobphone", userPhone);
        params.put("identcode", code);
        App.getRequestInstance().post(this, UrlPath.URL_SENDCODE_CHECK, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanMrsendCode data = JsonUtils.object(s, BeanMrsendCode.class);
//                data.getMsgFlag();
                Toast.makeText(RegisterActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
                if ("1".equals(data.getMsgFlag())) {
                    Intent intent = new Intent();
                    intent.putExtra("mobphone", userPhone);
                    intent.setClass(RegisterActivity.this, RegisterTwoActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, mFab, mFab.getTransitionName());
                        startActivityForResult(intent, 1, options.toBundle());
                    } else {
                        startActivityForResult(intent, 1);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            RegisterActivity.this.setResult(1);
            animateRevealClose();
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
            v.getLocationInWindow(l);//获取view在整个窗口内的绝对坐标
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            //用户点击在view内。
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

    private SMSContentObserver smsContentObserver;

    private void moxiaoting() {

        if(EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_SMS)){
            smsContentObserver = new SMSContentObserver(this, handler);
            //注册内容观察者
            registerContentObservers() ;
        }else {
            EasyPermissions.requestPermissions(this, "验证码需要短信权限",
                    122, android.Manifest.permission.READ_SMS);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功回调
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        smsContentObserver = new SMSContentObserver(this, handler);
        //注册内容观察者
        registerContentObservers() ;
    }

    //失败回调
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "需要权限，请点设置--->权限",R.string.setting,R.string.cancel,list);
    }


    private void registerContentObservers() {
        // ”表“内容观察者 ，通过测试我发现只能监听此Uri -----> content://sms
        // 监听不到其他的Uri 比如说 content://sms/outbox
        Uri smsUri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(smsUri, true,smsContentObserver);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String outbox = (String) msg.obj;
            Log.e("++", outbox);
            mEtCode.setText(outbox);
        }
    };
}
