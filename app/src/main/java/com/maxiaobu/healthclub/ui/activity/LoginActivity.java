package com.maxiaobu.healthclub.ui.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.IccOpenLogicalChannelResponse;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.DemoDBManager;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanMlogin;
import com.maxiaobu.healthclub.utils.HealthUtil;
import com.maxiaobu.healthclub.utils.RegularUtils;
import com.maxiaobu.healthclub.utils.SystemUtils;
import com.maxiaobu.healthclub.utils.VibratorUtil;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import javax.crypto.Mac;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 登录
     */
    public static final int REGISTER = 1;
    /**
     * 找回密码
     */
    public static final int FIND_PASSWORD = 2;
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.til_password)
    TextInputLayout mTilPassword;
    @Bind(R.id.til_user_name)
    TextInputLayout mTilUserName;
    @Bind(R.id.ly_root)
    RelativeLayout mLyRoot;
    private boolean progressShow;//加载中
    @Bind(R.id.et_username)
    TextInputEditText mEtUsername;
    @Bind(R.id.et_password)
    TextInputEditText mEtPassword;
    @Bind(R.id.bt_go)
    Button mBtGo;
    @Bind(R.id.tv_forget_password)
    TextView mTvForgetPassword;
    @Bind(R.id.cv)
    CardView mCv;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    Subscription mSubscription = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    public void initView() {
        Observable<CharSequence> userNameObservable = RxTextView.textChanges(mEtUsername).skip(1);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(mEtPassword).skip(1);
        mSubscription = Observable.combineLatest(userNameObservable, passwordObservable, (newUserName, newPassword) -> {
            mBtGo.setOnClickListener(v -> {
                Toast.makeText(this, "请输入正确的用户名及密码", Toast.LENGTH_SHORT).show();
            });
            VibratorUtil.Vibrate(LoginActivity.this, 30);
            boolean userNameValid = !isEmpty(newUserName) &&
                    RegularUtils.isMobile(newUserName.toString());
            if (!isEmpty(newUserName) && !RegularUtils.isMatch("^\\d{0,11}$", newUserName.toString())) {
                mTilUserName.setHint("请输入正确的手机号码");
                mTilUserName.setHintTextAppearance(R.style.RedText);

            } else {
                mTilUserName.setHint("用户名");
                mTilUserName.setHintTextAppearance(R.style.GreenText);
            }
            boolean passwordValid = !isEmpty(newPassword) &&
                    RegularUtils.isMatch("^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$", newPassword.toString());
            if (!isEmpty(newPassword) && !RegularUtils.isMatch("^[\\w\\u4e00-\\u9fa5]{0,20}(?<!_)$", newPassword.toString())) {
                mTilPassword.setHint("输入了无效密码");
                mTilPassword.setHintTextAppearance(R.style.RedText);
            } else {
                mTilPassword.setHint("密码");
                mTilPassword.setHintTextAppearance(R.style.GreenText);
            }
            return userNameValid && (newUserName.toString().equals("18624616670") || passwordValid);
        }).filter(aBoolean -> aBoolean)
                .subscribe(aBoolean -> {
                    mBtGo.setOnClickListener(v -> {
                        if (NetworkUtils.isConnected(LoginActivity.this)) {
                            login(mEtUsername.getText().toString().trim(), mEtPassword.getText().toString().trim());
                        } else {
                            HealthUtil.showNONetworkDialog(LoginActivity.this);
                        }
                    });
                });

        //用户名变删除已输入密码
        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEtPassword.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //如果有用户名放上
        String userId = SPUtils.getString(Constant.USER_ID);
        if (userId != null) {
            mEtUsername.setText(userId);
        }
    }

    public void initData() {
    }

    /**
     * 设置点击事件
     *
     * @param v
     */
    @OnClick({R.id.bt_go, R.id.fab, R.id.tv_forget_password})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //fab点击
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivityForResult(new Intent(this, RegisterActivity.class), REGISTER, options.toBundle());
                } else {
                    startActivityForResult(new Intent(this, RegisterActivity.class), REGISTER);
                }
                break;
            //忘记密码点击事件
            case R.id.tv_forget_password:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivityForResult(new Intent(this, FindPasswordActivity.class), FIND_PASSWORD, options.toBundle());
                } else {
                    startActivityForResult(new Intent(this, FindPasswordActivity.class), FIND_PASSWORD);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(com.maxiaobu.healthclub.chat.Constant.ACCOUNT_CONFLICT, false)) {
            Toast.makeText(this, "账户在其他设备登录", Toast.LENGTH_SHORT).show();
        } else if (intent.getBooleanExtra(com.maxiaobu.healthclub.chat.Constant.ACCOUNT_REMOVED, false)) {
            Toast.makeText(this, "账户被移除", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登录后台
     *
     * @param userName
     * @param password
     */
    public void login(final String userName, final String password) {
        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(dialog -> progressShow = false);
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        DemoDBManager.getInstance().closeDB();
        String phonedeviceno = "Android+" + Build.MODEL.toString() + SystemUtils.ip2long(this);
//        Log.d("LoginActivity", phonedeviceno);
        Observable<JsonObject> loginObservable =
                App.getRetrofitUtil().getRetrofit().getJsonLogin(userName, password, phonedeviceno);
        loginObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                        Log.d("LoginActivity", e.toString());
//                        pd.dismiss();
                        Log.d("LoginActivity","Login.do在LoginActivity中：" + e.toString());
                        if (pd != null)
                            pd.dismiss();
                        Snackbar.make(mLyRoot, "登录接口访问失败", 10000)
                                .setActionTextColor(Color.parseColor("#Fb8435"))
                                .setAction("确定", v -> {
                                }).show();
                    }

                    @Override
                    public void onNext(JsonObject object) {
                        Object msgFlag = object.get("msgFlag");
                        if ("\"1\"".equals(msgFlag.toString())) {
                            Gson gson = new Gson();
                            final BeanMlogin data = gson.fromJson(object, BeanMlogin.class);
                            final String nickname = data.getMember().getNickname();
                            final String avatar = data.getMember().getImgsfilename();
                            final String memid = data.getMember().getMemid();
                            SPUtils.putString(Constant.MEMID, memid);
                            new Thread(() -> {
                                SPUtils.putString(Constant.NICK_NAME, nickname);
                                SPUtils.putString(Constant.AVATAR, avatar);
                                SPUtils.putString(Constant.USER_ID, userName);
                                SPUtils.putString(Constant.MEMROLE, data.getMember().getMemrole());
                            }).start();
                            loginHx(memid.toLowerCase().trim(), password, nickname, avatar, pd);
                        } else {
                            Toast.makeText(LoginActivity.this, object.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                });
    }

    /**
     * 登录环信
     * @param memId
     * @param passWord
     * @param nickName
     * @param avatar
     * @param pd
     */
    private void loginHx(final String memId, final String passWord, final String nickName, final String avatar, final ProgressDialog pd) {
        EMClient.getInstance().login(memId, passWord, new EMCallBack() {
            @Override
            public void onSuccess() {
                new Thread(() -> {
                    boolean updatenick = DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);
                    DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(avatar);
                    DemoHelper.getInstance().setCurrentUserName(memId.toLowerCase()); // 环信Id
                    if (!updatenick) {
                        Log.e("LoginActivity", "更新用户昵称失败");
                    }
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                }).start();

                //如果aty还在,并且加载条正在显示
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                    progressShow = false;
                }
                goHome();
            }

            @Override
            public void onError(int i, final String s) {
                if (!progressShow) {
                    return;
                }
                Log.d("LoginActivity", "i:" + i);

                if (i == 204) {
                    String memid = SPUtils.getString(Constant.MEMID);
                    Observable<JsonObject> observable = App.getRetrofitUtil().getRetrofit().getJsonRegisterEase(memid, passWord);
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<JsonObject>() {
                                @Override
                                public void onCompleted() {
                                    if (pd != null)
                                        pd.dismiss();
                                }

                                @Override
                                public void onError(Throwable e) {
//                                    Toast.makeText(LoginActivity.this, "mregisterEase.do" + e.toString(), Toast.LENGTH_LONG).show();
                                    if (pd != null)
                                        pd.dismiss();
                                    Log.d("LoginActivity", e.toString());
                                    Snackbar.make(mLyRoot, "mregisterEase.do在LoginActivity中：" + e.toString(), 10000)
                                            .setActionTextColor(Color.parseColor("#Fb8435"))
                                            .setAction("确定", v -> {
                                            }).show();
                                }

                                @Override
                                public void onNext(JsonObject object) {
                                    Object msgFlag = object.get("msgFlag");
                                    Log.d("LoginActivity", "msgFlag:" + msgFlag);
                                    if (msgFlag.equals("1")) {
                                        loginHx(memId, passWord, nickName, avatar, pd);
                                    } else {
                                        Toast.makeText(LoginActivity.this, object.get("msgContent").toString(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                } else if (!LoginActivity.this.isFinishing() && pd.isShowing())
                    runOnUiThread(() -> {
                        pd.dismiss();
                        progressShow = false;
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + s,
                                Toast.LENGTH_SHORT).show();
                    });
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("onProgress", "login: 环信正在登录");
            }
        });
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

    /**
     * 去主界面
     * 动画待办
     */
    private void goHome() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        } else {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        }
    }
}
