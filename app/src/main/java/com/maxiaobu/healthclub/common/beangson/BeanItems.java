package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/9/22.
 */
public class BeanItems {


    /**
     * createtime : {"date":10,"day":6,"hours":17,"minutes":10,"month":8,"nanos":0,"seconds":37,"time":1473498637000,"timezoneOffset":-480,"year":116}
     * createuser : platAdmin
     * descr : 肩A
     * imgsfile : /image/btrainingitem/TI000028_1473498637160.png@!FOODMER_S
     * imgsfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/btrainingitem/TI000028_1473498637160.png@!FOODMER_S
     * itemid : TI000028
     * itemname : 肩A
     * items : []
     * modifyuser :
     * pitemid : TI000023
     * pitemname : 肩
     * pkeyListStr :
     * remark :
     * status : 1
     * trainingsort : 0
     * trainingsortname : 无氧
     * unit :
     */

    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * date : 10
         * day : 6
         * hours : 17
         * minutes : 10
         * month : 8
         * nanos : 0
         * seconds : 37
         * time : 1473498637000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private String descr;
        private String imgsfile;
        private String imgsfilename;
        private String itemid;
        private String itemname;
        private String modifyuser;
        private String pitemid;
        private String pitemname;
        private String pkeyListStr;
        private String remark;
        private String status;
        private String trainingsort;
        private String trainingsortname;
        private String unit;
        private List<?> items;

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

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
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

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getModifyuser() {
            return modifyuser;
        }

        public void setModifyuser(String modifyuser) {
            this.modifyuser = modifyuser;
        }

        public String getPitemid() {
            return pitemid;
        }

        public void setPitemid(String pitemid) {
            this.pitemid = pitemid;
        }

        public String getPitemname() {
            return pitemname;
        }

        public void setPitemname(String pitemname) {
            this.pitemname = pitemname;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTrainingsort() {
            return trainingsort;
        }

        public void setTrainingsort(String trainingsort) {
            this.trainingsort = trainingsort;
        }

        public String getTrainingsortname() {
            return trainingsortname;
        }

        public void setTrainingsortname(String trainingsortname) {
            this.trainingsortname = trainingsortname;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public List<?> getItems() {
            return items;
        }

        public void setItems(List<?> items) {
            this.items = items;
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
