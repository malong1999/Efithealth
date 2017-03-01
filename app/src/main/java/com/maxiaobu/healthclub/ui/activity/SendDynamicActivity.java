package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.PhotoBitmapUtils;
import com.maxiaobu.healthclub.utils.AliUpdata;
import com.maxiaobu.healthclub.utils.OssService;
import com.maxiaobu.healthclub.utils.SystemUtils;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mqldialoglibrary.dialog.MQLLoadingFragment;
import maxiaobu.mxbutilscodelibrary.ImageUtils;
import maxiaobu.mxbutilscodelibrary.ScreenUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.R.attr.bitmap;
import static android.R.attr.data;
import static android.R.attr.path;
import static vi.com.gdi.bgl.android.java.EnvDrawText.bmp;

/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：发布动态
 * 伪码：
 * 待完成：
 */
public class SendDynamicActivity extends BaseAty {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.tv_information_keep)
    TextView mTvInformationKeep;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.send_edit_text)
    EditText mSendEditText;
    @Bind(R.id.send_iamge_view)
    ImageView mSendIamgeView;
    @Bind(R.id.send_camera)
    ImageView mSendCamera;
    @Bind(R.id.send_picture)
    ImageView mSendPicture;
    @Bind(R.id.send_image_close)
    ImageView mSendImageClose;
    @Bind(R.id.send_image_layout)
    RelativeLayout mSendImageLayout;
    @Bind(R.id.send_permissions_image)
    ImageView mSendPermissionsImage;
    @Bind(R.id.send_permissions_text)
    TextView mSendPermissionsText;
    @Bind(R.id.send_permissions)
    LinearLayout mSendPermissions;
    private Object mCamera;
    private static final int MY_CAMMER = 122;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int IMAGE_REQUEST_CODE = 0;
    public static final int SEND_DYNAMIC_PREMISSIONS = 100;
    /**
     * 拍照得到的照片的存放地址
     */
    private String mPath;
    /**
     * 要上传的图片存放在阿里云上的地址
     */
    private String str;
    /**
     * 要上传的图片的手机地址
     */
    private String mFileName;
    private MQLLoadingFragment mDialog;
    /**
     * EventBus对象
     * 让TrainerDynamicFragment与HotDynamicFragment、DynamicFragment刷新一下，添加刚刚发表的动态
     * 注意：
     * HotDynamicFragment新出现的数据会在列表的最底下，其余的Fragment都会出现在列表的第一个。
     * HotDynamicFragment新出现的数据会在列表的最底下，其余的Fragment都会出现在列表的第一个。
     * HotDynamicFragment新出现的数据会在列表的最底下，其余的Fragment都会出现在列表的第一个。
     * 重要的事要说三遍！
     */
    private EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_dynamic);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "新动态");
        mEventBus = EventBus.getDefault();
        //调起系统拍照功能
        mSendCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCamera();
            }
        });
        //调起系统相册功能
        mSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
            }
        });
        //删除已选择的图片，点击黑色圆叉
        mSendImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendIamgeView.setImageURI(null);
                mSendImageLayout.setVisibility(View.GONE);
            }
        });

        //点击“发布”按钮
        mTvInformationKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSendImageLayout.getVisibility() == View.GONE) {
                    Toast.makeText(SendDynamicActivity.this, "请选择一张照片上传", Toast.LENGTH_SHORT).show();
                } else {
                    mFileName = resizeBitmap(mFileName);
                    str = "image/bmember/" + SPUtils.getString(Constant.MEMID) + "_" + System.currentTimeMillis() + ".png";
                    //1初始化
                    OssService ossService = AliUpdata.getOssService();
                    //2设置坚挺
                    ossService.setAliUpdataListener(new OssService.AliUpdataListener() {
                        @Override
                        public void onBegin() {
                            mDialog = MQLLoadingFragment.getInstance(6, 4.0f, false, false, false, false);
                            mDialog.show(getSupportFragmentManager(), "");
                        }

                        @Override
                        public void onUpdataSuccess(String objectName) {
//                            Toast.makeText(SendDynamicActivity.this, "图片成功到阿里", Toast.LENGTH_SHORT).show();
                            Log.d("myt", "图片成功到阿里");
                            initData();
                        }

                        @Override
                        public void onUpdataFail(String error) {
                            Log.d("SendDynamicActivity", error);
                            if (mDialog != null)
                                mDialog.dismiss();
                        }
                    });
                    Log.d("myt", mFileName);
                    //3开始上传
                    ossService.asyncPutImage(str, mFileName);

                }
            }
        });

        //点击"权限"进入权限的选择：好友圈、公开、仅自己可见
        mSendPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendDynamicActivity.this, SendDyniamicPremissions.class);
                intent.putExtra("premissions", mSendPermissionsText.getText().toString());
                startActivityForResult(intent, SEND_DYNAMIC_PREMISSIONS);
            }
        });

    }

    @Override
    public void initData() {

        RequestParams params = new RequestParams();
        params.put("post.authid", SPUtils.getString(Constant.MEMID));
        params.put("post.content", mSendEditText.getText().toString());
        params.put("post.imgpath", "/" + str);
        params.put("post.visiable", String.valueOf(1));
//        if (mSendPermissionsText.getText().toString().equals("公开")) {
//            params.put("post.visiable", String.valueOf(1));
//        } else if (mSendPermissionsText.getText().toString().equals("仅自己可见")) {
//            params.put("post.visiable", String.valueOf(3));
//        } else {
//            params.put("post.visiable", String.valueOf(2));
//        }
        App.getRequestInstance().post(UrlPath.URL_SEND_DYNAMIC, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                JSONObject object = null;
                try {
                    object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
                    String msgContent = object.getString("msgContent");
                    Log.d("SendDynamicActivity", msgContent);
                    Toast.makeText(SendDynamicActivity.this, msgContent, Toast.LENGTH_SHORT).show();
                    if (msgFlag.equals("1")) {
                        if (mDialog != null)
                            mDialog.dismiss();
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setSquareDynamicFalg(true);
                        mEventBus.post(beanEventBas);
                        setResult(101);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestError(VolleyError e, String error) {
                Log.d("SendDynamicActivity", error);
                if (mDialog != null)
                    mDialog.dismiss();

            }

            @Override
            public void noInternet(VolleyError e, String error) {
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取系统的现有图片
            case IMAGE_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    mSendImageLayout.setVisibility(View.VISIBLE);
                    Glide.with(this).load(data.getData()).into(mSendIamgeView);
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Uri uri = data.getData();
                    uri = geturi(data);
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        String path = cursor.getString(column_index);
                        mFileName = path;
                    }
                    cursor.close();
//                    mFileName = resizeBitmap(mFileName);
                    mFileName = getPicture(mFileName);
                }
                break;
            //拍照得到的图片
            case CAMERA_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    mSendImageLayout.setVisibility(View.VISIBLE);
                    mFileName = mPath;
                    Glide.with(this).load(mFileName).into(mSendIamgeView);
//                    mFileName = resizeBitmap(mFileName);
                    mFileName = getPicture(mFileName);
                }
                break;
            //状态发送权限
            case SEND_DYNAMIC_PREMISSIONS:

                if (data != null && data.getStringExtra("text") != null) {
                    mSendPermissionsText.setText(data.getStringExtra("text"));
                    if (data.getStringExtra("text").toString().equals("公开")) {
                        mSendPermissionsImage.setBackgroundResource(R.mipmap.bt_public_dynamic);
                    } else if (data.getStringExtra("text").toString().equals("仅自己可见")) {
                        mSendPermissionsImage.setBackgroundResource(R.mipmap.bt_mine_dynamic);
                    } else {
                        mSendPermissionsImage.setBackgroundResource(R.mipmap.bt_friend_dynamic);
                    }
                }

                break;
        }

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

    @AfterPermissionGranted(MY_CAMMER)
    private void getCamera() {
        if (EasyPermissions.hasPermissions(myApplication, Manifest.permission.CAMERA)) {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File parent = new File(Constant.CACHE_DIR);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            mPath = Constant.CACHE_DIR + System.currentTimeMillis() + "temp.jpg";
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPath)));
            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "需要相机权限",
                    MY_CAMMER, Manifest.permission.CAMERA);
        }
    }


    /**
     * 根据图片地址，把竖着照的图片(原来是横着放的)，竖着放
     * 旋转图片的代码
     *
     * @param fileName
     * @return
     */
    public String getPicture(String fileName) {
        String filName = Constant.CACHE_DIR + System.currentTimeMillis() + ".jpg";
        try {
            ExifInterface exifInterface = new ExifInterface(fileName);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                Matrix matrix = new Matrix();
                Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                matrix.setRotate(90);
                Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (null != bitmap) {
                    bitmap.recycle();
                }
                // 保存图片
                File parent = new File(Constant.CACHE_DIR);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                File file = new File(filName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();//输出
                    fos.close();//关闭
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                Matrix matrix = new Matrix();
                Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                matrix.setRotate(180);
                Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (null != bitmap) {
                    bitmap.recycle();
                }
                // 保存图片
                File parent = new File(Constant.CACHE_DIR);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                File file = new File(filName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();//输出
                    fos.close();//关闭
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                Matrix matrix = new Matrix();
                Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                matrix.setRotate(270);
                Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (null != bitmap) {
                    bitmap.recycle();
                }
                // 保存图片
                File parent = new File(Constant.CACHE_DIR);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                File file = new File(filName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();//输出
                    fos.close();//关闭
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return fileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filName;
    }

    String resizeBitmap(String fileName) {
//        String filName = Constant.CACHE_DIR + System.currentTimeMillis() + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        int width = bitmap.getWidth();
        Log.d("SendDynamicActivity", "width:" + width);
        int height = bitmap.getHeight();
        Log.d("SendDynamicActivity", "height:" + height);
        float scale = (float) width / height;
        Log.d("SendDynamicActivity", "scale:" + scale);
        if (width > height) {
            if (height > 818) {
                width = (int) (scale * 818);
                height = 818;
            }
        } else {
            if (width > 818) {
                height = (int) (818 / scale);
                width = 818;
            }
        }

        Bitmap bitmap1 = ImageUtils.compressByScale(bitmap, width, height);
        // 保存图片
        File parent = new File(Constant.CACHE_DIR);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        File file = new File(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();//输出
            fos.close();//关闭
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
}
