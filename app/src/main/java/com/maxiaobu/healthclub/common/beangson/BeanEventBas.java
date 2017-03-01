package com.maxiaobu.healthclub.common.beangson;

import com.baidu.mapapi.model.LatLng;

import maxiaobu.mxbutilscodelibrary.ProcessUtils;

/**
 * Created by 莫小婷 on 2016/11/17.
 */

public class BeanEventBas {

    private LatLng mLatLng;

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }


    /**
     * 是否刷新群聊详情页
     */
    private Boolean refresh;

    public Boolean getRefresh() {
        return refresh;
    }

    /**
     * 刷新群详情和个人页动态信息
     * @param refresh
     */
    public void setRefresh(Boolean refresh) {
        this.refresh = refresh;
    }


    /**
     * 加载个人动态信息
     */
    private Boolean falg;

    public Boolean getFalg() {
        return falg;
    }

    public void setFalg(Boolean falg) {
        this.falg = falg;
    }

    /**
     * 好友动态广场选择是否看私密
     */
    private Integer friendRefresh;

    public Integer getFriendRefresh() {
        return friendRefresh;
    }

    public void setFriendRefresh(Integer friendRefresh) {
        this.friendRefresh = friendRefresh;
    }

    /**
     * 刷新点赞
     */
    private Integer pos;
    private Boolean mBoolean;
    private String num;
    private String postId;
    //判断是从什么地方开始发出的EventBus
    private Boolean f;
    //评论数
    private String CommentNum;

    public String getCommentNum() {
        return CommentNum;
    }

    public void setCommentNum(String commentNum) {
        CommentNum = commentNum;
    }

    public Boolean getF() {
        return f;
    }

    public void setF(Boolean f) {
        this.f = f;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Boolean getBoolean() {
        return mBoolean;
    }

    public void setBoolean(Boolean aBoolean) {
        mBoolean = aBoolean;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    /**
     * 评论删除通知
     */
    private Boolean commentDelete;
    private String commentPostId;
    //这个是评论标志，当从评论详情删除评论时候，个人页和广场页都刷新，当从个人页删除的时候，只刷新广场
    private Boolean commentFlag;

    public Boolean getCommentFlag() {
        return commentFlag;
    }

    public void setCommentFlag(Boolean commentFlag) {
        this.commentFlag = commentFlag;
    }

    public String getCommentPostId() {
        return commentPostId;
    }

    public void setCommentPostId(String commentPostId) {
        this.commentPostId = commentPostId;
    }

    public Boolean getCommentDelete() {
        return commentDelete;
    }

    public void setCommentDelete(Boolean commentDelete) {
        this.commentDelete = commentDelete;
    }

    /**
     * 发动态通知广场动态跟新
     */
    public Boolean squareDynamicFalg;

    public Boolean getSquareDynamicFalg() {
        return squareDynamicFalg;
    }

    public void setSquareDynamicFalg(Boolean squareDynamicFalg) {
        this.squareDynamicFalg = squareDynamicFalg;
    }

    /**
     * 广场点赞动态刷新好友动态
     */
    public Boolean squareRefresh;
    public Integer loveNum;
    public String Postid;
    public Boolean square2friendFalg;
    public Boolean friend2squareFalg;

    public Boolean getFriend2squareFalg() {
        return friend2squareFalg;
    }

    public void setFriend2squareFalg(Boolean friend2squareFalg) {
        this.friend2squareFalg = friend2squareFalg;
    }

    public Boolean getSquare2friendFalg() {
        return square2friendFalg;
    }

    public void setSquare2friendFalg(Boolean square2friendFalg) {
        this.square2friendFalg = square2friendFalg;
    }

    public Integer getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(Integer loveNum) {
        this.loveNum = loveNum;
    }

    public String getPostid() {
        return Postid;
    }

    public void setPostid(String postid) {
        Postid = postid;
    }

    public Boolean getSquareRefresh() {
        return squareRefresh;
    }

    public void setSquareRefresh(Boolean squareRefresh) {
        this.squareRefresh = squareRefresh;
    }

    /**
     * 广场添加动态刷新
     */
    public Boolean addDynamicFalg;

    public Boolean getAddDynamicFalg() {
        return addDynamicFalg;
    }

    public void setAddDynamicFalg(Boolean addDynamicFalg) {
        this.addDynamicFalg = addDynamicFalg;
    }

    /**
     * 关注数量+1
     */
    public Boolean addAttention;

    public Boolean getAddAttention() {
        return addAttention;
    }

    public void setAddAttention(Boolean addAttention) {
        this.addAttention = addAttention;
    }

    /**
     * 关注数量-1
     */
    public Boolean cancleAttention;

    public Boolean getCancleAttention() {
        return cancleAttention;
    }

    public void setCancleAttention(Boolean cancleAttention) {
        this.cancleAttention = cancleAttention;
    }
}
