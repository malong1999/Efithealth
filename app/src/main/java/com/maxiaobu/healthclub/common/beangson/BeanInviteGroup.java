package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 莫小婷 on 2016/12/9.
 */

public class BeanInviteGroup {


    /**
     * msgFlag : 1
     * msgContent :
     * groupList : [{"address":"沈阳市沈河区沈河区哈尔滨路168-1号C1-14-12","bulletin":"跑步群公告","checkstatus":"1","checkstatusname":"审核通过","clubid":"C000292","clubname":"","clubperson":"","clubphone":"","createtime":{"date":9,"day":5,"hours":9,"minutes":25,"month":11,"nanos":0,"seconds":20,"time":1481246720000,"timezoneOffset":-480,"year":116},"createuser":"C000292","currpeoples":0,"distance":7603.049552134786,"distancestr":"7.6km","gname":"EMS跑步群","groupid":"GC000022","gsort":", 2","gsortname":"","gtype":"1","gtypename":"主题群","imgpfile":"/image/bmember/GC000022_1481247586825.jpg@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/GC000022_1481247586825.jpg@!BMEMBER_P","imgsfile":"/image/bmember/GC000022_1481247586825.jpg@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/GC000022_1481247586825.jpg@!BMEMBER_S","imid":"273512487628833296","isnotice":"","latitude":41.817463,"longitude":123.44303,"managerid":"M000038","manname":"","mannick":"","manphone":"","memList":[],"memberuplimit":100,"memcheckstatus":"","modifytime":{"date":9,"day":5,"hours":9,"minutes":39,"month":11,"nanos":0,"seconds":56,"time":1481247596000,"timezoneOffset":-480,"year":116},"modifyuser":"C000292","pkeyListStr":"","remark":"","summary":"跑步群简介"},{"address":"沈阳市沈河区沈河区哈尔滨路168-1号C1-14-12","bulletin":"测试群公告哦","checkstatus":"1","checkstatusname":"审核通过","clubid":"C000292","clubname":"","clubperson":"","clubphone":"","createtime":{"date":2,"day":5,"hours":20,"minutes":48,"month":11,"nanos":0,"seconds":6,"time":1480682886000,"timezoneOffset":-480,"year":116},"createuser":"C000292","currpeoples":9,"distance":7603.049552134786,"distancestr":"7.6km","gname":"EMS健身Family","groupid":"GC000021","gsort":", 0","gsortname":"","gtype":"0","gtypename":"默认群","imgpfile":"/image/bfoodmer/GC000021_1480682877204.jpg@!BMEMBER_P","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/GC000021_1480682877204.jpg@!BMEMBER_P","imgsfile":"/image/bfoodmer/GC000021_1480682877204.jpg@!BMEMBER_S","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/GC000021_1480682877204.jpg@!BMEMBER_S","imid":"271375299428484676","isnotice":"","latitude":41.817463,"longitude":123.44303,"managerid":"M000034","manname":"","mannick":"","manphone":"","memList":[],"memberuplimit":100,"memcheckstatus":"","modifytime":{"date":3,"day":6,"hours":15,"minutes":51,"month":11,"nanos":0,"seconds":26,"time":1480751486000,"timezoneOffset":-480,"year":116},"modifyuser":"C000292","pkeyListStr":"","remark":"","summary":"测试群简介哦"}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * address : 沈阳市沈河区沈河区哈尔滨路168-1号C1-14-12
     * bulletin : 跑步群公告
     * checkstatus : 1
     * checkstatusname : 审核通过
     * clubid : C000292
     * clubname :
     * clubperson :
     * clubphone :
     * createtime : {"date":9,"day":5,"hours":9,"minutes":25,"month":11,"nanos":0,"seconds":20,"time":1481246720000,"timezoneOffset":-480,"year":116}
     * createuser : C000292
     * currpeoples : 0
     * distance : 7603.049552134786
     * distancestr : 7.6km
     * gname : EMS跑步群
     * groupid : GC000022
     * gsort : , 2
     * gsortname :
     * gtype : 1
     * gtypename : 主题群
     * imgpfile : /image/bmember/GC000022_1481247586825.jpg@!BMEMBER_P
     * imgpfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/GC000022_1481247586825.jpg@!BMEMBER_P
     * imgsfile : /image/bmember/GC000022_1481247586825.jpg@!BMEMBER_S
     * imgsfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/GC000022_1481247586825.jpg@!BMEMBER_S
     * imid : 273512487628833296
     * isnotice :
     * latitude : 41.817463
     * longitude : 123.44303
     * managerid : M000038
     * manname :
     * mannick :
     * manphone :
     * memList : []
     * memberuplimit : 100
     * memcheckstatus :
     * modifytime : {"date":9,"day":5,"hours":9,"minutes":39,"month":11,"nanos":0,"seconds":56,"time":1481247596000,"timezoneOffset":-480,"year":116}
     * modifyuser : C000292
     * pkeyListStr :
     * remark :
     * summary : 跑步群简介
     */

