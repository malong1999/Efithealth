package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/9/2.
 */
public class BeanmDynamicList {

    /**
     * msgFlag : 1
     * msgContent : 用户资料
     * bBMember : {"address":"","applydate":{"date":26,"day":1,"hours":13,"minutes":19,"month":8,"nanos":0,"seconds":27,"time":1474867167000,"timezoneOffset":-480,"year":116},"applydatestr":"","applydescr":"我我我我","area":"","birth":"","birthday":{"date":22,"day":2,"hours":0,"minutes":0,"month":10,"nanos":0,"seconds":0,"time":1479744000000,"timezoneOffset":-480,"year":116},"checkopinion":"","clubid":"","clubname":"","coachadept":"啊啊啊","coachcert":"2","coachcertname":"已认证","coachprice":300,"courseprice":0,"coursetimes":4,"createtime":{"date":26,"day":1,"hours":8,"minutes":36,"month":8,"nanos":0,"seconds":20,"time":1474850180000,"timezoneOffset":-480,"year":116},"createuser":"M000502","curMemrole":"coach","dayadd":"","distance":0,"distancestr":"","dynamicnum":0,"evascore":0,"evascoretotal":2.543,"evatimes":4,"followernum":0,"follownum":0,"gender":"1","gendername":"男","headpage":"0","headpagename":"未设置","identcode":"","identity":"","imagefile":[],"img":null,"imgfile":null,"imgfileFileName":"","imgpfile":"/image/bmember/M000502_1484122820051.png@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000502_1484122820051.png@!BMEMBER_P","imgsfile":"/image/bmember/M000502_1484122820051.png@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000502_1484122820051.png@!BMEMBER_S","isclubadmin":"0","isclubadminname":"否","ismanager":"","ispush":"1","ispushname":"开启","isstealth":"0","istopfile":[],"istrans":"1","latitude":41.751509,"lessonresent":0,"lessontotal":0,"linkurl":"","longitude":123.466084,"massagetimes":0,"memid":"M000502","memname":"","mempass":"e10adc3949ba59abbe56e057f20f883e","memrole":"coach","mobphone":"13897944728","modifytime":{"date":20,"day":5,"hours":9,"minutes":15,"month":0,"nanos":0,"seconds":23,"time":1484874923000,"timezoneOffset":-480,"year":117},"modifyuser":"M000502","nickname":"张帅","nowTime":0,"nowtimestr":"","openid":"","phonedeviceno":"0C53FBCE-DFC2-49B2-90C7-40878D757D46","pkeyListStr":"","posiupdatetime":{"date":20,"day":5,"hours":9,"minutes":26,"month":0,"nanos":0,"seconds":41,"time":1484875601000,"timezoneOffset":-480,"year":117},"recaddress":"浑南新区沈阳师范大学","recname":"全球","recphone":"13897944728","remark":"教练认证审核人:平台管理员","resinform":"","signature":"我我我","socialrel":"none","sorttype":"","status":"1","statusname":"有效","transtime":null,"updatetime":{"date":17,"day":2,"hours":15,"minutes":4,"month":0,"nanos":0,"seconds":7,"time":1484636647000,"timezoneOffset":-480,"year":117},"wxheadimgurl":"","wxnickname":"","wxopenid":"","wxregiarea":"","ycoincashnum":577440718,"ycoinnum":301}
     */

