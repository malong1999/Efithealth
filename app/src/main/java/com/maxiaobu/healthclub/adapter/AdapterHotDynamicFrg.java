package com.maxiaobu.healthclub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanClubList;
import com.maxiaobu.healthclub.common.beangson.BeanHotDynamic;
import com.maxiaobu.healthclub.ui.activity.InviteGroupActivity;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import maxiaobu.mxbutilscodelibrary.ConstUtils;

/**
 * Created by 马小布 on 2016/8/31.
 * 热门动态
 */
public class AdapterHotDynamicFrg extends RecyclerView.Adapter implements BGABanner.OnItemClickListener, BGABanner.Adapter {

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Glide.with(mContext)
                .load(model)
                .placeholder(R.mipmap.ic_place_holder)
                .error(R.mipmap.test_header)
                .into((ImageView) view);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {

    }

    public interface OnItemClickListener {
        void onItemClick(View view, String tarid);
    }

    public interface OnItemClickWebLinstener{
        void onItemWeb();
    }

    public OnItemClickListener mListener;
    private OnItemClickWebLinstener mWebLinstener;

    public void setWebLinstener(OnItemClickWebLinstener webLinstener) {
        mWebLinstener = webLinstener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private List<BeanHotDynamic.ListBean> mLists;
    private OnListenGoodClickLayout mOnListenGoodClickLayout;
    private OnListenCommentClickLayout mOnListenCommentClickLayout;
    private OnListenShareClickLayout mOnListenShareClickLayout;
    private OnListenMoreClickLayout mOnListenMoreClickLayout;
    private OnListenItemClick mOnListenItemClick;
    private OnListenItemImageClick mOnListenItemImageClick;
    private OnListenHeadImage mOnListenHeadImage;

    public AdapterHotDynamicFrg(Context context, List<BeanHotDynamic.ListBean> mData) {
        mContext = context;
        this.mLists = mData;
    }

    public void setOnListenHeadImage(OnListenHeadImage onListenHeadImage) {
        mOnListenHeadImage = onListenHeadImage;
    }

    public void setOnListenItemImageClick(OnListenItemImageClick onListenItemImageClick) {
        mOnListenItemImageClick = onListenItemImageClick;
    }

    public void setOnListenItemClick(OnListenItemClick onListenItemClick) {
        mOnListenItemClick = onListenItemClick;
    }

    public void setOnListenMoreClickLayout(OnListenMoreClickLayout onListenMoreClickLayout) {
        mOnListenMoreClickLayout = onListenMoreClickLayout;
    }

    public void setOnListenShareClickLayout(OnListenShareClickLayout onListenShareClickLayout) {
        mOnListenShareClickLayout = onListenShareClickLayout;
    }

    public void setOnListenCommentClickLayout(OnListenCommentClickLayout onListenCommentClickLayout) {
        mOnListenCommentClickLayout = onListenCommentClickLayout;
    }

    public void setOnListenGoodClickLayout(OnListenGoodClickLayout onListenGoodClickLayout) {
        mOnListenGoodClickLayout = onListenGoodClickLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_hot_dynamic_header, parent, false);
            return new MyHeaderViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_dynamic, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position != 0) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.mItemImage.setBorderWidth(3);
            myViewHolder.mItemImage.setBorderColor(0xffc4c4c4);
            myViewHolder.mItemName.setText(mLists.get(position-1).getAuthnickname());
            Glide.with(mContext)
                    .load(mLists.get(position-1).getAuthimgsfilename())
                    .into(myViewHolder.mItemImage);
            myViewHolder.mItemTime.setText(getFriendlyTimeSpanByNow(mLists.get(position-1).getCreatetime().getTime()));
            myViewHolder.mItemContentText.setText(mLists.get(position-1).getContent());
            Picasso.with(mContext).load(mLists.get(position-1).getImgsfilename())
                    .into(myViewHolder.mItemContentImage);
            myViewHolder.mItemGoodNum.setText(mLists.get(position-1).getLikecount()+"");

