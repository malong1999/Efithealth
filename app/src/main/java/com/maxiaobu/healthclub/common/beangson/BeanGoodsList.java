package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/8/17.
 */
public class BeanGoodsList {

    /**
     * msgFlag : 1
     * msgContent : 配餐列表
     * list : [{"compodescr":"沙拉：牛肉肠，鹰嘴豆，西红柿，玉米粒，紫甘兰，山药，葡萄干 ，西兰花","detailsurl":"","energydescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","fplatConperson":"","fplatConphone":"","imgpfile":"/image/bfoodmer/M000033_1470635064063_p.jpg","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/M000033_1470635064063_p.jpg","imgsfile":"/image/bfoodmer/M000033_1470635064063_s.jpg","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/M000033_1470635064063_s.jpg","merdescr":"一周减脂午餐","merid":"M000033","mername":"一周减脂午餐","merprice":200,"mertype":"1"},{"compodescr":"一周：午餐+晚餐","detailsurl":"","energydescr":"热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg","fplatConperson":"","fplatConphone":"","imgpfile":"/image/bfoodmer/M000034_1471577870554_p.jpg","imgpfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/M000034_1471577870554_p.jpg","imgsfile":"/image/bfoodmer/M000034_1471577870554_s.jpg","imgsfilename":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/M000034_1471577870554_s.jpg","merdescr":"一周：午餐+晚餐","merid":"M000034","mername":"一周：午餐+晚餐","merprice":300,"mertype":"1"}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * compodescr : 沙拉：牛肉肠，鹰嘴豆，西红柿，玉米粒，紫甘兰，山药，葡萄干 ，西兰花
     * detailsurl : 
     * energydescr : 热量: 0 kcal, 蛋白质含量: 0g, 脂肪含量: 0g, 碳水化合物: 0g, 植物纤维: 0g, 钠: 0mg
     * fplatConperson : 
     * fplatConphone : 
     * imgpfile : /image/bfoodmer/M000033_1470635064063_p.jpg
     * imgpfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/M000033_1470635064063_p.jpg
     * imgsfile : /image/bfoodmer/M000033_1470635064063_s.jpg
     * imgsfilename : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bfoodmer/M000033_1470635064063_s.jpg
     * merdescr : 一周减脂午餐
     * merid : M000033
     * mername : 一周减脂午餐
     * merprice : 200
     * mertype : 1
     */

    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String compodescr;
        private String detailsurl;
        private String energydescr;
        private String fplatConperson;
        private String fplatConphone;
        private String imgpfile;
        private String imgpfilename;
        private String imgsfile;
        private String imgsfilename;
        private String merdescr;
        private String merid;
        private String mername;
        private String merprice;
        private String mertype;

        public String getCompodescr() {
            return compodescr;
        }

        public void setCompodescr(String compodescr) {
            this.compodescr = compodescr;
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
    }
}
