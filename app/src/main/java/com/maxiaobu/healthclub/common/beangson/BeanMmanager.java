package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/9/21.
 */
public class BeanMmanager {

    /**
     * msgFlag : 1
     * msgContent : 我的档期
     * managerlist : [{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"10|","weekday":"1","weekdayname":"星期一"},{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"10|11|12|13|14|15|16|17|18|19|20|","weekday":"2","weekdayname":"星期二"},{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"12|","weekday":"3","weekdayname":"星期三"},{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"","weekday":"4","weekdayname":"星期四"},{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"10|14|19|20|","weekday":"5","weekdayname":"星期五"},{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"","weekday":"6","weekdayname":"星期六"},{"coachid":"M000440","createtime":{"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116},"createuser":"M000440","daystatus":"1","daystatusname":"允许预约","memname":"","mobphone":"13897900910","modifytime":null,"modifyuser":"","nickname":"赵文卓","pkeyListStr":"","remark":"","schtimeslice":"10|11|13|18|19|","weekday":"7","weekdayname":""}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * coachid : M000440
     * createtime : {"date":19,"day":1,"hours":12,"minutes":5,"month":8,"nanos":0,"seconds":19,"time":1474257919000,"timezoneOffset":-480,"year":116}
     * createuser : M000440
     * daystatus : 1
     * daystatusname : 允许预约
     * memname :
     * mobphone : 13897900910
     * modifytime : null
     * modifyuser :
     * nickname : 赵文卓
     * pkeyListStr :
     * remark :
     * schtimeslice : 10|
     * weekday : 1
     * weekdayname : 星期一
     */

    private List<ManagerlistBean> managerlist;

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

    public List<ManagerlistBean> getManagerlist() {
        return managerlist;
    }

    public void setManagerlist(List<ManagerlistBean> managerlist) {
        this.managerlist = managerlist;
    }

    public static class ManagerlistBean {
        private String coachid;
        /**
         * date : 19
         * day : 1
         * hours : 12
         * minutes : 5
         * month : 8
         * nanos : 0
         * seconds : 19
         * time : 1474257919000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private String daystatus;
        private String daystatusname;
        private String memname;
        private String mobphone;
        private Object modifytime;
        private String modifyuser;
        private String nickname;
        private String pkeyListStr;
        private String remark;
        private String schtimeslice;
        private String weekday;
        private String weekdayname;

        public String getCoachid() {
            return coachid;
        }

        public void setCoachid(String coachid) {
            this.coachid = coachid;
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

        public String getDaystatus() {
            return daystatus;
        }

        public void setDaystatus(String daystatus) {
            this.daystatus = daystatus;
        }

        public String getDaystatusname() {
            return daystatusname;
        }

        public void setDaystatusname(String daystatusname) {
            this.daystatusname = daystatusname;
        }

        public String getMemname() {
            return memname;
        }

        public void setMemname(String memname) {
            this.memname = memname;
        }

        public String getMobphone() {
            return mobphone;
        }

        public void setMobphone(String mobphone) {
            this.mobphone = mobphone;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSchtimeslice() {
            return schtimeslice;
        }

        public void setSchtimeslice(String schtimeslice) {
            this.schtimeslice = schtimeslice;
        }

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getWeekdayname() {
            return weekdayname;
        }

        public void setWeekdayname(String weekdayname) {
            this.weekdayname = weekdayname;
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
