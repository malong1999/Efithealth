package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/8/22.
 */
public class BeanFoodDetail {

    /**
     * msgFlag : 1
     * msgContent : 配餐单页
     * bFoodmer : {"caloric":0,"clubsplitamt":0.5,"clubsplitratio":0,"coachsplitamt":0.5,"coachsplitratio":0,"coh":0,"compodescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","createtime":{"date":13,"day":6,"hours":9,"minutes":30,"month":7,"nanos":0,"seconds":37,"time":1471051837000,"timezoneOffset":-480,"year":116},"createuser":"platAdmin","deliverydays":1,"detailcontent":"\r\n\t撒旦法撒旦法是的发送到发送到分\r\n<\/p>\r\n\r\n\t阿斯顿发送到发送到发生法师打发斯蒂芬\r\n<\/p>\r\n","detailimg":null,"detailsurl":"","energydescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","fats":0,"foodmertype":"","fplatConperson":"包贝尔","fplatConphone":"13912345678","fqrimgfile":null,"fri":"2","gmsplitamt":0,"imgdetail":"","imgfile":null,"imgfileFileName":"","imgpfile":"/image/bfoodmer/M000024_1471051836026_p.jpg","imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000024_1471051836026_p.jpg","imgsfile":"/image/bfoodmer/M000024_1471051836026_s.jpg","imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000024_1471051836026_s.jpg","mercode":"","merdescr":"壹想食健身周单餐","merid":"M000024","mername":"壹想食健身周午餐","merprice":10,"mertype":"2","mertypename":"塑形","modifytime":{"date":13,"day":6,"hours":9,"minutes":31,"month":7,"nanos":0,"seconds":32,"time":1471051892000,"timezoneOffset":-480,"year":116},"modifyuser":"platAdmin","mon":"2","ordernotice":"配餐高峰期请提前一小时下单","pkeyListStr":"","plantfiber":0,"platsplitamt":0,"platsplitratio":0,"protein":0,"qrimgfile":"/image/bfoodmerqr/M000024_1471051837099_p.png","qrimgfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmerqr/M000024_1471051837099_p.png","remark":"","salenum":0,"sat":"0","sodium":0,"status":"1","statusname":"有效","sun":"0","thur":"2","tues":"2","wed":"2"}
     */

    private String msgFlag;
    private String msgContent;
    /**
     * caloric : 0
     * clubsplitamt : 0.5
     * clubsplitratio : 0
     * coachsplitamt : 0.5
     * coachsplitratio : 0
     * coh : 0
     * compodescr : 热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg
     * createtime : {"date":13,"day":6,"hours":9,"minutes":30,"month":7,"nanos":0,"seconds":37,"time":1471051837000,"timezoneOffset":-480,"year":116}
     * createuser : platAdmin
     * deliverydays : 1
     * detailcontent :
     撒旦法撒旦法是的发送到发送到分
     </p>

     阿斯顿发送到发送到发生法师打发斯蒂芬
     </p>

     * detailimg : null
     * detailsurl :
     * energydescr : 热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg
     * fats : 0
     * foodmertype :
     * fplatConperson : 包贝尔
     * fplatConphone : 13912345678
     * fqrimgfile : null
     * fri : 2
     * gmsplitamt : 0
     * imgdetail :
     * imgfile : null
     * imgfileFileName :
     * imgpfile : /image/bfoodmer/M000024_1471051836026_p.jpg
     * imgpfilename : http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000024_1471051836026_p.jpg
     * imgsfile : /image/bfoodmer/M000024_1471051836026_s.jpg
     * imgsfilename : http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmer/M000024_1471051836026_s.jpg
     * mercode :
     * merdescr : 壹想食健身周单餐
     * merid : M000024
     * mername : 壹想食健身周午餐
     * merprice : 10
     * mertype : 2
     * mertypename : 塑形
     * modifytime : {"date":13,"day":6,"hours":9,"minutes":31,"month":7,"nanos":0,"seconds":32,"time":1471051892000,"timezoneOffset":-480,"year":116}
     * modifyuser : platAdmin
     * mon : 2
     * ordernotice : 配餐高峰期请提前一小时下单
     * pkeyListStr :
     * plantfiber : 0
     * platsplitamt : 0
     * platsplitratio : 0
     * protein : 0
     * qrimgfile : /image/bfoodmerqr/M000024_1471051837099_p.png
     * qrimgfilename : http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bfoodmerqr/M000024_1471051837099_p.png
     * remark :
     * salenum : 0
     * sat : 0
     * sodium : 0
     * status : 1
     * statusname : 有效
     * sun : 0
     * thur : 2
     * tues : 2
     * wed : 2
     */

