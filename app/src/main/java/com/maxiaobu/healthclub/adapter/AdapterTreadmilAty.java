package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.utils.excel.MyHorizontalScrollView;

import java.util.ArrayList;

import static com.maxiaobu.healthclub.R.id.view;

/**
 * Created by 马小布 on 2017/2/21：10:29.
 * email：maxiaobu1216@gmail.com
 * 功能：
 * 伪码：
 * 待完成：
 */

public class AdapterTreadmilAty extends BaseAdapter {

    private ArrayList<String> datas;
    private Context context;
    private ArrayList<MyHorizontalScrollView> mScrollLists;

    public AdapterTreadmilAty(Context context, ArrayList<String> datas, ArrayList<MyHorizontalScrollView> mScrollLists) {
        this.context = context;
        this.datas = datas;
        this.mScrollLists = mScrollLists;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {

        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.hor_scroll_item, null);
            holder.myHorizontalScrollView = (MyHorizontalScrollView) view.findViewById(R.id.hor_scrollview);
            holder.tvData = (TextView) view.findViewById(R.id.tv_data);

            //将每一个MyHorizontalScrollView对象引用都添加到一个数组中
            mScrollLists.add((MyHorizontalScrollView) holder.myHorizontalScrollView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvData.setText("10." + i);
        return view;
    }

    class ViewHolder {
        MyHorizontalScrollView myHorizontalScrollView;
        TextView tvData;
    }
}