    private List<GroupListBean> groupList;

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

    public List<GroupListBean> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupListBean> groupList) {
        this.groupList = groupList;
    }

    public static class GroupListBean {
        private String address;
        private String bulletin;
        private String checkstatus;
        private String checkstatusname;
        private String clubid;
        private String clubname;
        private String clubperson;
        private String clubphone;
        /**
         * date : 9
         * day : 5
         * hours : 9
         * minutes : 25
         * month : 11
         * nanos : 0
         * seconds : 20
         * time : 1481246720000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private int currpeoples;
        private double distance;
        private String distancestr;
        private String gname;
        private String groupid;
        private String gsort;
        private String gsortname;
        private String gtype;
        private String gtypename;
        private String imgpfile;
        private String imgpfilename;
        private String imgsfile;
        private String imgsfilename;
        private String imid;
        private String isnotice;
        private double latitude;
        private double longitude;
        private String managerid;
        private String manname;
        private String mannick;
        private String manphone;
        private int memberuplimit;
        private String memcheckstatus;
        /**
         * date : 9
         * day : 5
         * hours : 9
         * minutes : 39
         * month : 11
         * nanos : 0
         * seconds : 56
         * time : 1481247596000
         * timezoneOffset : -480
         * year : 116
         */

        private ModifytimeBean modifytime;
        private String modifyuser;
        private String pkeyListStr;
        private String remark;
        private String summary;
        private List<?> memList;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBulletin() {
            return bulletin;
        }

        public void setBulletin(String bulletin) {
            this.bulletin = bulletin;
        }

        public String getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(String checkstatus) {
            this.checkstatus = checkstatus;
        }

        public String getCheckstatusname() {
            return checkstatusname;
        }

        public void setCheckstatusname(String checkstatusname) {
            this.checkstatusname = checkstatusname;
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

        public String getClubperson() {
            return clubperson;
        }

        public void setClubperson(String clubperson) {
            this.clubperson = clubperson;
        }

        public String getClubphone() {
            return clubphone;
        }

        public void setClubphone(String clubphone) {
            this.clubphone = clubphone;
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

        public int getCurrpeoples() {
            return currpeoples;
        }

        public void setCurrpeoples(int currpeoples) {
            this.currpeoples = currpeoples;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getDistancestr() {
            return distancestr;
        }

        public void setDistancestr(String distancestr) {
            this.distancestr = distancestr;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGsort() {
            return gsort;
        }

        public void setGsort(String gsort) {
            this.gsort = gsort;
        }

        public String getGsortname() {
            return gsortname;
        }

        public void setGsortname(String gsortname) {
            this.gsortname = gsortname;
        }

        public String getGtype() {
            return gtype;
        }

        public void setGtype(String gtype) {
            this.gtype = gtype;
        }

        public String getGtypename() {
            return gtypename;
        }

        public void setGtypename(String gtypename) {
            this.gtypename = gtypename;
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

        public String getImid() {
            return imid;
        }

        public void setImid(String imid) {
            this.imid = imid;
        }

        public String getIsnotice() {
            return isnotice;
        }

        public void setIsnotice(String isnotice) {
            this.isnotice = isnotice;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getManagerid() {
            return managerid;
        }

        public void setManagerid(String managerid) {
            this.managerid = managerid;
        }

        public String getManname() {
            return manname;
        }

        public void setManname(String manname) {
            this.manname = manname;
        }

        public String getMannick() {
            return mannick;
        }

        public void setMannick(String mannick) {
            this.mannick = mannick;
        }

        public String getManphone() {
            return manphone;
        }

        public void setManphone(String manphone) {
            this.manphone = manphone;
        }

        public int getMemberuplimit() {
            return memberuplimit;
        }

        public void setMemberuplimit(int memberuplimit) {
            this.memberuplimit = memberuplimit;
        }

        public String getMemcheckstatus() {
            return memcheckstatus;
        }

        public void setMemcheckstatus(String memcheckstatus) {
            this.memcheckstatus = memcheckstatus;
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

        public String getPkeyListStr() {
            return pkeyListStr;
        }

        public void setPkeyListStr(String pkeyListStr) {
            this.pkeyListStr = pkeyListStr;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<?> getMemList() {
            return memList;
        }

        public void setMemList(List<?> memList) {
            this.memList = memList;
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
    }
}
