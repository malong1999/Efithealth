package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 莫小婷 on 2017/1/7.
 */

public class BeanMineDynamic {
    private String name;
    private String Image;
    private String ContentText;
    private String ContentImage;
    private String loveNum;
    private String CommentNum;
    private String CreateTime;
    private String visiable;
    private Boolean loveFalg;
    private String bigImage;
    private String postId;
    private String authId;
    private String memrole;

    public String getMemrole() {
        return memrole;
    }

    public void setMemrole(String memrole) {
        this.memrole = memrole;
    }

    private List<String> LikeList;

    public List<String> getLikeList() {
        return LikeList;
    }

    public void setLikeList(List<String> likeList) {
        LikeList = likeList;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    private  int islike;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public Boolean getLoveFalg() {
        return loveFalg;
    }

    public void setLoveFalg(Boolean loveFalg) {
        this.loveFalg = loveFalg;
    }

    public String getCommentNum() {
        return CommentNum;
    }

    public void setCommentNum(String commentNum) {
        CommentNum = commentNum;
    }

    public String getContentImage() {
        return ContentImage;
    }

    public void setContentImage(String contentImage) {
        ContentImage = contentImage;
    }

    public String getContentText() {
        return ContentText;
    }

    public void setContentText(String contentText) {
        ContentText = contentText;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(String loveNum) {
        this.loveNum = loveNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisiable() {
        return visiable;
    }

    public void setVisiable(String visiable) {
        this.visiable = visiable;
    }
}
