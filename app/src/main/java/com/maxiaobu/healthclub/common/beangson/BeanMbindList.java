package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/9/12.
 */
public class BeanMbindList {

    /**
     * msgFlag : 1
     * msgContent : 教练俱乐部绑定列表!
     * bindList : [{"address":"沈阳市东陵区新华国际C座","bapplydescr":"我","bapplyid":"P000315","binddate":{"date":21,"day":4,"hours":18,"minutes":28,"month":6,"nanos":0,"seconds":33,"time":1469096913000,"timezoneOffset":-480,"year":116},"bindid":"B000023","bindstatus":"1","bindstatusname":"","brelievedate":null,"brelievedescr":"","breliever":"","brelievername":"","brelieveuser":"","checkuser":"","clubid":"C000172","clubmemid":"M000438","clubmemimgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000438_1469430008000_p.jpg@!BMEMBER_S","clubmemname":"羿健康俱乐部","clubname":"羿健康健身俱乐部","clubprice":100,"coachid":"M000440","coachprice":100,"createtime":{"date":21,"day":4,"hours":18,"minutes":28,"month":6,"nanos":0,"seconds":33,"time":1469096913000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","distance":9033.81,"imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1471935220977_s.png@!BMEMBER_S","istrans":"","latitude":41.752288,"longitude":123.465187,"modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","resinformclub":"提前两小时订餐","resinformcoach":"","transtime":null,"updatetime":null},{"address":"沈阳市东陵区奥体中心","bapplydescr":"","bapplyid":"P000325","binddate":{"date":1,"day":4,"hours":11,"minutes":15,"month":8,"nanos":0,"seconds":12,"time":1472699712000,"timezoneOffset":-480,"year":116},"bindid":"B000029","bindstatus":"1","bindstatusname":"","brelievedate":null,"brelievedescr":"","breliever":"","brelievername":"","brelieveuser":"","checkuser":"","clubid":"C000174","clubmemid":"M000443","clubmemimgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000443_1469429386655_p.png@!BMEMBER_S","clubmemname":"健身俱乐部CLUB_A管理员","clubname":"健身俱乐部CLUB_A","clubprice":0,"coachid":"M000440","coachprice":0,"createtime":{"date":1,"day":4,"hours":11,"minutes":15,"month":8,"nanos":0,"seconds":12,"time":1472699712000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","distance":9034.18,"imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1471935220977_s.png@!BMEMBER_S","istrans":"","latitude":41.749034,"longitude":123.46017,"modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","resinformclub":"俱乐部课程预定须知缺省值","resinformcoach":"","transtime":null,"updatetime":null}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * address : 沈阳市东陵区新华国际C座
     * bapplydescr : 我
     * bapplyid : P000315
     * binddate : {"date":21,"day":4,"hours":18,"minutes":28,"month":6,"nanos":0,"seconds":33,"time":1469096913000,"timezoneOffset":-480,"year":116}
     * bindid : B000023
     * bindstatus : 1
     * bindstatusname :
     * brelievedate : null
     * brelievedescr :
     * breliever :
     * brelievername :
     * brelieveuser :
     * checkuser :
     * clubid : C000172
     * clubmemid : M000438
     * clubmemimgsfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000438_1469430008000_p.jpg@!BMEMBER_S
     * clubmemname : 羿健康俱乐部
     * clubname : 羿健康健身俱乐部
     * clubprice : 100
     * coachid : M000440
     * coachprice : 100
     * createtime : {"date":21,"day":4,"hours":18,"minutes":28,"month":6,"nanos":0,"seconds":33,"time":1469096913000,"timezoneOffset":-480,"year":116}
     * createuser : platAdmin
     * distance : 9033.81
     * imgsfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000440_1471935220977_s.png@!BMEMBER_S
     * istrans :
     * latitude : 41.752288
     * longitude : 123.465187
     * modifytime : null
     * modifyuser :
     * nickname : 赵文卓
     * pkeyListStr :
     * resinformclub : 提前两小时订餐
     * resinformcoach :
     * transtime : null
     * updatetime : null
     */

    private List<BindListBean> bindList;

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

    public List<BindListBean> getBindList() {
        return bindList;
    }

    public void setBindList(List<BindListBean> bindList) {
        this.bindList = bindList;
    }

    public static class BindListBean {
        private String address;
        private String bapplydescr;
        private String bapplyid;
        /**
         * date : 21
         * day : 4
         * hours : 18
         * minutes : 28
         * month : 6
         * nanos : 0
         * seconds : 33
         * time : 1469096913000
         * timezoneOffset : -480
         * year : 116
         */

