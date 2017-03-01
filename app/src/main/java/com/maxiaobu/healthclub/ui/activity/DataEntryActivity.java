package com.maxiaobu.healthclub.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterDataEntryLeftListAty;
import com.maxiaobu.healthclub.adapter.AdapterDataEntryRightListAty;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanItems;
import com.maxiaobu.healthclub.common.beangson.BeanMgettrainingitem;
import com.maxiaobu.healthclub.dao.DataEntryDbHelper;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;

/**
 * Created by 马小布 on 2016/12/13.
 * email：maxiaobu1216@gmail.com
 * 功能：数据录入
 * 伪码：
 * 待完成：
 */
public class DataEntryActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.tv_title_save)
    TextView mTvTitleSave;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.rv_left)
    RecyclerView mRvLeft;
    @Bind(R.id.rv_right)
    RecyclerView mRvRight;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_times)
    TextView mTvTimes;
    @Bind(R.id.iv_times_add)
    ImageView mIvTimesAdd;
    @Bind(R.id.tv_times_num)
    AppCompatEditText mTvTimesNum;
    @Bind(R.id.iv_times_reduce)
    ImageView mIvTimesReduce;
    @Bind(R.id.tv_strength)
    TextView mTvStrength;
    @Bind(R.id.iv_strength_add)
    ImageView mIvStrengthAdd;
    @Bind(R.id.tv_strength_num)
    AppCompatEditText mTvStrengthNum;
    @Bind(R.id.iv_strength_reduce)
    ImageView mIvStrengthReduce;
    @Bind(R.id.tv_delete)
    TextView mTvDelete;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.ll_edit)
    LinearLayout mLlEdit;
    @Bind(R.id.fl_edit)
    FrameLayout mFlEdit;
    private List<BeanMgettrainingitem.ListBean.ItemsBean> mLeftData;
    List<BeanItems.ItemsBean> mRightData;
    AdapterDataEntryLeftListAty mLeftAdapter;
    AdapterDataEntryRightListAty mRightAdapter;
    private SQLiteDatabase mDb;
    private String mCorderlessonid;//表名
    private int mTimesNum = 1;
    private int mStrengthNum = 1;
    private int inputType;//0第一次录入数据  1有数据未打开3收起来
    private String currentItemID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "训练数据录入");
        mLeftData = new ArrayList<>();
        mRightData = new ArrayList<>();
        mCorderlessonid = getIntent().getStringExtra("corderlessonid");
        DataEntryDbHelper mDbHelper = new DataEntryDbHelper(this, mCorderlessonid);
        mDb = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(mDb);

        LinearLayoutManager layoutManagerLeft = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvLeft.setLayoutManager(layoutManagerLeft);
        mRvLeft.setItemAnimator(new DefaultItemAnimator());
        mLeftAdapter = new AdapterDataEntryLeftListAty(this, mLeftData);
        mRvLeft.setAdapter(mLeftAdapter);

        LinearLayoutManager layoutManagerRight = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvRight.setHasFixedSize(true);
        mRvRight.setLayoutManager(layoutManagerRight);
        mRvRight.setItemAnimator(new DefaultItemAnimator());
        mRightAdapter = new AdapterDataEntryRightListAty(this, mRightData, mDb);
        mRvRight.setAdapter(mRightAdapter);
        mRightAdapter.setOnItemClickListener(new AdapterDataEntryRightListAty.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String itemId, String title, int position, int status) {
                if (status == 0) {
                    inputType = status;
                    currentItemID = itemId;
                    mTvTitle.setText(title);
                    mTvSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkZero()){
                                add(currentItemID, "1", mTvStrengthNum.getText().toString(), mTvTimesNum.getText().toString());
                                mRightAdapter.notifyDataSetChanged();
                                hideMenu();
                            }
                        }
                    });
                    showMenu();
                } else if (status == 1) {
                    inputType = status;
                    currentItemID = itemId;
                    mRightAdapter.notifyDataSetChanged();
                } else if (status == 3) {
                    inputType = status;
                    mRightAdapter.notifyDataSetChanged();
                }
            }
        });

        mRightAdapter.setOnItemAddClickListener(new AdapterDataEntryRightListAty.OnItemAddClickListener() {
            @Override
            public void onItemClick(View view, final int position, List<List<String>> mData) {
                currentItemID = mData.get(0).get(0);
                mTvTitle.setText("第" + (mData.size() + 1) + "组");
                mTvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkZero()){
                            add(currentItemID, String.valueOf(position + 1), mTvStrengthNum.getText().toString(), mTvTimesNum.getText().toString());
                            mRightAdapter.notifyDataSetChanged();
                            hideMenu();
                        }
                    }
                });
                mTvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideMenu();
                    }
                });
                showMenu();
            }
        });

        mRightAdapter.setOnItemModifyClickListener(new AdapterDataEntryRightListAty.OnItemModifyClickListener() {
            @Override
            public void onItemClick(View view, final int position, final List<List<String>> mData) {
                currentItemID = mData.get(0).get(0);
                mTvTitle.setText("第" + (position + 1) + "组");
                mTvSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkZero()){
                            ContentValues values = new ContentValues();
                            values.put(DataEntryDbHelper.STRENGTH, mTvStrengthNum.getText().toString());
                            values.put(DataEntryDbHelper.TIMES, mTvTimesNum.getText().toString());
                            int update = mDb.update(mCorderlessonid, values,
                                    "itemid = ? and groups = ? ", new String[]{currentItemID, mData.get(position).get(1)});
                            Log.d("DataEntryActivity", "update:" + update);
                            mRightAdapter.notifyDataSetChanged();
                            hideMenu();
                        }
                    }
                });
                mTvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        delete(currentItemID, mData.get(position).get(1));
