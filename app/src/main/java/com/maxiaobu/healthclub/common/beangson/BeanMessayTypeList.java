package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/10/5.
 */

public class BeanMessayTypeList {

    /**
     * msgFlag : 1
     * msgContent : 获取文章分类集合
     * tagList : [{"createtime":{"date":8,"day":5,"hours":14,"minutes":14,"month":3,"nanos":0,"seconds":57,"time":1460096097000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000001","etname":"减脂","modifytime":{"date":31,"day":2,"hours":14,"minutes":22,"month":4,"nanos":0,"seconds":20,"time":1464675740000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":23,"day":6,"hours":17,"minutes":19,"month":3,"nanos":0,"seconds":52,"time":1461403192000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000003","etname":"增肌塑形","modifytime":{"date":31,"day":2,"hours":14,"minutes":22,"month":4,"nanos":0,"seconds":6,"time":1464675726000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":23,"day":6,"hours":17,"minutes":37,"month":3,"nanos":0,"seconds":11,"time":1461404231000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000004","etname":"产后恢复","modifytime":{"date":31,"day":2,"hours":14,"minutes":19,"month":4,"nanos":0,"seconds":37,"time":1464675577000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":23,"day":6,"hours":17,"minutes":37,"month":3,"nanos":0,"seconds":58,"time":1461404278000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000005","etname":"康复保健","modifytime":{"date":31,"day":2,"hours":14,"minutes":19,"month":4,"nanos":0,"seconds":21,"time":1464675561000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":23,"day":6,"hours":17,"minutes":48,"month":3,"nanos":0,"seconds":33,"time":1461404913000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000006","etname":"健康饮食","modifytime":{"date":31,"day":2,"hours":14,"minutes":18,"month":4,"nanos":0,"seconds":53,"time":1464675533000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"},{"createtime":{"date":3,"day":2,"hours":18,"minutes":49,"month":4,"nanos":0,"seconds":6,"time":1462272546000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","etid":"T000007","etname":"运动装备","modifytime":{"date":31,"day":2,"hours":14,"minutes":18,"month":4,"nanos":0,"seconds":34,"time":1464675514000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","remark":"","status":"1","statusname":"有效"}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * createtime : {"date":8,"day":5,"hours":14,"minutes":14,"month":3,"nanos":0,"seconds":57,"time":1460096097000,"timezoneOffset":-480,"year":116}
     * createuser : platAdmin
     * etid : T000001
     * etname : 减脂
     * modifytime : {"date":31,"day":2,"hours":14,"minutes":22,"month":4,"nanos":0,"seconds":20,"time":1464675740000,"timezoneOffset":-480,"year":116}
     * modifyuser : platAdmin
     * remark : 
     * status : 1
     * statusname : 有效
     */

    private List<TagListBean> tagList;

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

    public List<TagListBean> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagListBean> tagList) {
        this.tagList = tagList;
    }

    public static class TagListBean {
        /**
         * date : 8
         * day : 5
         * hours : 14
         * minutes : 14
         * month : 3
         * nanos : 0
         * seconds : 57
         * time : 1460096097000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private String etid;
        private String etname;
        /**
         * date : 31
         * day : 2
         * hours : 14
         * minutes : 22
         * month : 4
         * nanos : 0
         * seconds : 20
         * time : 1464675740000
         * timezoneOffset : -480
         * year : 116
         */

        private ModifytimeBean modifytime;
        private String modifyuser;
        private String remark;
        private String status;
        private String statusname;

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

        public String getEtid() {
            return etid;
        }

        public void setEtid(String etid) {
            this.etid = etid;
        }

        public String getEtname() {
            return etname;
        }

        public void setEtname(String etname) {
            this.etname = etname;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public static class ModifytimeBean {
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