        private BinddateBean binddate;
        private String bindid;
        private String bindstatus;
        private String bindstatusname;
        private Object brelievedate;
        private String brelievedescr;
        private String breliever;
        private String brelievername;
        private String brelieveuser;
        private String checkuser;
        private String clubid;
        private String clubmemid;
        private String clubmemimgsfile;
        private String clubmemname;
        private String clubname;
        private String clubprice;
        private String coachid;
        private String coachprice;
        /**
         * date : 21
         * day : 4
         * hours : 18
         * minutes : 28
         * month : 6
         * nanos : 0
         * seconds : 33
         * time : 1469096913000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private double distance;
        private String imgsfile;
        private String istrans;
        private double latitude;
        private double longitude;
        private Object modifytime;
        private String modifyuser;
        private String nickname;
        private String pkeyListStr;
        private String resinformclub;
        private String resinformcoach;
        private Object transtime;
        private Object updatetime;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBapplydescr() {
            return bapplydescr;
        }

        public void setBapplydescr(String bapplydescr) {
            this.bapplydescr = bapplydescr;
        }

        public String getBapplyid() {
            return bapplyid;
        }

        public void setBapplyid(String bapplyid) {
            this.bapplyid = bapplyid;
        }

        public BinddateBean getBinddate() {
            return binddate;
        }

        public void setBinddate(BinddateBean binddate) {
            this.binddate = binddate;
        }

        public String getBindid() {
            return bindid;
        }

        public void setBindid(String bindid) {
            this.bindid = bindid;
        }

        public String getBindstatus() {
            return bindstatus;
        }

        public void setBindstatus(String bindstatus) {
            this.bindstatus = bindstatus;
        }

        public String getBindstatusname() {
            return bindstatusname;
        }

        public void setBindstatusname(String bindstatusname) {
            this.bindstatusname = bindstatusname;
        }

        public Object getBrelievedate() {
            return brelievedate;
        }

        public void setBrelievedate(Object brelievedate) {
            this.brelievedate = brelievedate;
        }

        public String getBrelievedescr() {
            return brelievedescr;
        }

        public void setBrelievedescr(String brelievedescr) {
            this.brelievedescr = brelievedescr;
        }

        public String getBreliever() {
            return breliever;
        }

        public void setBreliever(String breliever) {
            this.breliever = breliever;
        }

        public String getBrelievername() {
            return brelievername;
        }

        public void setBrelievername(String brelievername) {
            this.brelievername = brelievername;
        }

        public String getBrelieveuser() {
            return brelieveuser;
        }

        public void setBrelieveuser(String brelieveuser) {
            this.brelieveuser = brelieveuser;
        }

        public String getCheckuser() {
            return checkuser;
        }

        public void setCheckuser(String checkuser) {
            this.checkuser = checkuser;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public String getClubmemid() {
            return clubmemid;
        }

        public void setClubmemid(String clubmemid) {
            this.clubmemid = clubmemid;
        }

        public String getClubmemimgsfile() {
            return clubmemimgsfile;
        }

        public void setClubmemimgsfile(String clubmemimgsfile) {
            this.clubmemimgsfile = clubmemimgsfile;
        }

        public String getClubmemname() {
            return clubmemname;
        }

        public void setClubmemname(String clubmemname) {
            this.clubmemname = clubmemname;
        }

        public String getClubname() {
            return clubname;
        }

        public void setClubname(String clubname) {
            this.clubname = clubname;
        }

        public String getClubprice() {
            return clubprice;
        }

        public void setClubprice(String clubprice) {
            this.clubprice = clubprice;
        }

        public String getCoachid() {
            return coachid;
        }

        public void setCoachid(String coachid) {
            this.coachid = coachid;
        }

        public String getCoachprice() {
            return coachprice;
        }

        public void setCoachprice(String coachprice) {
            this.coachprice = coachprice;
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

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getImgsfile() {
            return imgsfile;
        }

        public void setImgsfile(String imgsfile) {
            this.imgsfile = imgsfile;
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

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public Object getModifytime() {
            return modifytime;
        }

        public void setModifytime(Object modifytime) {
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

        public String getPkeyListStr() {
            return pkeyListStr;
        }

        public void setPkeyListStr(String pkeyListStr) {
            this.pkeyListStr = pkeyListStr;
        }

        public String getResinformclub() {
            return resinformclub;
        }

        public void setResinformclub(String resinformclub) {
            this.resinformclub = resinformclub;
        }

        public String getResinformcoach() {
            return resinformcoach;
        }

        public void setResinformcoach(String resinformcoach) {
            this.resinformcoach = resinformcoach;
        }

        public Object getTranstime() {
            return transtime;
        }

        public void setTranstime(Object transtime) {
            this.transtime = transtime;
        }

        public Object getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(Object updatetime) {
            this.updatetime = updatetime;
        }

        public static class BinddateBean {
            private String date;
            private String day;
            private String hours;
            private String minutes;
            private String month;
            private String nanos;
            private String seconds;
            private long time;
            private String timezoneOffset;
            private String year;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public String getMinutes() {
                return minutes;
            }

            public void setMinutes(String minutes) {
                this.minutes = minutes;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getNanos() {
                return nanos;
            }

            public void setNanos(String nanos) {
                this.nanos = nanos;
            }

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(String timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }
        }

        public static class CreatetimeBean {
            private String date;
            private String day;
            private String hours;
            private String minutes;
            private String month;
            private String nanos;
            private String seconds;
            private long time;
            private String timezoneOffset;
            private String year;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public String getMinutes() {
                return minutes;
            }

            public void setMinutes(String minutes) {
                this.minutes = minutes;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getNanos() {
                return nanos;
            }

            public void setNanos(String nanos) {
                this.nanos = nanos;
            }

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(String timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }
        }
    }
}
