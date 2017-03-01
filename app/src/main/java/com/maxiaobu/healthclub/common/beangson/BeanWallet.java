package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/11/14.
 */

public class BeanWallet {

    /**
     * cardList : [{"address":"","bespeaknum":0,"buynum":1,"ca_imgsfilename":"","ca_nickname":"","clubid":"C000174","clubmem":"","clubname":"","coachid":"","coachname":"","coursedays":12,"courseid":"MS000013","coursename":"阿瑟发2","coursenum":0,"coursetimes":12,"gcoursetimes":"","gcoursetype":"","gcoursetypename":"","istrans":"1","ordamt":0,"ordcoursetimes":12,"ordenddate":{"date":23,"day":3,"hours":0,"minutes":0,"month":10,"nanos":0,"seconds":0,"time":1479830400000,"timezoneOffset":-480,"year":116},"ordno":"CO-20161111-410","ordscourseamt":0,"ordstatus":"1","ordstatusname":"","ordtype":"gclub","ordtypename":"","payamt":0,"paystatus":"2","paystatusname":"","paytype":"","paytypename":"","pkeyListStr":"","transstatus":""}]
     * corderList : [{"address":"","bespeaknum":2,"buynum":1,"ca_imgsfilename":"","ca_nickname":"","clubid":"C000174","clubmem":"","clubname":"","coachid":"M000520","coachname":"沫沫教练","coursedays":365,"courseid":"PC000281","coursename":"从入门到转行","coursenum":1,"coursetimes":50,"gcoursetimes":"","gcoursetype":"","gcoursetypename":"","istrans":"1","ordamt":3500,"ordcoursetimes":50,"ordenddate":{"date":29,"day":0,"hours":0,"minutes":0,"month":9,"nanos":0,"seconds":0,"time":1509206400000,"timezoneOffset":-480,"year":117},"ordno":"CO-20161029-333","ordscourseamt":70,"ordstatus":"0","ordstatusname":"","ordtype":"pcoach","ordtypename":"","payamt":3500,"paystatus":"1","paystatusname":"","paytype":"ycoin","paytypename":"","pkeyListStr":"","transstatus":""}]
     * msgContent : 我的卡包
     * msgFlag : 1
     */

    private String msgContent;
    private String msgFlag;
    private List<CardListBean> cardList;
    private List<CorderListBean> corderList;

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