    private String msgFlag;
    private String msgContent;
    /**
     * address :
     * applydate : {"date":26,"day":1,"hours":13,"minutes":19,"month":8,"nanos":0,"seconds":27,"time":1474867167000,"timezoneOffset":-480,"year":116}
     * applydatestr :
     * applydescr : 我我我我
     * area :
     * birth :
     * birthday : {"date":22,"day":2,"hours":0,"minutes":0,"month":10,"nanos":0,"seconds":0,"time":1479744000000,"timezoneOffset":-480,"year":116}
     * checkopinion :
     * clubid :
     * clubname :
     * coachadept : 啊啊啊
     * coachcert : 2
     * coachcertname : 已认证
     * coachprice : 300
     * courseprice : 0
     * coursetimes : 4
     * createtime : {"date":26,"day":1,"hours":8,"minutes":36,"month":8,"nanos":0,"seconds":20,"time":1474850180000,"timezoneOffset":-480,"year":116}
     * createuser : M000502
     * curMemrole : coach
     * dayadd :
     * distance : 0
     * distancestr :
     * dynamicnum : 0
     * evascore : 0
     * evascoretotal : 2.543
     * evatimes : 4
     * followernum : 0
     * follownum : 0
     * gender : 1
     * gendername : 男
     * headpage : 0
     * headpagename : 未设置
     * identcode :
     * identity :
     * imagefile : []
     * img : null
     * imgfile : null
     * imgfileFileName :
     * imgpfile : /image/bmember/M000502_1484122820051.png@!BMEMBER_P
     * imgpfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000502_1484122820051.png@!BMEMBER_P
     * imgsfile : /image/bmember/M000502_1484122820051.png@!BMEMBER_S
     * imgsfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000502_1484122820051.png@!BMEMBER_S
     * isclubadmin : 0
     * isclubadminname : 否
     * ismanager :
     * ispush : 1
     * ispushname : 开启
     * isstealth : 0
     * istopfile : []
     * istrans : 1
     * latitude : 41.751509
     * lessonresent : 0
     * lessontotal : 0
     * linkurl :
     * longitude : 123.466084
     * massagetimes : 0
     * memid : M000502
     * memname :
     * mempass : e10adc3949ba59abbe56e057f20f883e
     * memrole : coach
     * mobphone : 13897944728
     * modifytime : {"date":20,"day":5,"hours":9,"minutes":15,"month":0,"nanos":0,"seconds":23,"time":1484874923000,"timezoneOffset":-480,"year":117}
     * modifyuser : M000502
     * nickname : 张帅
     * nowTime : 0
     * nowtimestr :
     * openid :
     * phonedeviceno : 0C53FBCE-DFC2-49B2-90C7-40878D757D46
     * pkeyListStr :
     * posiupdatetime : {"date":20,"day":5,"hours":9,"minutes":26,"month":0,"nanos":0,"seconds":41,"time":1484875601000,"timezoneOffset":-480,"year":117}
     * recaddress : 浑南新区沈阳师范大学
     * recname : 全球
     * recphone : 13897944728
     * remark : 教练认证审核人:平台管理员
     * resinform :
     * signature : 我我我
     * socialrel : none
     * sorttype :
     * status : 1
     * statusname : 有效
     * transtime : null
     * updatetime : {"date":17,"day":2,"hours":15,"minutes":4,"month":0,"nanos":0,"seconds":7,"time":1484636647000,"timezoneOffset":-480,"year":117}
     * wxheadimgurl :
     * wxnickname :
     * wxopenid :
     * wxregiarea :
     * ycoincashnum : 577440718
     * ycoinnum : 301
     */

    private BBMemberBean bBMember;

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

    public BBMemberBean getBBMember() {
        return bBMember;
    }

    public void setBBMember(BBMemberBean bBMember) {
        this.bBMember = bBMember;
    }

    public static class BBMemberBean {
        private String address;
        /**
         * date : 26
         * day : 1
         * hours : 13
         * minutes : 19
         * month : 8
         * nanos : 0
         * seconds : 27
         * time : 1474867167000
         * timezoneOffset : -480
         * year : 116
         */

        private ApplydateBean applydate;
        private String applydatestr;
        private String applydescr;
        private String area;
        private String birth;
        /**
         * date : 22
         * day : 2
         * hours : 0
         * minutes : 0
         * month : 10
         * nanos : 0
         * seconds : 0
         * time : 1479744000000
         * timezoneOffset : -480
         * year : 116
         */

