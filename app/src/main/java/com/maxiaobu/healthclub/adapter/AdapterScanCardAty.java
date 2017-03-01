package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanWallet;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.rx.RxBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 * 会员卡列表
 */
public class AdapterScanCardAty extends RecyclerView.Adapter {
    public static class TapEvent {
        BeanWallet.CardListBean cardListBean;
        BeanWallet.CorderListBean corderListBean;
        public void setData(BeanWallet.CardListBean cardListBean) {
            this.cardListBean = cardListBean;
        }

        public BeanWallet.CardListBean getData() {
            return cardListBean;
        }

        public void setData1(BeanWallet.CorderListBean corderListBean) {
            this.corderListBean = corderListBean;
        }
        public BeanWallet.CorderListBean getData1() {
            return corderListBean;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String tarid);
    }

    public OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private Activity mActivity;
    private BeanWallet mData;
    private RxBus mRxBus;

    public AdapterScanCardAty(Activity activity, BeanWallet mData) {
        mActivity = activity;
        this.mData = mData;
        mRxBus = App.getRxBusSingleton();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_scan_card_title_aty, parent, false);
            return new TitleViewHolder(v);
        } else if (viewType == 2) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_scan_card_member_aty, parent, false);
            return new CardViewHolder(v);
        } else if (viewType == 3) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_scan_card_title_aty, parent, false);
            return new TitleViewHolder(v);
        } else if (viewType == 4) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_scan_card_order_aty, parent, false);
            return new OrderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_scan_card_empty_aty, parent, false);
            return new EmptyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.mTvTitle.setText("会员卡");
        } else if (holder instanceof CardViewHolder) {
            BeanWallet.CardListBean cardListBean = mData.getCardList().get(position - 1);
            CardViewHolder cardViewHolder = (CardViewHolder) holder;
            cardViewHolder.mTvClubName.setText(cardListBean.getClubname());
            String time = TimesUtil.timestampToStringS(String.valueOf(cardListBean.getOrdenddate().getTime()), "yyyy/MM/dd");
            cardViewHolder.mTvTime.setText(time);
            cardViewHolder.mTvBalance.setText("不知道哪个字段");
            int num = cardListBean.getCoursetimes() - cardListBean.getCoursenum();
            cardViewHolder.mTvNumber.setText("剩余次数： "+ num +"次");
            cardViewHolder.mLyRoot.setOnClickListener(v -> {
                TapEvent tapEvent = new TapEvent();
                tapEvent.setData(cardListBean);
                if (mRxBus.hasObservers()) {
                    mRxBus.send(tapEvent);
                }
                mActivity.finish();
            });
        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.mTvTitle.setText("订单劵");
        } else if (holder instanceof OrderViewHolder) {
//            Log.d("AdapterScanCardAty", "position:" + position);
//            Log.d("AdapterScanCardAty", "mData.getCardList().size():" + mData.getCardList().size());
            BeanWallet.CorderListBean corderListBean = mData.getCorderList().get(position -(mData.getCardList().size()==0?3:(2 +mData.getCardList().size())));
            OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
            orderViewHolder.mTvName.setText(corderListBean.getCoursename());
            orderViewHolder.mTvType.setText(corderListBean.getClubname());
            orderViewHolder.mTvPrice.setText(corderListBean.getOrdamt() + "元");
            int m = Integer.valueOf(corderListBean.getCoursetimes()) - Integer.valueOf(corderListBean.getCoursenum()) ;
            orderViewHolder.mTvNumber.setText("剩余次数： "+ m +"次");
            String time = TimesUtil.timestampToStringS(String.valueOf(corderListBean.getOrdenddate().getTime()), "yyyy/MM/dd");
            orderViewHolder.mTvTime.setText("截至时间： " + time);
            orderViewHolder.mLyRoot.setOnClickListener(v -> {
                TapEvent tapEvent = new TapEvent();
                tapEvent.setData1(corderListBean);
                if (mRxBus.hasObservers()) {
                    mRxBus.send(tapEvent);
                }
                mActivity.finish();
            });
        } else if (position == getItemCount()-1) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.mIvEmpty.setImageResource(R.mipmap.bg_scan_card_order_empty);
        } else {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.mIvEmpty.setImageResource(R.mipmap.bg_scan_card_empty);
        }
    }

    @Override
    public int getItemCount() {
        int i = mData.getCardList().size() == 0 ? 1 : mData.getCardList().size();
        int ii = mData.getCorderList().size() == 0 ? 1 : mData.getCorderList().size();
        return i + ii + 2;
    }

    @Override
    public int getItemViewType(int position) {
        int i = getItemCount();
        Log.d("AdapterScanCardAty", "i:" + i);
        if (position == 0) {
            return 1;
        }
        if (mData.getCardList().size() != 0) {
            if (position < mData.getCardList().size() + 1)
                return 2;
        } else {
            if (position == 1) {
                return 5;
            }
        }
        if (position ==(mData.getCardList().size()==0? (mData.getCardList().size() + 2):(mData.getCardList().size()+1)))
            return 3;
        if (mData.getCorderList().size() != 0) {
            if (position > mData.getCardList().size())
                return 4;
        } else {
            if (position == getItemCount()-1) {
                return 6;
            }
        }
        return super.getItemViewType(position);
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.ly_root)
        RelativeLayout mLyRoot;
        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_empty)
        ImageView mIvEmpty;
        @Bind(R.id.ly_root)
        RelativeLayout mLyRoot;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.tv_club_name)
        TextView mTvClubName;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_number)
        TextView mTvNumber;
        @Bind(R.id.tv_balance)
        TextView mTvBalance;
        @Bind(R.id.ly_root)
        RelativeLayout mLyRoot;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_type)
        TextView mTvType;
        @Bind(R.id.view)
        View mView;
        @Bind(R.id.tv_number)
        TextView mTvNumber;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.ly_root)
        RelativeLayout mLyRoot;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