    public List<CardListBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardListBean> cardList) {
        this.cardList = cardList;
    }

    public List<CorderListBean> getCorderList() {
        return corderList;
    }

    public void setCorderList(List<CorderListBean> corderList) {
        this.corderList = corderList;
    }

    public static class CardListBean {
        /**
         * address : 
         * bespeaknum : 0
         * buynum : 1
         * ca_imgsfilename : 
         * ca_nickname : 
         * clubid : C000174
         * clubmem : 
         * clubname : 
         * coachid : 
         * coachname : 
         * coursedays : 12
         * courseid : MS000013
         * coursename : 阿瑟发2
         * coursenum : 0
         * coursetimes : 12
         * gcoursetimes : 
         * gcoursetype : 
         * gcoursetypename : 
         * istrans : 1
         * ordamt : 0
         * ordcoursetimes : 12
         * ordenddate : {"date":23,"day":3,"hours":0,"minutes":0,"month":10,"nanos":0,"seconds":0,"time":1479830400000,"timezoneOffset":-480,"year":116}
         * ordno : CO-20161111-410
         * ordscourseamt : 0
         * ordstatus : 1
         * ordstatusname : 
         * ordtype : gclub
         * ordtypename : 
         * payamt : 0
         * paystatus : 2
         * paystatusname : 
         * paytype : 
         * paytypename : 
         * pkeyListStr : 
         * transstatus : 
         */

        private String address;
        private String bespeaknum;
        private String buynum;
        private String ca_imgsfilename;
        private String ca_nickname;
        private String clubid;
        private String clubmem;
        private String clubname;
        private String coachid;
        private String coachname;
        private String coursedays;
        private String courseid;
        private String coursename;
        private int coursenum;
        private int coursetimes;
        private String gcoursetimes;
        private String gcoursetype;
        private String gcoursetypename;
        private String istrans;
        private String ordamt;
        private String ordcoursetimes;
        private OrdenddateBean ordenddate;
        private String ordno;
        private String ordscourseamt;
        private String ordstatus;
        private String ordstatusname;
        private String ordtype;
        private String ordtypename;
        private String payamt;
        private String paystatus;
        private String paystatusname;
        private String paytype;
        private String paytypename;
        private String pkeyListStr;
        private String transstatus;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBespeaknum() {
            return bespeaknum;
        }

        public void setBespeaknum(String bespeaknum) {
            this.bespeaknum = bespeaknum;
        }

        public String getBuynum() {
            return buynum;
        }

        public void setBuynum(String buynum) {
            this.buynum = buynum;
        }

        public String getCa_imgsfilename() {
            return ca_imgsfilename;
        }

        public void setCa_imgsfilename(String ca_imgsfilename) {
            this.ca_imgsfilename = ca_imgsfilename;
        }

        public String getCa_nickname() {
            return ca_nickname;
        }

        public void setCa_nickname(String ca_nickname) {
            this.ca_nickname = ca_nickname;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public String getClubmem() {
            return clubmem;
        }

        public void setClubmem(String clubmem) {
            this.clubmem = clubmem;
        }

        public String getClubname() {
            return clubname;
        }

        public void setClubname(String clubname) {
            this.clubname = clubname;
        }

        public String getCoachid() {
            return coachid;
        }

        public void setCoachid(String coachid) {
            this.coachid = coachid;
        }

        public String getCoachname() {
            return coachname;
        }

        public void setCoachname(String coachname) {
            this.coachname = coachname;
        }

        public String getCoursedays() {
            return coursedays;
        }

        public void setCoursedays(String coursedays) {
            this.coursedays = coursedays;
        }

        public String getCourseid() {
            return courseid;
        }

        public void setCourseid(String courseid) {
            this.courseid = courseid;
        }

        public String getCoursename() {
            return coursename;
        }

        public void setCoursename(String coursename) {
            this.coursename = coursename;
        }

        public int getCoursenum() {
            return coursenum;
        }

        public void setCoursenum(int coursenum) {
            this.coursenum = coursenum;
        }

        public int getCoursetimes() {
            return coursetimes;
        }

        public void setCoursetimes(int coursetimes) {
            this.coursetimes = coursetimes;
        }

        public String getGcoursetimes() {
            return gcoursetimes;
        }

        public void setGcoursetimes(String gcoursetimes) {
            this.gcoursetimes = gcoursetimes;
        }

        public String getGcoursetype() {
            return gcoursetype;
        }

        public void setGcoursetype(String gcoursetype) {
            this.gcoursetype = gcoursetype;
        }

        public String getGcoursetypename() {
            return gcoursetypename;
        }

        public void setGcoursetypename(String gcoursetypename) {
            this.gcoursetypename = gcoursetypename;
        }

        public String getIstrans() {
            return istrans;
        }

        public void setIstrans(String istrans) {
            this.istrans = istrans;
        }

        public String getOrdamt() {
            return ordamt;
        }

        public void setOrdamt(String ordamt) {
            this.ordamt = ordamt;
        }

        public String getOrdcoursetimes() {
            return ordcoursetimes;
        }

        public void setOrdcoursetimes(String ordcoursetimes) {
            this.ordcoursetimes = ordcoursetimes;
        }

        public OrdenddateBean getOrdenddate() {
            return ordenddate;
        }

        public void setOrdenddate(OrdenddateBean ordenddate) {
            this.ordenddate = ordenddate;
        }

        public String getOrdno() {
            return ordno;
        }

        public void setOrdno(String ordno) {
            this.ordno = ordno;
        }

        public String getOrdscourseamt() {
            return ordscourseamt;
        }

        public void setOrdscourseamt(String ordscourseamt) {
            this.ordscourseamt = ordscourseamt;
        }

        public String getOrdstatus() {
            return ordstatus;
        }

        public void setOrdstatus(String ordstatus) {
            this.ordstatus = ordstatus;
        }

        public String getOrdstatusname() {
            return ordstatusname;
        }

        public void setOrdstatusname(String ordstatusname) {
            this.ordstatusname = ordstatusname;
        }

        public String getOrdtype() {
            return ordtype;
        }

        public void setOrdtype(String ordtype) {
            this.ordtype = ordtype;
        }

        public String getOrdtypename() {
            return ordtypename;
        }

        public void setOrdtypename(String ordtypename) {
            this.ordtypename = ordtypename;
        }

        public String getPayamt() {
            return payamt;
        }

        public void setPayamt(String payamt) {
            this.payamt = payamt;
        }

        public String getPaystatus() {
            return paystatus;
        }

        public void setPaystatus(String paystatus) {
            this.paystatus = paystatus;
        }

        public String getPaystatusname() {
            return paystatusname;
        }

        public void setPaystatusname(String paystatusname) {
            this.paystatusname = paystatusname;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getPaytypename() {
            return paytypename;
        }

        public void setPaytypename(String paytypename) {
            this.paytypename = paytypename;
        }

        public String getPkeyListStr() {
            return pkeyListStr;
        }

        public void setPkeyListStr(String pkeyListStr) {
            this.pkeyListStr = pkeyListStr;
        }

        public String getTransstatus() {
            return transstatus;
        }

        public void setTransstatus(String transstatus) {
            this.transstatus = transstatus;
        }

        public static class OrdenddateBean {
            /**
             * date : 23
             * day : 3
             * hours : 0
             * minutes : 0
             * month : 10
             * nanos : 0
             * seconds : 0
             * time : 1479830400000
             * timezoneOffset : -480
             * year : 116
             */

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

    public static class CorderListBean {
        /**
         * address : 
         * bespeaknum : 2
         * buynum : 1
         * ca_imgsfilename : 
         * ca_nickname : 
         * clubid : C000174
         * clubmem : 
         * clubname : 
         * coachid : M000520
         * coachname : 沫沫教练
         * coursedays : 365
         * courseid : PC000281
         * coursename : 从入门到转行
         * coursenum : 1
         * coursetimes : 50
         * gcoursetimes : 
         * gcoursetype : 
         * gcoursetypename : 
         * istrans : 1
         * ordamt : 3500
         * ordcoursetimes : 50
         * ordenddate : {"date":29,"day":0,"hours":0,"minutes":0,"month":9,"nanos":0,"seconds":0,"time":1509206400000,"timezoneOffset":-480,"year":117}
         * ordno : CO-20161029-333
         * ordscourseamt : 70
         * ordstatus : 0
         * ordstatusname : 
         * ordtype : pcoach
         * ordtypename : 
         * payamt : 3500
         * paystatus : 1
         * paystatusname : 
         * paytype : ycoin
         * paytypename : 
         * pkeyListStr : 
         * transstatus : 
         */

        private String address;
        private String bespeaknum;
        private String buynum;
        private String ca_imgsfilename;
        private String ca_nickname;
        private String clubid;
        private String clubmem;
        private String clubname;
        private String coachid;
        private String coachname;
        private String coursedays;
        private String courseid;
        private String coursename;
        private String coursenum;
        private String coursetimes;
        private String gcoursetimes;
        private String gcoursetype;
        private String gcoursetypename;
        private String istrans;
        private String ordamt;
        private String ordcoursetimes;
        private OrdenddateBeanX ordenddate;
        private String ordno;
        private String ordscourseamt;
        private String ordstatus;
        private String ordstatusname;
        private String ordtype;
        private String ordtypename;
        private String payamt;
        private String paystatus;
        private String paystatusname;
        private String paytype;
        private String paytypename;
        private String pkeyListStr;
        private String transstatus;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBespeaknum() {
            return bespeaknum;
        }

        public void setBespeaknum(String bespeaknum) {
            this.bespeaknum = bespeaknum;
        }

        public String getBuynum() {
            return buynum;
        }

        public void setBuynum(String buynum) {
            this.buynum = buynum;
        }

        public String getCa_imgsfilename() {
            return ca_imgsfilename;
        }

        public void setCa_imgsfilename(String ca_imgsfilename) {
            this.ca_imgsfilename = ca_imgsfilename;
        }

        public String getCa_nickname() {
            return ca_nickname;
        }

        public void setCa_nickname(String ca_nickname) {
            this.ca_nickname = ca_nickname;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public String getClubmem() {
            return clubmem;
        }

        public void setClubmem(String clubmem) {
            this.clubmem = clubmem;
        }

        public String getClubname() {
            return clubname;
        }

        public void setClubname(String clubname) {
            this.clubname = clubname;
        }

        public String getCoachid() {
            return coachid;
        }

        public void setCoachid(String coachid) {
            this.coachid = coachid;
        }

        public String getCoachname() {
            return coachname;
        }

        public void setCoachname(String coachname) {
            this.coachname = coachname;
        }

        public String getCoursedays() {
            return coursedays;
        }

        public void setCoursedays(String coursedays) {
            this.coursedays = coursedays;
        }

        public String getCourseid() {
            return courseid;
        }

        public void setCourseid(String courseid) {
            this.courseid = courseid;
        }

        public String getCoursename() {
            return coursename;
        }

        public void setCoursename(String coursename) {
            this.coursename = coursename;
        }

        public String getCoursenum() {
            return coursenum;
        }

        public void setCoursenum(String coursenum) {
            this.coursenum = coursenum;
        }

        public String getCoursetimes() {
            return coursetimes;
        }

        public void setCoursetimes(String coursetimes) {
            this.coursetimes = coursetimes;
        }

        public String getGcoursetimes() {
            return gcoursetimes;
        }

        public void setGcoursetimes(String gcoursetimes) {
            this.gcoursetimes = gcoursetimes;
        }

        public String getGcoursetype() {
            return gcoursetype;
        }

        public void setGcoursetype(String gcoursetype) {
            this.gcoursetype = gcoursetype;
        }

        public String getGcoursetypename() {
            return gcoursetypename;
        }

        public void setGcoursetypename(String gcoursetypename) {
            this.gcoursetypename = gcoursetypename;
        }

        public String getIstrans() {
            return istrans;
        }

        public void setIstrans(String istrans) {
            this.istrans = istrans;
        }

        public String getOrdamt() {
            return ordamt;
        }

        public void setOrdamt(String ordamt) {
            this.ordamt = ordamt;
        }

        public String getOrdcoursetimes() {
            return ordcoursetimes;
        }

        public void setOrdcoursetimes(String ordcoursetimes) {
            this.ordcoursetimes = ordcoursetimes;
        }

        public OrdenddateBeanX getOrdenddate() {
            return ordenddate;
        }

        public void setOrdenddate(OrdenddateBeanX ordenddate) {
            this.ordenddate = ordenddate;
        }

        public String getOrdno() {
            return ordno;
        }

        public void setOrdno(String ordno) {
            this.ordno = ordno;
        }

        public String getOrdscourseamt() {
            return ordscourseamt;
        }

        public void setOrdscourseamt(String ordscourseamt) {
            this.ordscourseamt = ordscourseamt;
        }

        public String getOrdstatus() {
            return ordstatus;
        }

        public void setOrdstatus(String ordstatus) {
            this.ordstatus = ordstatus;
        }

        public String getOrdstatusname() {
            return ordstatusname;
        }

        public void setOrdstatusname(String ordstatusname) {
            this.ordstatusname = ordstatusname;
        }

        public String getOrdtype() {
            return ordtype;
        }

        public void setOrdtype(String ordtype) {
            this.ordtype = ordtype;
        }

        public String getOrdtypename() {
            return ordtypename;
        }

        public void setOrdtypename(String ordtypename) {
            this.ordtypename = ordtypename;
        }

        public String getPayamt() {
            return payamt;
        }

        public void setPayamt(String payamt) {
            this.payamt = payamt;
        }

        public String getPaystatus() {
            return paystatus;
        }

        public void setPaystatus(String paystatus) {
            this.paystatus = paystatus;
        }

        public String getPaystatusname() {
            return paystatusname;
        }

        public void setPaystatusname(String paystatusname) {
            this.paystatusname = paystatusname;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getPaytypename() {
            return paytypename;
        }

        public void setPaytypename(String paytypename) {
            this.paytypename = paytypename;
        }

        public String getPkeyListStr() {
            return pkeyListStr;
        }

        public void setPkeyListStr(String pkeyListStr) {
            this.pkeyListStr = pkeyListStr;
        }

        public String getTransstatus() {
            return transstatus;
        }

        public void setTransstatus(String transstatus) {
            this.transstatus = transstatus;
        }

        public static class OrdenddateBeanX {
            /**
             * date : 29
             * day : 0
             * hours : 0
             * minutes : 0
             * month : 9
             * nanos : 0
             * seconds : 0
             * time : 1509206400000
             * timezoneOffset : -480
             * year : 117
             */

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