        private BirthdayBean birthday;
        private String checkopinion;
        private String clubid;
        private String clubname;
        private String coachadept;
        private String coachcert;
        private String coachcertname;
        private int coachprice;
        private int courseprice;
        private int coursetimes;
        /**
         * date : 26
         * day : 1
         * hours : 8
         * minutes : 36
         * month : 8
         * nanos : 0
         * seconds : 20
         * time : 1474850180000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private String curMemrole;
        private String dayadd;
        private int distance;
        private String distancestr;
        private int dynamicnum;
        private int evascore;
        private double evascoretotal;
        private int evatimes;
        private int followernum;
        private int follownum;
        private String gender;
        private String gendername;
        private String headpage;
        private String headpagename;
        private String identcode;
        private String identity;
        private Object img;
        private Object imgfile;
        private String imgfileFileName;
        private String imgpfile;
        private String imgpfilename;
        private String imgsfile;
        private String imgsfilename;
        private String isclubadmin;
        private String isclubadminname;
        private String ismanager;
        private String ispush;
        private String ispushname;
        private String isstealth;
        private String istrans;
        private double latitude;
        private int lessonresent;
        private int lessontotal;
        private String linkurl;
        private double longitude;
        private int massagetimes;
        private String memid;
        private String memname;
        private String mempass;
        private String memrole;
        private String mobphone;
        /**
         * date : 20
         * day : 5
         * hours : 9
         * minutes : 15
         * month : 0
         * nanos : 0
         * seconds : 23
         * time : 1484874923000
         * timezoneOffset : -480
         * year : 117
         */

        private ModifytimeBean modifytime;
        private String modifyuser;
        private String nickname;
        private int nowTime;
        private String nowtimestr;
        private String openid;
        private String phonedeviceno;
        private String pkeyListStr;
        /**
         * date : 20
         * day : 5
         * hours : 9
         * minutes : 26
         * month : 0
         * nanos : 0
         * seconds : 41
         * time : 1484875601000
         * timezoneOffset : -480
         * year : 117
         */

        private PosiupdatetimeBean posiupdatetime;
        private String recaddress;
        private String recname;
        private String recphone;
        private String remark;
        private String resinform;
        private String signature;
        private String socialrel;
        private String sorttype;
        private String status;
        private String statusname;
        private Object transtime;
        /**
         * date : 17
         * day : 2
         * hours : 15
         * minutes : 4
         * month : 0
         * nanos : 0
         * seconds : 7
         * time : 1484636647000
         * timezoneOffset : -480
         * year : 117
         */

        private UpdatetimeBean updatetime;
        private String wxheadimgurl;
        private String wxnickname;
        private String wxopenid;
        private String wxregiarea;
        private int ycoincashnum;
        private int ycoinnum;
        private List<?> imagefile;
        private List<?> istopfile;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ApplydateBean getApplydate() {
            return applydate;
        }

        public void setApplydate(ApplydateBean applydate) {
            this.applydate = applydate;
        }

        public String getApplydatestr() {
            return applydatestr;
        }

        public void setApplydatestr(String applydatestr) {
            this.applydatestr = applydatestr;
        }

        public String getApplydescr() {
            return applydescr;
        }

