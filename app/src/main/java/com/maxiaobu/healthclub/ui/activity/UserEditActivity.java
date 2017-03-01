package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanMe;
import com.maxiaobu.healthclub.ui.weiget.AddTextOnClick;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.utils.AliUpdata;
import com.maxiaobu.healthclub.utils.OssService;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.dialog.MQLLoadingFragment;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.CameraUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 魔芋听 on 2016/12/30.
 * email：maxiaobu1216@gmail.com
 * 功能：个人信息编辑页
 * 伪码：
 * 待完成：
 */
public class UserEditActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.tv_information_keep)
    TextView mTvInformationKeep;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.iv_information_image)
    ImageView mIvInformationImage;
    @Bind(R.id.rl_information_change)
    RelativeLayout mRlInformationChange;
    @Bind(R.id.tv_information_birthdayNum)
    TextView mTvInformationBirthdayNum;
    @Bind(R.id.rl_information_birthday)
    RelativeLayout mRlInformationBirthday;
    @Bind(R.id.et_information_userName)
    EditText mEtInformationUserName;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.tv_information_userSex)
    TextView mTvInformationUserSex;
    @Bind(R.id.rl_information_userSex)
    RelativeLayout mRlInformationUserSex;
    @Bind(R.id.et_information_userSign)
    EditText mEtInformationUserSign;
    @Bind(R.id.et_information_consignee)
    TextView mEtInformationConsignee;
    @Bind(R.id.rl_consignee)
    RelativeLayout mRlConsignee;
    @Bind(R.id.iv_detail)
    ImageView mIvDetail;
    private String str;
    private BeanMe mBeanMinem;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private String mSexm;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    private static final int MY_CAMMER = 122;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private Uri uritempFile;
    private String mFileName;

    private String imageStr = new String();

    private String[] sexs = {"男", "女"};
    /**
     * 用户修改资料了吗？  是 true  否  false
     */
    private Boolean userFlag = false;
    /**
     * 判断是否是第一次进入
     */
    private Boolean onceFlag = true;
    /**
     * 前四次不执行
     */
    private int once = 0;

    private AlertDialog.Builder dialog;
    private MQLLoadingFragment mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        ButterKnife.bind(this);
        mShortAnimationDuration = 300;
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "个人编辑");
        mToolbarCommon.setNavigationOnClickListener(v -> {
            if (!userFlag) {
                finish();
            } else {
                dialog = setShowCustom();
                dialog.show();
            }
        });

        mEtInformationUserSign.addTextChangedListener(new AddTextOnClick() {
            @Override
            public void onTextClick(Editable s) {
                userChange(s);
            }
        });
        mEtInformationUserName.addTextChangedListener(new AddTextOnClick() {
            @Override
            public void onTextClick(Editable s) {
                userChange(s);
            }
        });
        mTvInformationUserSex.addTextChangedListener(new AddTextOnClick() {
            @Override
            public void onTextClick(Editable s) {
                userChange(s);
            }
        });
        mTvInformationBirthdayNum.addTextChangedListener(new AddTextOnClick() {
            @Override
            public void onTextClick(Editable s) {
                userChange(s);
            }
        });

    }

    public AlertDialog.Builder setShowCustom() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("是否退出此次编辑？");
        dialog.setPositiveButton("确定", (dialog1, which) -> {
            dialog1.dismiss();
            finish();
        });

        dialog.setNegativeButton("取消", (dialog12, which) -> dialog12.dismiss());
        return dialog;
    }

    @Override
    public void initData() {
        str = getIntent().getStringExtra("values");
        Gson gson = new Gson();
        mBeanMinem = gson.fromJson(str, BeanMe.class);

        Glide.with(this).load(mBeanMinem.getMember().getImgsfilename())
                .placeholder(R.mipmap.ic_place_holder)
                .transform(new GlideCircleTransform(this))
                .into(mIvInformationImage);
        mTvInformationBirthdayNum.setText(SPUtils.getString(Constant.BRITHDAY));

        imageStr = mBeanMinem.getMember().getImgsfilename();

        if (mBeanMinem.getMember().getGender().equals("1")) {
            mTvInformationUserSex.setText("男");
        } else {
            mTvInformationUserSex.setText("女");
        }

        mEtInformationUserSign.setText(mBeanMinem.getMember().getSignature());
        mEtInformationUserName.setText(mBeanMinem.getMember().getNickname());

        mRlConsignee.setOnClickListener(this);
        mTvInformationKeep.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    /**
     * 点击事件来判断是否隐藏软键盘
     *
     * @param ev
     * @return
     */
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

    @OnClick({R.id.rl_consignee, R.id.iv_information_image, R.id.rl_information_change,
            R.id.tv_information_keep, R.id.rl_information_birthday, R.id.rl_information_userSex})
    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.rl_consignee:

                    Intent intent = new Intent(this, RevampAddress.class);
                    startActivity(intent);
                    break;
                case R.id.iv_information_image:
                    zoomImageFromThumb(view, imageStr);
//                Toast.makeText(this, "有功能", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rl_information_change:
//                Toast.makeText(this, "跳转到相册", Toast.LENGTH_SHORT).show();
                    selectImage();
                    break;
                case R.id.tv_information_keep:
                    saveInformation();
                    break;
                case R.id.rl_information_birthday:
                    if (SPUtils.getString(Constant.BRITHDAY).equals("") || null == SPUtils.getString(Constant.BRITHDAY)) {
                        int mYear = 1983;
                        int mMonth = 1;
                        int mDay = 1;
                        setBirthday(mYear, mMonth, mDay);

                    } else {
                        String birth = mTvInformationBirthdayNum.getText().toString();
                        String[] birthday = birth.split("-");
                        int year = Integer.valueOf(birthday[0]);
                        int month = Integer.valueOf(birthday[1]);
                        int day = Integer.valueOf(birthday[2]);
                        setBirthday(year, month - 1, day);
                    }

                    break;
                case R.id.rl_information_userSex:
                    new MaterialDialog.Builder(this).items(sexs).itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            mTvInformationUserSex.setText(sexs[position]);
                            dialog.dismiss();
                        }
                    }).show();
                    break;
            }
        }
    }

    //设置生日日期
    public void setBirthday(int year, int month, int day) {

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            int mon = month1 + 1;
            mTvInformationBirthdayNum.setText(year1 + "-" + mon + "-" + dayOfMonth);
        }, year, month, day);

        dialog.show();
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            DatePicker dp = dialog.getDatePicker();
            dp.setMaxDate(new Date().getTime());
        }

    }

    /**
     * 大小图缩放
     *
     * @param thumbView The thumbnail view to zoom in.
     * @param url       图片网址
     */
    private void zoomImageFromThumb(final View thumbView, String url) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        Glide.with(mActivity).load(url).placeholder(R.mipmap.ic_place_holder).into(mIvDetail);
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        mIvDetail.setVisibility(View.VISIBLE);

        mIvDetail.setPivotX(0f);
        mIvDetail.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        mIvDetail.setOnClickListener(view -> {
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }
            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left))
                    .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(mIvDetail, View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(mIvDetail, View.SCALE_Y, startScaleFinal));
            set1.setDuration(mShortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    mIvDetail.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    mIvDetail.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
            });
            set1.start();
            mCurrentAnimator = set1;
        });
    }


    private void saveInformation() {
        if (mFileName != null) {
            String memid = SPUtils.getString(Constant.MEMID);
            String objectName = "image/bmember/" + memid + "_" + System.currentTimeMillis() + ".png";
            //1初始化
            OssService ossService = AliUpdata.getOssService();
            //2设置坚挺
            ossService.setAliUpdataListener(new OssService.AliUpdataListener() {
                @Override
                public void onBegin() {
                    //回调1：上传开始时调用
                    mDialog = MQLLoadingFragment.getInstance(6, 4.0f, false, false, false, false);
                    mDialog.show(getSupportFragmentManager(), "");
                }

                @Override
                public void onUpdataSuccess(String objectName) {
                    //回调2：上传成功了
                    if (mTvInformationUserSex.getText().toString().equals("男")) {
                        mSexm = "1";
                    } else {
                        mSexm = "0";
                    }
                    String birth = mTvInformationBirthdayNum.getText().toString();
                    String[] bir = birth.split("-");
                    String userName = mEtInformationUserName.getText().toString();
                    String sign = mEtInformationUserSign.getText().toString();
                    String birthday = bir[0] + "/" + bir[1] + "/" + bir[2];
                    Observable<JsonObject> jsonUpdateMember =
                            App.getRetrofitUtil().getRetrofit().getJsonUpdateMember(objectName,
                                    SPUtils.getString(Constant.MEMID), mSexm,
                                    userName, sign, birthday);
                    jsonUpdateMember.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<JsonObject>() {
                                @Override
                                public void onCompleted() {
                                    if (mDialog != null)
                                        mDialog.dismiss();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (mDialog != null)
                                        mDialog.dismiss();

                                }

                                @Override
                                public void onNext(JsonObject jsonObject) {
                                    finish();
                                    SPUtils.putBoolean(Constant.FALG, true);
                                }
                            });
                }

                @Override
                public void onUpdataFail(String error) {
                    if (mDialog != null)
                        mDialog.dismiss();
                }
            });
            //3开始上传
            ossService.asyncPutImage(objectName, mFileName);
        } else {
            if (mTvInformationUserSex.getText().toString().equals("男")) {
                mSexm = "1";
            } else {
                mSexm = "0";
            }
            String birth = mTvInformationBirthdayNum.getText().toString();
            String[] bir = birth.split("-");
            String userName = mEtInformationUserName.getText().toString();
            String sign = mEtInformationUserSign.getText().toString();
            String birthday = bir[0] + "/" + bir[1] + "/" + bir[2];
            Observable<JsonObject> jsonUpdateMember = App.getRetrofitUtil().getRetrofit().getJsonUpdateMember("",
                    SPUtils.getString(Constant.MEMID), mSexm,
                    userName, sign, birthday);
            jsonUpdateMember.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<JsonObject>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            mDialog = MQLLoadingFragment.getInstance(6, 4.0f, false, false, false, false);
                            mDialog.show(getSupportFragmentManager(), "");
                        }

                        @Override
                        public void onCompleted() {
                            if (mDialog != null)
                                mDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mDialog != null)
                                mDialog.dismiss();

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            finish();
                            SPUtils.putBoolean(Constant.FALG, true);
                        }
                    });
        }
    }

    /**
     * 弹出dialog选择是相册还是拍照
     */
    private void selectImage() {
        new MaterialDialog.Builder(this)
                .title("上传图片")
                .items(new String[]{"相册", "拍照"})
                .itemsCallback((dialog1, itemView, position, text) -> {
                    switch (position) {
                        //相册
                        case 0:
                            Intent intentFromGallery = CameraUtils.getImagePickerIntent();
                            startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                            dialog1.dismiss();
                            break;
                        //拍照
                        case 1:
                            getCamera();
                            dialog1.dismiss();
                            break;
                    }
                }).show();
    }

    @AfterPermissionGranted(MY_CAMMER)
    private void getCamera() {
        if (EasyPermissions.hasPermissions(myApplication, Manifest.permission.CAMERA)) {
            Intent intentFromCapture = CameraUtils.getOpenCameraIntent();
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Constant.CACHE_DIR + "temp.png")));
            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "需要相机权限", MY_CAMMER,
                    Manifest.permission.CAMERA);
        }
    }

    /**
     * 返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取系统的现有图片
            case IMAGE_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    startPhotoZoom(data.getData());
                }
                break;
            //拍照得到的图片
            case CAMERA_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    File parent = new File(Constant.CACHE_DIR);
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    File tempFile = new File(Constant.CACHE_DIR + "temp.png");
                    startPhotoZoom(Uri.fromFile(tempFile));
                }
                break;
            //返回的结果
            case RESULT_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    if (data != null) {
                        //将Uri图片转换为Bitmap
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                            mFileName = saveBmp(bitmap);
                            imageStr = mFileName;
                            userFlag = true;
                            Glide.with(this).load(mFileName).transform(new GlideCircleTransform(this)).into(mIvInformationImage);
//                            mIvInformationImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 调用系统裁剪功能
     *
     * @param uri 要裁剪的地址。
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 5);
        intent.putExtra("aspectY", 5);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.png");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.WEBP.toString());
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //存图片到本地
    private String saveBmp(Bitmap bmp) {
        String fileName = Constant.CACHE_DIR + System.currentTimeMillis() + ".png";
        FileOutputStream stream = null;
        try {
            File parent = new File(Constant.CACHE_DIR);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            stream = new FileOutputStream(fileName);
            bmp.compress(Bitmap.CompressFormat.PNG, 70, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        bmp.recycle();
        return fileName;
    }


    @Override
    public void onBackPressed() {
        if (mIvDetail.getVisibility() == View.VISIBLE) {
            mIvDetail.performClick();

        } else if (userFlag) {
            dialog = setShowCustom();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    public void userChange(Editable s) {
        if (!onceFlag) {
            if (!s.toString().equals(mBeanMinem.getMember().getSignature())
                    || !s.toString().equals(mBeanMinem.getMember().getNickname())
                    || !s.toString().equals(mBeanMinem.getMember().getGendername())
                    || !s.toString().equals(SPUtils.getString(Constant.BRITHDAY)))
                userFlag = true;
        } else {
            if (once == 3) {
                onceFlag = false;
            } else {
                once++;
            }
        }
    }
}
