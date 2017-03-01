package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMmanager;
import com.maxiaobu.healthclub.common.beangson.ClubList;
import com.maxiaobu.healthclub.common.beangson.MpCourseSave;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestJsonListener;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.DialogAction;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：发布课程
 * 伪码：
 * 待完成：
 */
public class CourseReleaseActivity extends BaseAty implements EasyPermissions.PermissionCallbacks, View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.iv_top)
    ImageView mIvTop;
    @Bind(R.id.et_name)
    AppCompatEditText mEtName;
    @Bind(R.id.et_times)
    AppCompatEditText mEtTimes;
    @Bind(R.id.ly_times)
    LinearLayout mLyTimes;
    @Bind(R.id.et_days)
    AppCompatEditText mEtDays;
    @Bind(R.id.ly_days)
    LinearLayout mLyDays;
    @Bind(R.id.tv_club)
    TextView mTvClub;
    @Bind(R.id.ly_club)
    LinearLayout mLyClub;
    @Bind(R.id.et_price)
    AppCompatEditText mEtPrice;
    @Bind(R.id.ly_price)
    LinearLayout mLyPrice;
    @Bind(R.id.ed_content)
    AppCompatEditText mEdContent;
    @Bind(R.id.tv_release)
    TextView mTvRelease;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int MY_CAMMER = 122;
    private String mFileName;
    private MpCourseSave mpCourseSave;
    private String clubid;
    private boolean hadScheduleManagement = false;
    private Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_release);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "发布课程");
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mIvTop.getLayoutParams().height = (int) (width / 22 * 15);
    }

    @Override
    public void initData() {
        App.getRequestInstance().post(this, UrlPath.URL_MMANAGER,
                BeanMmanager.class, new RequestParams("coachid", SPUtils.getString(Constant.MEMID))
                , new RequestJsonListener<BeanMmanager>() {
                    @Override
                    public void requestSuccess(BeanMmanager result) {
                        List<BeanMmanager.ManagerlistBean> managerlist = result.getManagerlist();
                        for (int i = 0; i < managerlist.size(); i++) {
                            if (!TextUtils.isEmpty(managerlist.get(i).getSchtimeslice())) {
                                hadScheduleManagement = true;
                                return;
                            }
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

    @OnClick({R.id.iv_top, R.id.tv_release, R.id.ly_club})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top:
                selectImage();
                break;
            case R.id.tv_release:
                if (prepareRelease()) {
                    releaseCourse();
                }
                break;
            case R.id.ly_club:
                showClubDialog();
                break;

            default:
                break;
        }
    }

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
                    File tempFile = new File(Constant.CACHE_DIR + "temp.jpg");
                    startPhotoZoom(Uri.fromFile(tempFile));
                }
                break;
            //返回的结果
            case RESULT_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    if (data != null) {
//                        getImageToView(data);
                        //将Uri图片转换为Bitmap
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));

                            mFileName =   saveBmp(bitmap);
                            mIvTop.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constant.RESULT_REQUEST_THIRD:
                if (resultCode == Constant.RESULT_OK_ONE) {
                    initData();
                } else if (requestCode == Constant.RESULT_OK_TWO) {
                    hadScheduleManagement = false;
                }
                break;
        }
    }

    private void showClubDialog() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(mActivity, UrlPath.URL_CLUB_MESSAGE, ClubList.class, params, new RequestJsonListener<ClubList>() {
            @Override
            public void requestSuccess(final ClubList clubList) {
                if ("1".equals(clubList.getMsgFlag())) {
//                    Log.d("===教练绑定的俱乐部信息 ok", "ok");
                    ArrayList<String> clubs = new ArrayList<String>();
                    for (int i = 0; i < clubList.getBindList().size(); i++) {
                        clubs.add(clubList.getBindList().get(i).getClubname());
                    }
                    new MaterialDialog.Builder(mActivity)
                            .items(clubs)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    clubid = clubList.getBindList().get(position).getClubid();
                                    mTvClub.setText(clubList.getBindList().get(position).getClubname());
                                }
                            }).show();

                } else {
                    Toast.makeText(mActivity, "没有教练绑定信息", Toast.LENGTH_LONG).show();
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

    private boolean prepareRelease() {
        if (mTvClub.getText().toString().equals("合作俱乐部(必选)") ||
                TextUtils.isEmpty(mEtName.getText().toString()) ||
                TextUtils.isEmpty(mEtDays.getText().toString()) ||
                TextUtils.isEmpty(mEtTimes.getText().toString()) ||
                TextUtils.isEmpty(mEtPrice.getText().toString()) ||
                TextUtils.isEmpty(mEdContent.getText().toString())) {
            Toast.makeText(mActivity, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!hadScheduleManagement) {
            new MaterialDialog.Builder(mActivity)
                    .title("提示")
                    .content("您需要选择课程档期后才能发布课程")
                    .positiveText("去选择")
                    .negativeText("稍后")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivityForResult(new Intent(CourseReleaseActivity.this, ScheduleManagementActivity.class), Constant.RESULT_REQUEST_THIRD);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        } else {
            return true;
        }
    }

    private void releaseCourse() {
        try {
            FileInputStream inputStream = new FileInputStream(mFileName);
            RequestParams params = new RequestParams();
            params.put("coachid", SPUtils.getString(Constant.MEMID));
            params.put("pcoursename", mEtName.getText().toString().trim());// 课程名称
            params.put("pcoursecode", "");// 课程特征码
            params.put("imagefile", inputStream, mFileName);
            params.put("pcoursetimes", mEtTimes.getText().toString().trim());
            params.put("pcoursedays", mEtDays.getText().toString().trim());
            params.put("pcourseprice", mEtPrice.getText().toString().trim());
            params.put("resinform", mEdContent.getText().toString().trim());
            params.put("clubid", clubid);

            App.getRequestInstance().post(this, UrlPath.URL_ISSUE_COURSE, params, new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    Gson gson = new Gson();
                    mpCourseSave = gson.fromJson(json, MpCourseSave.class);
                    if ("1".equals(mpCourseSave.getMsgFlag())) {
                        Toast.makeText(mActivity, mpCourseSave.getMsgContent(), Toast.LENGTH_SHORT).show();
                        CourseReleaseActivity.this.finish();
                    } else {
                        Toast.makeText(mActivity, mpCourseSave.getMsgContent(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void requestError(VolleyError volleyError, String s) {

                }

                @Override
                public void noInternet(VolleyError volleyError, String s) {

                }

            });
        } catch (Exception e) {
            Log.d("Coaches", e.toString());
            Toast.makeText(mActivity, "请上传图片", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        new MaterialDialog.Builder(this)
                .title("上传图片")
                .items(new String[]{"相册", "拍照"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            //相册
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_PICK);
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                dialog.dismiss();
                                break;
                            //拍照
                            case 1:
                                getCamera();
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }


    /**
     * 调用系统裁剪功能
     * @param uri  要裁剪的地址。
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
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 550);
        intent.putExtra("outputY", 375);

        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.WEBP.toString());
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private Map<Bundle, SoftReference<Bitmap>> imageCache = new HashMap<Bundle, SoftReference<Bitmap>>();

    /**
     * 得到图片的地址，设置上图片
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            addBitmapToCache(extras);
            mFileName = saveBmp(getBitmapByPath(extras));
            mIvTop.setImageURI(Uri.parse(mFileName));
        }
    }

    /**
     * 图片到内存。软引用
     * @param bundle
     */
    public void addBitmapToCache(Bundle bundle) {
        // 强引用的Bitmap对象
        Bitmap bitmap = bundle.getParcelable("data");
        // 软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        // 添加该对象到Map中使其缓存
        imageCache.put(bundle, softBitmap);
    }

    /**
     * 从软引用中提出存入的bitmap
     * @param path 这是存入的bundle.
     * @return  返回是一个Bitmap
     */
    public Bitmap getBitmapByPath(Bundle path) {
        // 从缓存中取软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = imageCache.get(path);
        // 判断是否存在软引用
        if (softBitmap == null) {
            return null;
        }
        // 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空
        Bitmap bit = softBitmap.get();
        return bit;
    }


    private String saveBmp(Bitmap bmp) {
        String fileName = Constant.CACHE_DIR + System.currentTimeMillis() + ".jpg";
        FileOutputStream stream = null;
        try {
            File parent = new File(Constant.CACHE_DIR);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            stream = new FileOutputStream(fileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
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


    @AfterPermissionGranted(MY_CAMMER)
    private void getCamera() {
        if (EasyPermissions.hasPermissions(myApplication, Manifest.permission.CAMERA)) {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constant.CACHE_DIR + "temp.jpg")));
            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "需要相机权限",
                    MY_CAMMER, Manifest.permission.CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "点设置---》权限---》访问相机的权限给我", R.string.setting, R.string.cancel, perms);
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
