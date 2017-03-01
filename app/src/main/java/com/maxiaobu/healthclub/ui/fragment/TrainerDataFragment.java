package com.maxiaobu.healthclub.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterClubListAty;
import com.maxiaobu.healthclub.common.beangson.BeanClubList;
import com.maxiaobu.healthclub.common.beangson.BeanTrainerData;
import com.maxiaobu.healthclub.common.beangson.BeanmDynamicList;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.MyGridView;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.nineoldandroids.view.ViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.maxiaobu.healthclub.R.mipmap.ic_personlly_birth;

/**
 * 教练个人简介
 */
public class TrainerDataFragment extends BaseFrg {


    @Bind(R.id.grid_view)
    RecyclerView mGridView;
    @Bind(R.id.tv_user_birth)
    TextView mTvUserBirth;
    private List<BeanTrainerData> mBean;
    private Adapter mAdapter;

    public static Fragment getInstance() {
        TrainerDataFragment instance = new TrainerDataFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer_data, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {

        mBean = new ArrayList<>();
        mAdapter = new Adapter(getActivity());
        mAdapter.setList(mBean);
        mGridView.setHasFixedSize(true);
        mGridView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManagerPersonal.setSmoothScrollbarEnabled(true);
        layoutManagerPersonal.setAutoMeasureEnabled(true);
        mGridView.setLayoutManager(layoutManagerPersonal);
        mGridView.setItemAnimator(new DefaultItemAnimator());
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {

    }

    @Subscribe
    public void getValue(BeanmDynamicList.BBMemberBean bean) {
        if (null != bean) {
//            mTvUserBirth.setText(TimesUtil.timestampToStringS(String.valueOf(bean.getBirthday().getTime()), "yyyy-hh-dd"));
            BeanTrainerData b1 = new BeanTrainerData();
            b1.setImageId(R.mipmap.ic_personlly_nickname);
            b1.setText("昵称");
            b1.setContent(bean.getNickname());
            mBean.add(b1);
            BeanTrainerData b2 = new BeanTrainerData();
            b2.setImageId(R.mipmap.ic_personlly_sign);
            b2.setText("签名");
            if (bean.getSignature().equals("") || null == bean.getSignature()) {
                b2.setContent("暂无签名");
            } else {
                b2.setContent(bean.getSignature());
            }
            mBean.add(b2);
            BeanTrainerData b3 = new BeanTrainerData();
            b3.setImageId(R.mipmap.ic_personlly_sex);
            b3.setText("性别");
            b3.setContent(bean.getGendername());
            mBean.add(b3);
            BeanTrainerData b4 = new BeanTrainerData();
            b4.setImageId(R.mipmap.ic_personlly_local);
            b4.setText("认证");
            if (bean.getCurMemrole().equals("coach")) {
                b4.setContent("教练");
            } else {
                b4.setContent("会员");
            }
            mBean.add(b4);
            BeanTrainerData b5 = new BeanTrainerData();
            b5.setImageId(R.mipmap.ic_personlly_birth);
            b5.setText("出生日期");
            b5.setContent(TimesUtil.timestampToStringS(String.valueOf(bean.getBirthday().getTime()), "yyyy-hh-dd"));
            mBean.add(b5);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private Context mContext;
        private List<BeanTrainerData> mList;

        public void setList(List<BeanTrainerData> list) {
            mList = list;
        }

        public Adapter(Context context) {
            mContext = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_trainer_data, parent, false);
            return new ViewHolder(v);
        }



        @Override
        public void onBindViewHolder(ViewHolder holder,  int position) {
            holder.mItemImage.setImageResource(mList.get(position).getImageId());
            holder.mItemText.setText(mList.get(position).getText());
            holder.mItemUserName.setText(mList.get(position).getContent());
            if (position == mList.size()-1) {
                holder.mItemUserBirth.setVisibility(View.VISIBLE);
                holder.mItemUserBirth.setText(mList.get(position).getContent());
                holder.mItemUserName.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder  extends RecyclerView.ViewHolder {
            @Bind(R.id.item_image)
            ImageView mItemImage;
            @Bind(R.id.item_text)
            TextView mItemText;
            @Bind(R.id.item_user_name)
            TextView mItemUserName;
            @Bind(R.id.tv_user_birth)
            TextView mItemUserBirth;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