    private BFoodmerBean bFoodmer;

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

    public BFoodmerBean getBFoodmer() {
        return bFoodmer;
    }

    public void setBFoodmer(BFoodmerBean bFoodmer) {
        this.bFoodmer = bFoodmer;
    }

    public static class BFoodmerBean {
        private String caloric;
        private String clubsplitamt;
        private String clubsplitratio;
        private String coachsplitamt;
        private String coachsplitratio;
        private String coh;
        private String compodescr;
        /**
         * date : 13
         * day : 6
         * hours : 9
         * minutes : 30
         * month : 7
         * nanos : 0
         * seconds : 37
         * time : 1471051837000
         * timezoneOffset : -480
         * year : 116
         */

        private CreatetimeBean createtime;
        private String createuser;
        private String deliverydays;
        private String detailcontent;
        private Object detailimg;
        private String detailsurl;
        private String energydescr;
        private String fats;
        private String foodmertype;
        private String fplatConperson;
        private String fplatConphone;
        private Object fqrimgfile;
        private String fri;
        private String gmsplitamt;
        private String imgdetail;
        private Object imgfile;
        private String imgfileFileName;
        private String imgpfile;
        private String imgpfilename;
        private String imgsfile;
        private String imgsfilename;
        private String mercode;
        private String merdescr;
        private String merid;
        private String mername;
        private String merprice;
        private String mertype;
        private String mertypename;
        /**
         * date : 13
         * day : 6
         * hours : 9
         * minutes : 31
         * month : 7
         * nanos : 0
         * seconds : 32
         * time : 1471051892000
         * timezoneOffset : -480
         * year : 116
         */

        private ModifytimeBean modifytime;
        private String modifyuser;
        private String mon;
        private String ordernotice;
        private String pkeyListStr;
        private String plantfiber;
        private String platsplitamt;
        private String platsplitratio;
        private String protein;
        private String qrimgfile;
        private String qrimgfilename;
        private String remark;
        private String salenum;
        private String sat;
        private String sodium;
        private String status;
        private String statusname;
        private String sun;
        private String thur;
        private String tues;
        private String wed;

        public String getCaloric() {
            return caloric;
        }

        public void setCaloric(String caloric) {
            this.caloric = caloric;
        }

        public String getClubsplitamt() {
            return clubsplitamt;
        }

        public void setClubsplitamt(String clubsplitamt) {
            this.clubsplitamt = clubsplitamt;
        }

        public String getClubsplitratio() {
            return clubsplitratio;
        }

        public void setClubsplitratio(String clubsplitratio) {
            this.clubsplitratio = clubsplitratio;
        }

        public String getCoachsplitamt() {
            return coachsplitamt;
        }

        public void setCoachsplitamt(String coachsplitamt) {
            this.coachsplitamt = coachsplitamt;
        }

        public String getCoachsplitratio() {
            return coachsplitratio;
        }

        public void setCoachsplitratio(String coachsplitratio) {
            this.coachsplitratio = coachsplitratio;
        }

        public String getCoh() {
            return coh;
        }

        public void setCoh(String coh) {
            this.coh = coh;
        }

        public String getCompodescr() {
            return compodescr;
        }

