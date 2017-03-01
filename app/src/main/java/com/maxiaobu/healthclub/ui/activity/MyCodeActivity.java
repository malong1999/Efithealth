package com.maxiaobu.healthclub.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/13.
 * 我的跳转的扫描二维码
 */
public class MyCodeActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.iv_user_image)
    ImageView mIvUserImage;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.iv_user_code)
    ImageView mIvUserCode;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mine_code, menu);
        return true;
    }

    /**
     * 创建菜单item点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.save_imageCode){
            saveImageToGallery(this,mBitmap);
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            return  true;
        }else if(id == R.id.scan_imageCode){
            Intent intent = new Intent(this,
                    ScanCodeActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示item中的图片；
     * @param view
     * @param menu
     * @return
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"我的二维码");
        mTvUserName.setText(SPUtils.getString(Constant.NICK_NAME));
        Glide.with(this).load(SPUtils.getString(Constant.AVATAR))
                .transform(new GlideCircleTransform(this))
                .placeholder(R.mipmap.ic_place_holder)
                .into(mIvUserImage);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

        }

    }

    @Override
    public void initData() {
        Bitmap qrBitmap = generateBitmap("member://"+System.currentTimeMillis(), 1100, 1100);
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mBitmap = addLogo(qrBitmap, logoBitmap);
        mIvUserCode.setImageBitmap(mBitmap);
    }

    /**
     * 二维码的创建
     * @param content 扫创建完成以后的二维码将会跳转的内容
     * @param width 二维码的宽度
     * @param height  二维码的高度
     * @return  返回创建的二维码的bitmap
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);  //边距
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width ,height , hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0xff000000;
                    } else {
                        pixels[i * width + j] = 0xffFAFAFA;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加二维码中间图标
     * @param qrBitmap 二维码
     * @param logoBitmap 图标
     * @return 返回有中间图标的二维码
     */
    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize =1f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 4) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 4)) {
            scaleSize *= 1.5;
        }
        float sx = 1.0f / scaleSize;
//        float sx =  20;
//        扩大。x为水平方向的放大倍数，y为竖直方向的放大倍数。
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

    /**
     * 保存图片到系统相册
     * @param context
     * @param bmp  要保存的图片
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://Download/")));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data == null) {
//                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("data");
                try {
                    String[] str = scanResult.split("//");
                    if(str[0].equals("interface:")){
                        String url = str[1];
                        App.getRequestInstance().post(UrlPath.URL_BASE+url+"&memid="+SPUtils.getString(Constant.MEMID), this, null, new RequestListener() {
                            @Override
                            public void requestSuccess(String json) {
//
                                try {
                                    JSONObject object = null;
                                    object = new JSONObject(json);
                                    Object msgFlag = object.get("msgFlag");
                                    Object msgContent = object.get("msgContent");
                                    if(msgFlag.equals("1")){
                                        startActivity(new Intent(MyCodeActivity.this, OrderListActivity.class));
                                    }else if(msgFlag.equals("0")){
                                        if(msgContent != null){
                                            Toast.makeText(MyCodeActivity.this, msgContent.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void requestError(VolleyError volleyError, String s) {
                            }

                            @Override
                            public void noInternet(VolleyError volleyError, String s) {
                            }

                        });

                    }else if(str[0].equals("http:")){

                        Intent intent= new Intent(this, WebActivity.class);
                        intent.putExtra("url",scanResult);
                        startActivity(intent);

                    }else if(str[0].equals("member:")){
                        Toast.makeText(this, "社交功能即将开放", Toast.LENGTH_SHORT).show();
                    }

                }catch (NullPointerException e){
                    Toast.makeText(this, "二维码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
