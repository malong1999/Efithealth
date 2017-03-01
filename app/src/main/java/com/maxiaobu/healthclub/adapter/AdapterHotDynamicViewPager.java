package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maxiaobu.healthclub.R;

import java.util.List;

/**
 * Created by 莫小婷 on 2016/12/9.
 * 没用
 */

public class AdapterHotDynamicViewPager extends PagerAdapter {

    private List<Integer> mIntegers;
    private Context mContext;

    public AdapterHotDynamicViewPager(Context context, List<Integer> integers) {
        mContext = context;
        mIntegers = integers;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vp,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        imageView.setImageResource(mIntegers.get(position % mIntegers.size()));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