        public void setCompodescr(String compodescr) {
            this.compodescr = compodescr;
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

        public String getDeliverydays() {
            return deliverydays;
        }

        public void setDeliverydays(String deliverydays) {
            this.deliverydays = deliverydays;
        }

        public String getDetailcontent() {
            return detailcontent;
        }

        public void setDetailcontent(String detailcontent) {
            this.detailcontent = detailcontent;
        }

        public Object getDetailimg() {
            return detailimg;
        }

        public void setDetailimg(Object detailimg) {
            this.detailimg = detailimg;
        }

        public String getDetailsurl() {
            return detailsurl;
        }

        public void setDetailsurl(String detailsurl) {
            this.detailsurl = detailsurl;
        }

        public String getEnergydescr() {
            return energydescr;
        }

        public void setEnergydescr(String energydescr) {
            this.energydescr = energydescr;
        }

        public String getFats() {
            return fats;
        }

        public void setFats(String fats) {
            this.fats = fats;
        }

        public String getFoodmertype() {
            return foodmertype;
        }

        public void setFoodmertype(String foodmertype) {
            this.foodmertype = foodmertype;
        }

        public String getFplatConperson() {
            return fplatConperson;
        }

        public void setFplatConperson(String fplatConperson) {
            this.fplatConperson = fplatConperson;
        }

        public String getFplatConphone() {
            return fplatConphone;
        }

        public void setFplatConphone(String fplatConphone) {
            this.fplatConphone = fplatConphone;
        }

        public Object getFqrimgfile() {
            return fqrimgfile;
        }

        public void setFqrimgfile(Object fqrimgfile) {
            this.fqrimgfile = fqrimgfile;
        }

        public String getFri() {
            return fri;
        }

        public void setFri(String fri) {
            this.fri = fri;
        }

        public String getGmsplitamt() {
            return gmsplitamt;
        }

        public void setGmsplitamt(String gmsplitamt) {
            this.gmsplitamt = gmsplitamt;
        }

        public String getImgdetail() {
            return imgdetail;
        }

        public void setImgdetail(String imgdetail) {
            this.imgdetail = imgdetail;
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

        public String getMercode() {
            return mercode;
        }

        public void setMercode(String mercode) {
            this.mercode = mercode;
        }

        public String getMerdescr() {
            return merdescr;
        }

        public void setMerdescr(String merdescr) {
            this.merdescr = merdescr;
        }

        public String getMerid() {
            return merid;
        }

        public void setMerid(String merid) {
            this.merid = merid;
        }

        public String getMername() {
            return mername;
        }

        public void setMername(String mername) {
            this.mername = mername;
        }

        public String getMerprice() {
            return merprice;
        }

        public void setMerprice(String merprice) {
            this.merprice = merprice;
        }

        public String getMertype() {
            return mertype;
        }

        public void setMertype(String mertype) {
            this.mertype = mertype;
        }

        public String getMertypename() {
            return mertypename;
        }

        public void setMertypename(String mertypename) {
            this.mertypename = mertypename;
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

        public String getMon() {
            return mon;
        }

        public void setMon(String mon) {
            this.mon = mon;
        }

        public String getOrdernotice() {
            return ordernotice;
        }

        public void setOrdernotice(String ordernotice) {
            this.ordernotice = ordernotice;
        }

        public String getPkeyListStr() {
            return pkeyListStr;
        }

        public void setPkeyListStr(String pkeyListStr) {
            this.pkeyListStr = pkeyListStr;
        }

        public String getPlantfiber() {
            return plantfiber;
        }

        public void setPlantfiber(String plantfiber) {
            this.plantfiber = plantfiber;
        }

        public String getPlatsplitamt() {
            return platsplitamt;
        }

        public void setPlatsplitamt(String platsplitamt) {
            this.platsplitamt = platsplitamt;
        }

        public String getPlatsplitratio() {
            return platsplitratio;
        }

        public void setPlatsplitratio(String platsplitratio) {
            this.platsplitratio = platsplitratio;
        }

        public String getProtein() {
            return protein;
        }

        public void setProtein(String protein) {
            this.protein = protein;
        }

        public String getQrimgfile() {
            return qrimgfile;
        }

        public void setQrimgfile(String qrimgfile) {
            this.qrimgfile = qrimgfile;
        }

        public String getQrimgfilename() {
            return qrimgfilename;
        }

        public void setQrimgfilename(String qrimgfilename) {
            this.qrimgfilename = qrimgfilename;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSalenum() {
            return salenum;
        }

        public void setSalenum(String salenum) {
            this.salenum = salenum;
        }

        public String getSat() {
            return sat;
        }

        public void setSat(String sat) {
            this.sat = sat;
        }

        public String getSodium() {
            return sodium;
        }

        public void setSodium(String sodium) {
            this.sodium = sodium;
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

        public String getSun() {
            return sun;
        }

        public void setSun(String sun) {
            this.sun = sun;
        }

        public String getThur() {
            return thur;
        }

        public void setThur(String thur) {
            this.thur = thur;
        }

        public String getTues() {
            return tues;
        }

        public void setTues(String tues) {
            this.tues = tues;
        }

        public String getWed() {
            return wed;
        }

        public void setWed(String wed) {
            this.wed = wed;
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
