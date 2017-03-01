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
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.MpCourseSave;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 编辑课程
 */
public class CoachesHistoryManageActivity extends BaseAty implements EasyPermissions.PermissionCallbacks, View.OnClickListener {
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
    @Bind(R.id.et_price)
    AppCompatEditText mEtPrice;
    @Bind(R.id.ly_price)
    LinearLayout mLyPrice;
    @Bind(R.id.ed_content)
    AppCompatEditText mEdContent;
    @Bind(R.id.tv_online)
    TextView mTvOnline;
    @Bind(R.id.tv_delete)
    TextView mTvDelete;

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int MY_CAMMER = 122;
    private String mFileName;
    private MpCourseSave mpCourseSave;
    private String clubid;
    private Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_history_manage);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "编辑课程");
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mIvTop.getLayoutParams().height = (int) (width / 22 * 15);

        Intent intent = getIntent();
        mEtName.setText(intent.getStringExtra("pcoursename"));
        mEtTimes.setText(intent.getStringExtra("pcoursetimes"));
        mEtDays.setText(intent.getStringExtra("pcoursedays"));
        mEtPrice.setText(intent.getStringExtra("pcourseprice"));
        mEdContent.setText(intent.getStringExtra("resinform"));
        Glide.with(this).load(intent.getStringExtra("imgsfile")).placeholder(R.mipmap.ic_place_holder).into(mIvTop);

    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.iv_top,R.id.tv_online,R.id.tv_delete})//, R.id.tv_release
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top:
                selectImage();
                break;
            case R.id.tv_online:
                if (prepareRelease()) {
                    releaseCourse();
                }
                break;
            case R.id.tv_delete:
                deleteCourse();
                break;
            default:
                break;
        }
    }

    private void deleteCourse() {
        RequestParams params = new RequestParams();
        params.put("pcourseid", getIntent().getStringExtra("pcourseid"));// 课程编号
        App.getRequestInstance().post(this, UrlPath.URL_DELETECOURSE, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Gson gson = new Gson();
                mpCourseSave = gson.fromJson(json, MpCourseSave.class);
                if ("1".equals(mpCourseSave.getMsgFlag())) {
                    Toast.makeText(mActivity, mpCourseSave.getMsgContent(), Toast.LENGTH_SHORT).show();
                    CoachesHistoryManageActivity.this.setResult(Constant.RESULT_OK);
                    CoachesHistoryManageActivity.this.finish();
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
    }


    private boolean prepareRelease() {
        if (TextUtils.isEmpty(mEtName.getText().toString()) ||
                TextUtils.isEmpty(mEtDays.getText().toString()) ||
                TextUtils.isEmpty(mEtTimes.getText().toString()) ||
                TextUtils.isEmpty(mEtPrice.getText().toString()) ||
                TextUtils.isEmpty(mEdContent.getText().toString())) {
            Toast.makeText(mActivity, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void releaseCourse() {
        try {
            RequestParams params = new RequestParams();
            if (!TextUtils.isEmpty(mFileName)) {
                FileInputStream inputStream = new FileInputStream(mFileName);
                params.put("imagefile", inputStream, mFileName);// 图片修改
            }
            params.put("pcourseid", getIntent().getStringExtra("pcourseid"));// 课程编号
            params.put("pcoursename",  mEtName.getText().toString().trim());// 名称修改
            params.put("pcoursetimes", mEtTimes.getText().toString().trim());
            params.put("pcoursedays", mEtDays.getText().toString().trim());
            params.put("pcourseprice", mEtPrice.getText().toString().trim());
            params.put("resinform", mEdContent.getText().toString().trim());
            params.put("linestatus", "1");// 1上线0:下线
            App.getRequestInstance().post(this, UrlPath.URL_UPDATECOURSE, params, new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    Gson gson = new Gson();
                    mpCourseSave = gson.fromJson(json, MpCourseSave.class);
                    if ("1".equals(mpCourseSave.getMsgFlag())) {
                        Toast.makeText(mActivity, mpCourseSave.getMsgContent(), Toast.LENGTH_SHORT).show();
                        CoachesHistoryManageActivity.this.setResult(Constant.RESULT_OK_ONE);
                        CoachesHistoryManageActivity.this.finish();
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
//            Log.d("Coaches", e.toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    startPhotoZoom(data.getData());
                }
                break;
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
            case RESULT_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    if (data != null) {
//                        getImageToView(data);

                        //将Uri图片转换为Bitmap
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                            mFileName =   saveBmp(bitmap);
//                            mIvTop.setImageURI(Uri.parse(mFileName));
                            Glide.with(this).load(new File(mFileName)).placeholder(R.mipmap.ic_place_holder).into(mIvTop);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
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

    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mFileName = saveBmp(photo);
//            mIvTop.setImageURI(Uri.parse(mFileName));
            Glide.with(this).load(new File(mFileName)).placeholder(R.mipmap.ic_place_holder).into(mIvTop);
        }
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
        bmp.recycle();
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
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "你妈的，瞎恩个jb，点设置---》权限---》访问相机的权限给我", R.string.setting, R.string.cancel, perms);
    }
}