            myViewHolder.mItemCommentNum.setText(mLists.get(position-1).getCommentcount()+"");

//        Log.d("mytt", mLists.get(position).getPostId());
//        Log.d("wjn", "num.get(position):" + position + " ----->" + num.get(position) + "----valuse:" + holder.mItemGoodNum.getText().toString());

            myViewHolder.mItemLayout.setOnClickListener(v -> {
                if (mOnListenItemClick != null) {
                    mOnListenItemClick.onItemListen(mLists.get(position-1).getPostid(), mLists.get(position-1).getVisiable(), position-1, mLists.get(position-1).getAuthid(),mLists.get(position-1).getLikecount(),mLists.get(position-1).getCommentcount());
                }
            });
            myViewHolder.mItemGoodLayout.setOnClickListener(v -> {
                if (mOnListenGoodClickLayout != null)
                    mOnListenGoodClickLayout.onClickGoodListen(myViewHolder.mItemGoodImage, myViewHolder.mItemGoodNum, position-1);
            });
            myViewHolder.mItemGoodImage.setOnClickListener(v -> {
                if (mOnListenGoodClickLayout != null)
                    mOnListenGoodClickLayout.onClickGoodListen(myViewHolder.mItemGoodImage, myViewHolder.mItemGoodNum, position-1);
            });
            myViewHolder.mItemCommentLayout.setOnTouchListener((v, event) -> {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mOnListenCommentClickLayout != null)
                        mOnListenCommentClickLayout.onClickCommentListenDown(myViewHolder.mItemCommentImage);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mOnListenCommentClickLayout != null)
                        mOnListenCommentClickLayout.onClickCommentListenUp(myViewHolder.mItemCommentImage, position-1, mLists.get(position-1).getPostid(), mLists.get(position-1).getVisiable(), mLists.get(position-1).getAuthid(),mLists.get(position-1).getLikecount(),mLists.get(position-1).getCommentcount());
                }
                return true;
            });
            myViewHolder.mItemCommentImage.setOnClickListener(v -> {
                if (mOnListenCommentClickLayout != null)
                    mOnListenCommentClickLayout.onClickCommentListenUp(myViewHolder.mItemCommentImage, position-1, mLists.get(position-1).getPostid(), mLists.get(position-1).getVisiable(), mLists.get(position-1).getAuthid(),mLists.get(position-1).getLikecount(),mLists.get(position-1).getCommentcount());
            });

            myViewHolder.mItemShare.setOnClickListener(v -> {
                if (mOnListenShareClickLayout != null) {

                    mOnListenShareClickLayout.onClickShareListenUp(myViewHolder.mItemShare, position-1);
                }
            });

            myViewHolder.mItemMore.setOnClickListener(v -> {
                if (mOnListenMoreClickLayout != null) {

                    mOnListenMoreClickLayout.onClickMoreListenUp(myViewHolder.mItemMore, position-1 , mLists.get(position-1).getAuthid() , mLists.get(position-1).getPostid());
                }
            });
            myViewHolder.mItemContentImage.setOnClickListener(v -> {
                mOnListenItemImageClick.onImageClick(v, myViewHolder.mItemContentImage, position-1, mLists.get(position-1).getImgpfilename());
            });
            myViewHolder.mItemImage.setOnClickListener(view -> mOnListenHeadImage.onListenClick(position-1,mLists.get(position-1).getAuthid(),mLists.get(position-1).getMemrole()));

            if (mLists.get(position-1).getIslike() == 0) {
                myViewHolder.mItemGoodImage.setBackgroundResource(R.mipmap.ic_dynamic_good_df);
            } else {
                myViewHolder.mItemGoodImage.setBackgroundResource(R.mipmap.ic_dynamic_good_dw);
            }

        } else {
            final MyHeaderViewHolder viewHolder = (MyHeaderViewHolder) holder;
            List<Integer> mIntegers = new ArrayList<>();
            mIntegers.add(R.mipmap.pic_01);
            mIntegers.add(R.mipmap.pic_02);
            mIntegers.add(R.mipmap.pic_lb);
            viewHolder.mBannerMainDepth.setAdapter(this);
            viewHolder.mBannerMainDepth.setData(mIntegers, null);
            viewHolder.mLyEnterGroup.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, InviteGroupActivity.class)));
            viewHolder.mLyTips.setOnClickListener(view -> {
                if(mWebLinstener != null){
                    mWebLinstener.onItemWeb();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mLists.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;

        return 1;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_image)
        CircularImageView mItemImage;
        @Bind(R.id.item_name)
        TextView mItemName;
        @Bind(R.id.item_time)
        TextView mItemTime;
        @Bind(R.id.item_content_text)
        TextView mItemContentText;
        @Bind(R.id.item_content_image)
        ImageView mItemContentImage;
        @Bind(R.id.item_good_image)
        CheckBox mItemGoodImage;
        @Bind(R.id.item_good_num)
        TextView mItemGoodNum;
        @Bind(R.id.item_good_layout)
        LinearLayout mItemGoodLayout;
        @Bind(R.id.item_comment_image)
        ImageButton mItemCommentImage;
        @Bind(R.id.item_comment_num)
        TextView mItemCommentNum;
        @Bind(R.id.item_comment_layout)
        LinearLayout mItemCommentLayout;
        @Bind(R.id.item_share)
        ImageView mItemShare;
        @Bind(R.id.item_more)
        ImageView mItemMore;
        @Bind(R.id.item_layout)
        LinearLayout mItemLayout;
        @Bind(R.id.item_view)
        View mItemView;
        @Bind(R.id.item_premission_image)
        ImageView mItemPremissionImage;
        @Bind(R.id.item_premission_text)
        TextView mItemPremissionText;
        @Bind(R.id.ll_layout)
        LinearLayout mLlLayout;
        @Bind(R.id.item_view_head)
        View mItemViewHead;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class MyHeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.banner_main_depth)
        BGABanner mBannerMainDepth;
        @Bind(R.id.ly_enter_group)
        LinearLayout mLyEnterGroup;
        @Bind(R.id.ly_tips)
        LinearLayout mLyTips;
        @Bind(R.id.ly_same_city)
        LinearLayout mLySameCity;


        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnListenGoodClickLayout {
        void onClickGoodListen(CheckBox goodCheckBox, TextView textView, int pos);

    }

    public interface OnListenCommentClickLayout {
        void onClickCommentListenDown(ImageButton commentImageButton);

        void onClickCommentListenUp(ImageButton commentImageButton, int pos, String postid, String visiable, String authId, Integer likeNum , Integer commentNum);
    }

    public interface OnListenShareClickLayout {
        void onClickShareListenUp(ImageView shareImageButton, int pos);
    }

    public interface OnListenMoreClickLayout {
        void onClickMoreListenUp(ImageView moreImageButton, int pos,String memid,String postId);
    }

    public interface OnListenItemClick {
        void onItemListen(String postid, String visiable, int i, String authId,Integer likeNum , Integer commentNum);
    }

    public interface OnListenItemImageClick {
        void onImageClick(View view, ImageView imageView, int pos, String image);
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    @SuppressLint("DefaultLocale")
    public String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            return String.format("%tc", millis);// U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
        if (span < 1000) {
            return "刚刚";
        } else if (span < ConstUtils.MIN) {
            return String.format("%d秒前", span / ConstUtils.SEC);
        } else if (span < ConstUtils.HOUR) {
            return String.format("%d分钟前", span / ConstUtils.MIN);
        }

        // 获取当天00:00
        long wee = (now / ConstUtils.DAY) * ConstUtils.DAY;
        if (millis >= wee) {
            long day = span / (24 * 60 * 60 * 1000);
            long hour = (span / (60 * 60 * 1000) - day * 24);
            return hour + "小时前";
        } else if (millis >= wee - ConstUtils.DAY) {
            return "昨天";
        } else {
            long day = span / (24 * 60 * 60 * 1000);
            if (day >= 30) {
                long mon = day / 30;
                return mon + "个月前";
            } else {
                return day + "天前";
            }
        }

    }

    public interface OnListenHeadImage{
        void onListenClick(int pos , String authId,String authMemrole);
    }
}
