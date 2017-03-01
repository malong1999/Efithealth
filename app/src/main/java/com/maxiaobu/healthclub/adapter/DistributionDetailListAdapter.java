package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanFoodOrderDetail;
import com.maxiaobu.healthclub.utils.TimesUtil;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/15.
 * 配送页面
 */
public class DistributionDetailListAdapter extends BaseAdapter {

    private Context mContext;
    private BeanFoodOrderDetail mList;

    public DistributionDetailListAdapter(Context context) {
        mContext = context;
    }

    public void setList(BeanFoodOrderDetail list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.getDelvyList().size();
    }

    @Override
    public Object getItem(int position) {
        return mList.getDelvyList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lv_distribution_detail_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position != 0&&mList.getDelvyList().get(position-1).getDelvystr().equals(mList.getDelvyList().get(position).getDelvystr())){
            holder.mTvDistributionDate.setText("\t\t\t\t\t\t\t\t\t\t\t");
        }else {
            holder.mTvDistributionDate.setText(mList.getDelvyList().get(position).getDelvystr());
        }

        holder.mTvDistributionStar.setText(mList.getDelvyList().get(position).getDayOfWeek());
        holder.mTvDistributionTime.setText(mList.getDelvyList().get(position).getDlvtimestr());

        if(mList.getDelvyList().get(position).getStatus().equals("1")){
            long currtionTime  = System.currentTimeMillis();
            if(currtionTime - mList.getDelvyList().get(position).getDeliverydate().getTime() >= 0){
                holder.mTvDistributionText.setText("待配送");
            }else {
                holder.mTvDistributionText.setText("未配送");
            }

        }else if(mList.getDelvyList().get(position).getStatus().equals("2")){
            holder.mTvDistributionText.setText("已完成");
        }else {
            holder.mTvDistributionText.setText("已取消");
        }


        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.tv_distribution_date)
        TextView mTvDistributionDate;
        @Bind(R.id.tv_distribution_star)
        TextView mTvDistributionStar;
        @Bind(R.id.tv_distribution_time)
        TextView mTvDistributionTime;
        @Bind(R.id.tv_distribution_text)
        TextView mTvDistributionText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
