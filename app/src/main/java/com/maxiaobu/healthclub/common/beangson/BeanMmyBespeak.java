package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/9/10.
 */
public class BeanMmyBespeak {

    /**
     * bespeaklist : [{"begintime":"2016-10-18 19:00","clubid":"","clubname":"健身俱乐部CLUB_A","coachcert":"","corderlessonid":"L000477","coursename":"asdf","coursestatus":"0","evascore":4,"evastatus":"0","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000440","memrole":"","nickname":"我原哥儿","ordtype":"","signature":"Qwqw"},{"begintime":"2016-10-18 13:00","clubid":"","clubname":"健身俱乐部CLUB_A","coachcert":"","corderlessonid":"L000539","coursename":"212","coursestatus":"0","evascore":0,"evastatus":"0","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/default.png@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/default.png@!BMEMBER_P","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/default.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/default.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000517","memrole":"","nickname":"1212312","ordtype":"","signature":""},{"begintime":"2016-10-06 21:00","clubid":"","clubname":"健身俱乐部CLUB_A","coachcert":"","corderlessonid":"L000421","coursename":"测试团操","coursestatus":"0","evascore":4,"evastatus":"0","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000440","memrole":"","nickname":"我原哥儿","ordtype":"","signature":"Qwqw"},{"begintime":"2016-10-06 21:00","clubid":"","clubname":"健身俱乐部CLUB_A","coachcert":"","corderlessonid":"L000422","coursename":"测试团操","coursestatus":"0","evascore":4,"evastatus":"0","imgpfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P","imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S","isclubadmin":"","lessonid":"","memid":"M000440","memrole":"","nickname":"我原哥儿","ordtype":"","signature":"Qwqw"}]
     * msgContent : 我的预约
     * msgFlag : 1
     */

    private String msgContent;
    private String msgFlag;
    /**
     * begintime : 2016-10-18 19:00
     * clubid :
     * clubname : 健身俱乐部CLUB_A
     * coachcert :
     * corderlessonid : L000477
     * coursename : asdf
     * coursestatus : 0
     * evascore : 4
     * evastatus : 0
     * imgpfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P
     * imgpfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_P
     * imgsfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S
     * imgsfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1476506575073.png@!BMEMBER_S
     * isclubadmin :
     * lessonid :
     * memid : M000440
     * memrole :
     * nickname : 我原哥儿
     * ordtype :
     * signature : Qwqw
     */

    private List<BespeaklistBean> bespeaklist;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public List<BespeaklistBean> getBespeaklist() {
        return bespeaklist;
    }

    public void setBespeaklist(List<BespeaklistBean> bespeaklist) {
        this.bespeaklist = bespeaklist;
    }

    public static class BespeaklistBean {
        private String begintime;
        private String clubid;
        private String clubname;
        private String coachcert;
        private String corderlessonid;
        private String coursename;
        private String coursestatus;
        private String evascore;
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
        private String clubmemid;

        public String getClubmemid() {
            return clubmemid;
        }

        public void setClubmemid(String clubmemid) {
            this.clubmemid = clubmemid;
        }

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

        public String getEvascore() {
            return evascore;
        }

        public void setEvascore(String evascore) {
            this.evascore = evascore;
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
