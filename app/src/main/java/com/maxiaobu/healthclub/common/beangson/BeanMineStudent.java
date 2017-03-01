package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class BeanMineStudent {

    /**
     * msgFlag : 1
     * msgContent : 我的学员
     * coachmemberlist : [{"begintime":"","clubid":"","clubname":"","coachcert":"","corderlessonid":"","coursename":"","coursestatus":"","evastatus":"","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000520_1476153333317.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000520_1476153333317.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000520","memrole":"coach","nickname":"沫沫","ordtype":"","signature":"我写点什么吧"},{"begintime":"","clubid":"","clubname":"","coachcert":"","corderlessonid":"","coursename":"","coursestatus":"","evastatus":"","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1474946581342.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1474946581342.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000440","memrole":"coach","nickname":"我媳妇儿","ordtype":"","signature":"我"},{"begintime":"","clubid":"","clubname":"","coachcert":"","corderlessonid":"","coursename":"","coursestatus":"","evastatus":"","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000448_1475733094159.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000448_1475733094159.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000448","memrole":"coach","nickname":"哈哈","ordtype":"","signature":"\u2026\u2026"},{"begintime":"","clubid":"","clubname":"","coachcert":"","corderlessonid":"","coursename":"","coursestatus":"","evastatus":"","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.comnull","isclubadmin":"","lessonid":"","memid":"M000510","memrole":"mem","nickname":"马小号","ordtype":"","signature":""}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * begintime :
     * clubid :
     * clubname :
     * coachcert :
     * corderlessonid :
     * coursename :
     * coursestatus :
     * evastatus :
     * imgpfile : http://efithealthresource.img-cn-beijing.aliyuncs.comnull
     * imgpfilename : http://efithealthresource.img-cn-beijing.aliyuncs.comnull
     * imgsfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000520_1476153333317.png@!BMEMBER_S
     * imgsfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000520_1476153333317.png@!BMEMBER_S
     * isclubadmin :
     * lessonid :
     * memid : M000520
     * memrole : coach
     * nickname : 沫沫
     * ordtype :
     * signature : 我写点什么吧
     */

    private List<CoachmemberlistBean> coachmemberlist;

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public List<CoachmemberlistBean> getCoachmemberlist() {
        return coachmemberlist;
    }

    public void setCoachmemberlist(List<CoachmemberlistBean> coachmemberlist) {
        this.coachmemberlist = coachmemberlist;
    }

    public static class CoachmemberlistBean {
        private String begintime;
        private String clubid;
        private String clubname;
        private String coachcert;
        private String corderlessonid;
        private String coursename;
        private String coursestatus;
        private String evastatus;
        private String imgpfile;
        private String imgpfilename;
        private String imgsfile;
        private String imgsfilename;
        private String isclubadmin;
        private String lessonid;
        private String memid;
        private String memrole;
        private String nickname;
        private String ordtype;
        private String signature;

        public String getBegintime() {
            return begintime;
        }

        public void setBegintime(String begintime) {
            this.begintime = begintime;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public String getClubname() {
            return clubname;
        }

        public void setClubname(String clubname) {
            this.clubname = clubname;
        }

        public String getCoachcert() {
            return coachcert;
        }

        public void setCoachcert(String coachcert) {
            this.coachcert = coachcert;
        }

        public String getCorderlessonid() {
            return corderlessonid;
        }

        public void setCorderlessonid(String corderlessonid) {
            this.corderlessonid = corderlessonid;
        }

        public String getCoursename() {
            return coursename;
        }

        public void setCoursename(String coursename) {
            this.coursename = coursename;
        }

        public String getCoursestatus() {
            return coursestatus;
        }

        public void setCoursestatus(String coursestatus) {
            this.coursestatus = coursestatus;
        }

        public String getEvastatus() {
            return evastatus;
        }

        public void setEvastatus(String evastatus) {
            this.evastatus = evastatus;
        }

        public String getImgpfile() {
            return imgpfile;
        }

        public void setImgpfile(String imgpfile) {
            this.imgpfile = imgpfile;
        }

        public String getImgpfilename() {
            return imgpfilename;
        }

        public void setImgpfilename(String imgpfilename) {
            this.imgpfilename = imgpfilename;
        }

        public String getImgsfile() {
            return imgsfile;
        }

        public void setImgsfile(String imgsfile) {
            this.imgsfile = imgsfile;
        }

        public String getImgsfilename() {
            return imgsfilename;
        }

        public void setImgsfilename(String imgsfilename) {
            this.imgsfilename = imgsfilename;
        }

        public String getIsclubadmin() {
            return isclubadmin;
        }

        public void setIsclubadmin(String isclubadmin) {
            this.isclubadmin = isclubadmin;
        }

        public String getLessonid() {
            return lessonid;
        }

        public void setLessonid(String lessonid) {
            this.lessonid = lessonid;
        }

        public String getMemid() {
            return memid;
        }

        public void setMemid(String memid) {
            this.memid = memid;
        }

        public String getMemrole() {
            return memrole;
        }

        public void setMemrole(String memrole) {
            this.memrole = memrole;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOrdtype() {
            return ordtype;
        }

        public void setOrdtype(String ordtype) {
            this.ordtype = ordtype;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