        public void setApplydescr(String applydescr) {
            this.applydescr = applydescr;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public BirthdayBean getBirthday() {
            return birthday;
        }

        public void setBirthday(BirthdayBean birthday) {
            this.birthday = birthday;
        }

        public String getCheckopinion() {
            return checkopinion;
        }

        public void setCheckopinion(String checkopinion) {
            this.checkopinion = checkopinion;
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

        public String getCoachadept() {
            return coachadept;
        }

        public void setCoachadept(String coachadept) {
            this.coachadept = coachadept;
        }

        public String getCoachcert() {
            return coachcert;
        }

        public void setCoachcert(String coachcert) {
            this.coachcert = coachcert;
        }

        public String getCoachcertname() {
            return coachcertname;
        }

        public void setCoachcertname(String coachcertname) {
            this.coachcertname = coachcertname;
        }

        public int getCoachprice() {
            return coachprice;
        }

        public void setCoachprice(int coachprice) {
            this.coachprice = coachprice;
        }

        public int getCourseprice() {
            return courseprice;
        }

        public void setCourseprice(int courseprice) {
            this.courseprice = courseprice;
        }

        public int getCoursetimes() {
            return coursetimes;
        }

        public void setCoursetimes(int coursetimes) {
            this.coursetimes = coursetimes;
        }

        public CreatetimeBean getCreatetime() {
            return createtime;
        }

        public void setCreatetime(CreatetimeBean createtime) {
            this.createtime = createtime;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getCurMemrole() {
            return curMemrole;
        }

        public void setCurMemrole(String curMemrole) {
            this.curMemrole = curMemrole;
        }

        public String getDayadd() {
            return dayadd;
        }

        public void setDayadd(String dayadd) {
            this.dayadd = dayadd;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getDistancestr() {
            return distancestr;
        }

        public void setDistancestr(String distancestr) {
            this.distancestr = distancestr;
        }

        public int getDynamicnum() {
            return dynamicnum;
        }

        public void setDynamicnum(int dynamicnum) {
            this.dynamicnum = dynamicnum;
        }

        public int getEvascore() {
            return evascore;
        }

        public void setEvascore(int evascore) {
            this.evascore = evascore;
        }

        public double getEvascoretotal() {
            return evascoretotal;
        }

        public void setEvascoretotal(double evascoretotal) {
            this.evascoretotal = evascoretotal;
        }

        public int getEvatimes() {
            return evatimes;
        }

        public void setEvatimes(int evatimes) {
            this.evatimes = evatimes;
        }

        public int getFollowernum() {
            return followernum;
        }

        public void setFollowernum(int followernum) {
            this.followernum = followernum;
        }

        public int getFollownum() {
            return follownum;
        }

        public void setFollownum(int follownum) {
            this.follownum = follownum;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGendername() {
            return gendername;
        }

        public void setGendername(String gendername) {
            this.gendername = gendername;
        }

        public String getHeadpage() {
            return headpage;
        }

        public void setHeadpage(String headpage) {
            this.headpage = headpage;
        }

        public String getHeadpagename() {
            return headpagename;
        }

        public void setHeadpagename(String headpagename) {
            this.headpagename = headpagename;
        }

        public String getIdentcode() {
            return identcode;
        }

        public void setIdentcode(String identcode) {
            this.identcode = identcode;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public Object getImg() {
            return img;
        }

        public void setImg(Object img) {
            this.img = img;
        }

        public Object getImgfile() {
            return imgfile;
        }

        public void setImgfile(Object imgfile) {
            this.imgfile = imgfile;
        }

        public String getImgfileFileName() {
            return imgfileFileName;
        }

        public void setImgfileFileName(String imgfileFileName) {
            this.imgfileFileName = imgfileFileName;
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

        public String getIsclubadminname() {
            return isclubadminname;
        }

        public void setIsclubadminname(String isclubadminname) {
            this.isclubadminname = isclubadminname;
        }

        public String getIsmanager() {
            return ismanager;
        }

        public void setIsmanager(String ismanager) {
            this.ismanager = ismanager;
        }

        public String getIspush() {
            return ispush;
        }

        public void setIspush(String ispush) {
            this.ispush = ispush;
        }

        public String getIspushname() {
            return ispushname;
        }

        public void setIspushname(String ispushname) {
            this.ispushname = ispushname;
        }

        public String getIsstealth() {
            return isstealth;
        }

        public void setIsstealth(String isstealth) {
            this.isstealth = isstealth;
        }

        public String getIstrans() {
            return istrans;
        }

        public void setIstrans(String istrans) {
            this.istrans = istrans;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getLessonresent() {
            return lessonresent;
        }

        public void setLessonresent(int lessonresent) {
            this.lessonresent = lessonresent;
        }

        public int getLessontotal() {
            return lessontotal;
        }

        public void setLessontotal(int lessontotal) {
            this.lessontotal = lessontotal;
        }

        public String getLinkurl() {
            return linkurl;
        }

        public void setLinkurl(String linkurl) {
            this.linkurl = linkurl;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getMassagetimes() {
            return massagetimes;
        }

        public void setMassagetimes(int massagetimes) {
            this.massagetimes = massagetimes;
        }

        public String getMemid() {
            return memid;
        }

        public void setMemid(String memid) {
            this.memid = memid;
        }

        public String getMemname() {
            return memname;
        }

        public void setMemname(String memname) {
            this.memname = memname;
        }

        public String getMempass() {
            return mempass;
        }

        public void setMempass(String mempass) {
            this.mempass = mempass;
        }

        public String getMemrole() {
            return memrole;
        }

        public void setMemrole(String memrole) {
            this.memrole = memrole;
        }

        public String getMobphone() {
            return mobphone;
        }

        public void setMobphone(String mobphone) {
            this.mobphone = mobphone;
        }

        public ModifytimeBean getModifytime() {
            return modifytime;
        }

        public void setModifytime(ModifytimeBean modifytime) {
            this.modifytime = modifytime;
        }

        public String getModifyuser() {
            return modifyuser;
        }

        public void setModifyuser(String modifyuser) {
            this.modifyuser = modifyuser;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getNowTime() {
            return nowTime;
        }

        public void setNowTime(int nowTime) {
            this.nowTime = nowTime;
        }

        public String getNowtimestr() {
            return nowtimestr;
        }

        public void setNowtimestr(String nowtimestr) {
            this.nowtimestr = nowtimestr;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getPhonedeviceno() {
            return phonedeviceno;
        }

        public void setPhonedeviceno(String phonedeviceno) {
            this.phonedeviceno = phonedeviceno;
        }

        public String getPkeyListStr() {
            return pkeyListStr;
        }

        public void setPkeyListStr(String pkeyListStr) {
            this.pkeyListStr = pkeyListStr;
        }

        public PosiupdatetimeBean getPosiupdatetime() {
            return posiupdatetime;
        }

        public void setPosiupdatetime(PosiupdatetimeBean posiupdatetime) {
            this.posiupdatetime = posiupdatetime;
        }

        public String getRecaddress() {
            return recaddress;
        }

        public void setRecaddress(String recaddress) {
            this.recaddress = recaddress;
        }

        public String getRecname() {
            return recname;
        }

        public void setRecname(String recname) {
            this.recname = recname;
        }

        public String getRecphone() {
            return recphone;
        }

        public void setRecphone(String recphone) {
            this.recphone = recphone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getResinform() {
            return resinform;
        }

        public void setResinform(String resinform) {
            this.resinform = resinform;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSocialrel() {
            return socialrel;
        }

        public void setSocialrel(String socialrel) {
            this.socialrel = socialrel;
        }

        public String getSorttype() {
            return sorttype;
        }

        public void setSorttype(String sorttype) {
            this.sorttype = sorttype;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusname() {
            return statusname;
        }

        public void setStatusname(String statusname) {
            this.statusname = statusname;
        }

        public Object getTranstime() {
            return transtime;
        }

        public void setTranstime(Object transtime) {
            this.transtime = transtime;
        }

        public UpdatetimeBean getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(UpdatetimeBean updatetime) {
            this.updatetime = updatetime;
        }

        public String getWxheadimgurl() {
            return wxheadimgurl;
        }

        public void setWxheadimgurl(String wxheadimgurl) {
            this.wxheadimgurl = wxheadimgurl;
        }

        public String getWxnickname() {
            return wxnickname;
        }

        public void setWxnickname(String wxnickname) {
            this.wxnickname = wxnickname;
        }

        public String getWxopenid() {
            return wxopenid;
        }

        public void setWxopenid(String wxopenid) {
            this.wxopenid = wxopenid;
        }

        public String getWxregiarea() {
            return wxregiarea;
        }

        public void setWxregiarea(String wxregiarea) {
            this.wxregiarea = wxregiarea;
        }

        public int getYcoincashnum() {
            return ycoincashnum;
        }

        public void setYcoincashnum(int ycoincashnum) {
            this.ycoincashnum = ycoincashnum;
        }

        public int getYcoinnum() {
            return ycoinnum;
        }

        public void setYcoinnum(int ycoinnum) {
            this.ycoinnum = ycoinnum;
        }

        public List<?> getImagefile() {
            return imagefile;
        }

        public void setImagefile(List<?> imagefile) {
            this.imagefile = imagefile;
        }

        public List<?> getIstopfile() {
            return istopfile;
        }

        public void setIstopfile(List<?> istopfile) {
            this.istopfile = istopfile;
        }

        public static class ApplydateBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }

        public static class BirthdayBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }

        public static class CreatetimeBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }

        public static class ModifytimeBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }

        public static class PosiupdatetimeBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }

        public static class UpdatetimeBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