//                        mRightAdapter.notifyDataSetChanged();
                        hideMenu();
                    }
                });
                showMenu();
            }
        });

        mRightAdapter.setOnItemModifyLongClickListener(new AdapterDataEntryRightListAty.OnItemModifyLongClickListener() {
            @Override
            public void onItemClick(View view, final int ps, final List<List<String>> mData) {
                new MaterialDialog.Builder(DataEntryActivity.this)
                        .items("修改", "删除")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, final int position, CharSequence text) {
                                if (position == 0) {
                                    currentItemID = mData.get(0).get(0);
                                    mTvTitle.setText("第" + (ps + 1) + "组");
                                    mTvSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ContentValues values = new ContentValues();
                                            values.put(DataEntryDbHelper.STRENGTH, mTvStrengthNum.getText().toString());
                                            values.put(DataEntryDbHelper.TIMES, mTvTimesNum.getText().toString());
                                            int update = mDb.update(mCorderlessonid, values,
                                                    "itemid = ? and groups = ? ", new String[]{currentItemID, mData.get(ps).get(1)});
                                            Log.d("DataEntryActivity", "update:" + update);
                                            mRightAdapter.notifyDataSetChanged();
                                            hideMenu();
                                        }
                                    });
                                    showMenu();
                                } else {
                                    delete(currentItemID, mData.get(ps).get(1));
                                    mRightAdapter.notifyDataSetChanged();
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public void initData() {
        App.getRequestInstance().post(this, UrlPath.URL_MGETTRAININGITEM, null, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray list = object.getJSONArray("list");
                    final JSONObject o = list.getJSONObject(0);
                    final JSONArray items = o.getJSONArray("items");

                    BeanMgettrainingitem result = JsonUtils.object(json, BeanMgettrainingitem.class);
                    mLeftData.clear();
                    mLeftData.addAll(result.getList().get(0).getItems());
                    mLeftAdapter.notifyDataSetChanged();
                    mLeftAdapter.setOnItemClickListener(new AdapterDataEntryLeftListAty.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            try {
                                mLeftAdapter.notifyDataSetChanged();
                                mRightData.clear();
                                JSONObject jsonObject = items.getJSONObject(position);
                                String s = "{ \"items\": " + jsonObject.getJSONArray("items").toString() + "}";
//                                Log.d("DataEntryActivity", s);
                                BeanItems m = JsonUtils.object(s, BeanItems.class);
                                mRightData.addAll(m.getItems());
                                mRightAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    mRightData.clear();
                    JSONObject jsonObject = items.getJSONObject(0);
                    String s = "{ \"items\": " + jsonObject.getJSONArray("items").toString() + "}";
//                    Log.d("DataEntryActivity", s);
                    BeanItems m = JsonUtils.object(s, BeanItems.class);
                    mRightData.addAll(m.getItems());
                    mRightAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(mActivity, "DataEntryActivityjson解析失败", Toast.LENGTH_SHORT).show();
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

    @OnClick({R.id.iv_times_add, R.id.iv_times_reduce, R.id.iv_strength_add,
            R.id.iv_strength_reduce, R.id.tv_save, R.id.tv_delete, R.id.tv_title_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_times_add:
                mTimesNum++;
                mTvTimesNum.setText(String.valueOf(mTimesNum));
                break;
            case R.id.iv_times_reduce:
                if (mTimesNum > 1)
                    mTimesNum--;
                mTvTimesNum.setText(String.valueOf(mTimesNum));
                break;
            case R.id.iv_strength_add:
                mStrengthNum++;
                mTvStrengthNum.setText(String.valueOf(mStrengthNum));
                break;
            case R.id.iv_strength_reduce:
                if (mStrengthNum > 1)
                    mStrengthNum--;
                mTvStrengthNum.setText(String.valueOf(mStrengthNum));
                break;
            case R.id.tv_title_save:
                //保存
                Cursor cursor = mDb.query(mCorderlessonid, null, null,
                        null, null, null, DataEntryDbHelper.GROUPS);
                String head = "{\"detial\":[";
                String end = "]}";
                //cursor!=null&&
                if (cursor.moveToFirst()) {
                    int count = cursor.getCount();
                    for (int i = 1; i <= count; i++) {
                        String item = "{\"groups\": \"" + cursor.getString(2) + "\",\"strength\": \""
                                + cursor.getString(3) + "\",\"times\": \"" + cursor.getString(4) +
                                "\",\"itemid\":\"" + cursor.getString(1) + "\",\"remark\":\"adasasdaa\"}";
                        cursor.moveToNext();
                        if (i != count) {
                            item += ",";
                        } else {
                            item += end;
                        }
                        head += item;
                    }
                }
                cursor.close();
                Log.d("DataEntryActivity", head);
                RequestParams params = new RequestParams();
                params.put("corderlessonid", mCorderlessonid);
                params.put("trainingdetial", head);
                params.put("memid", SPUtils.getString(Constant.MEMID));
                App.getRequestInstance().post(DataEntryActivity.this, UrlPath.URL_MCONFIRMLESSION, params, new RequestListener() {
                    @Override
                    public void requestSuccess(String json) {
                        Log.d("DataEntryActivity", json);
                        try {
                            JSONObject object = new JSONObject(json);
                            Object msgFlag = object.get("msgFlag");
                            if ("1".equals(msgFlag.toString())) {
                                Toast.makeText(mActivity, "数据录入成功", Toast.LENGTH_SHORT).show();
                                DataEntryActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mActivity, "返回数据解析失败", Toast.LENGTH_SHORT).show();
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

                break;
            case R.id.tv_delete:
                if (inputType == 0) {
                    mRightAdapter.notifyDataSetChanged();
                    hideMenu();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (mFlEdit.getVisibility() == View.VISIBLE) {
            hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    public Cursor query(String itemid) {
        Cursor c = mDb.query(mCorderlessonid, null, DataEntryDbHelper.ITEMID + "=?",
                new String[]{itemid}, null, null, DataEntryDbHelper.GROUPS+" asc");
        return c;
    }

    public boolean hasData(String itemid) {
        Cursor c = mDb.rawQuery("select * from " + mCorderlessonid + " where itemid=?", new String[]{itemid});
        int count = c.getCount();
        return count > 0;
    }

    public void add(String itemId, String groups, String strength, String times) {
        ContentValues values = new ContentValues();
        values.put(DataEntryDbHelper.ITEMID, itemId);
        values.put(DataEntryDbHelper.GROUPS, groups);
        values.put(DataEntryDbHelper.STRENGTH, strength);
        values.put(DataEntryDbHelper.TIMES, times);
        long biaoming = mDb.insert(mCorderlessonid, null, values);
    }

    //             1               2 -1
    public void upData(String groupsFrom, String groupsTo, String itemId) {
        try {
            Log.d("AdapterDataEntryContent", "from" + groupsFrom + "-----to:" + groupsTo);
            mDb.beginTransaction();
            if (Integer.parseInt(groupsFrom)<Integer.parseInt(groupsTo)){
                ContentValues values = new ContentValues();
                values.put(DataEntryDbHelper.GROUPS, "-1");
                int update = mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?", new String[]{itemId, groupsFrom});
                values.clear();
                for (int i = Integer.parseInt(groupsFrom); i <= Integer.parseInt(groupsTo); i++) {
                    values.put(DataEntryDbHelper.GROUPS, String.valueOf(i-1));
                    mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?",
                            new String[]{itemId, String.valueOf(i)});
                    values.clear();
                }
                values.clear();
                values.put(DataEntryDbHelper.GROUPS, groupsTo);
                int update2 = mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?", new String[]{itemId, "-1"});
            }else {
                ContentValues values = new ContentValues();
                values.put(DataEntryDbHelper.GROUPS, "-1");
                int update = mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?", new String[]{itemId, groupsFrom});
                values.clear();
                for (int i = Integer.parseInt(groupsFrom)-1; i >=Integer.parseInt(groupsTo); i--) {
                    values.put(DataEntryDbHelper.GROUPS, String.valueOf(i+1));
                    mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?",
                            new String[]{itemId, String.valueOf(i)});
                    values.clear();
                }
                values.clear();
                values.put(DataEntryDbHelper.GROUPS, groupsTo);
                int update2 = mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?", new String[]{itemId, "-1"});
            }
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(mActivity, "位置更换失败", Toast.LENGTH_SHORT).show();
        } finally {
            mDb.endTransaction();
        }
    }

    public void delete(String itemID, String groups) {
        int delete = mDb.delete(mCorderlessonid, DataEntryDbHelper.ITEMID + " = ? and " +
                DataEntryDbHelper.GROUPS + " = ?", new String[]{itemID, groups});
        Log.d("DataEntryActivity", "delete:" + delete);
        Cursor c = mDb.query(mCorderlessonid, null, DataEntryDbHelper.ITEMID + "=?",
                new String[]{itemID}, null, null, DataEntryDbHelper.GROUPS);
        int count = c.getCount();
        c.close();
        try {
            mDb.beginTransaction();
            ContentValues values = new ContentValues();
            for (int i = Integer.parseInt(groups); i <= count; i++) {
                values.put(DataEntryDbHelper.GROUPS, String.valueOf(i));
                int update = mDb.update(mCorderlessonid, values, "itemid = ? and groups = ?",
                        new String[]{itemID, String.valueOf(i + 1)});
                values.clear();
            }
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(mActivity, "位置更换失败", Toast.LENGTH_SHORT).show();
        } finally {
            mDb.endTransaction();
        }
    }

    public void hideMenu() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        mFlEdit.setVisibility(View.GONE);
        mFlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.dd_mask_out));
        mLlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_out));
        mTimesNum = 1;
        mStrengthNum = 1;
        mTvTimesNum.setText(String.valueOf(mTimesNum));
        mTvStrengthNum.setText(String.valueOf(mStrengthNum));
    }

    public void showMenu() {
        mFlEdit.setVisibility(View.VISIBLE);
        mFlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.dd_mask_in));
        mLlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_in));
    }

    public boolean checkZero(){
        if ( TextUtils.isEmpty(mTvTimesNum.getText().toString())||
                mTvTimesNum.getText().toString().substring(0,1).equals("0") ){
            Toast.makeText(mActivity, "请填写合乎逻辑的训练次数", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ( TextUtils.isEmpty(mTvStrengthNum.getText().toString())||
                mTvStrengthNum.getText().toString().substring(0,1).equals("0")){
            Toast.makeText(mActivity, "请填写合乎逻辑的训练强度", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
