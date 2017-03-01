package com.maxiaobu.healthclub.common.beangson;

import java.util.List;

/**
 * Created by 马小布 on 2016/9/12.
 */
public class BeanMunbindList {

    /**
     * msgFlag : 1
     * msgContent : 教练俱乐部未绑定列表!
     * unbindList : [{"address":"沈阳市东陵区奥体中心","bapplydescr":"","bapplyid":"","binddate":null,"bindid":"","bindstatus":"","bindstatusname":"","brelievedate":null,"brelievedescr":"","breliever":"","brelievername":"","brelieveuser":"","checkuser":"","clubid":"C000175","clubmemid":"M000444","clubmemimgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000444_1469429374948_p.jpg@!BMEMBER_S","clubmemname":"健身俱乐部CLUB_B管理员","clubname":"健身俱乐部CLUB_B","clubprice":100,"coachid":"","coachprice":0,"createtime":null,"createuser":"","distance":9034.18,"imgsfile":"http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000444_1469429374948_p.jpg@!BMEMBER_S","istrans":"","latitude":41.749034,"longitude":123.46017,"modifytime":null,"modifyuser":"","nickname":"健身俱乐部CLUB_B管理员","pkeyListStr":"","resinformclub":"俱乐部课程预定须知缺省值","resinformcoach":"","transtime":null,"updatetime":null}]
     */

    private String msgFlag;
    private String msgContent;
    /**
     * address : 沈阳市东陵区奥体中心
     * bapplydescr :
     * bapplyid :
     * binddate : null
     * bindid :
     * bindstatus :
     * bindstatusname :
     * brelievedate : null
     * brelievedescr :
     * breliever :
     * brelievername :
     * brelieveuser :
     * checkuser :
     * clubid : C000175
     * clubmemid : M000444
     * clubmemimgsfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000444_1469429374948_p.jpg@!BMEMBER_S
     * clubmemname : 健身俱乐部CLUB_B管理员
     * clubname : 健身俱乐部CLUB_B
     * clubprice : 100
     * coachid :
     * coachprice : 0
     * createtime : null
     * createuser :
     * distance : 9034.18
     * imgsfile : http://efithealthresource.img-cn-beijing.aliyuncs.com/image/bmember/M000444_1469429374948_p.jpg@!BMEMBER_S
     * istrans :
     * latitude : 41.749034
     * longitude : 123.46017
     * modifytime : null
     * modifyuser :
     * nickname : 健身俱乐部CLUB_B管理员
     * pkeyListStr :
     * resinformclub : 俱乐部课程预定须知缺省值
     * resinformcoach :
     * transtime : null
     * updatetime : null
     */

    private List<UnbindListBean> unbindList;

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

    public List<UnbindListBean> getUnbindList() {
        return unbindList;
    }

    public void setUnbindList(List<UnbindListBean> unbindList) {
        this.unbindList = unbindList;
    }

    public static class UnbindListBean {
        private String address;
        private String bapplydescr;
        private String bapplyid;
        private Object binddate;
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
        private Object createtime;
        private String createuser;
        private String distance;
        private String imgsfile;
        private String istrans;
        private String latitude;
        private String longitude;
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

        public Object getBinddate() {
            return binddate;
        }

        public void setBinddate(Object binddate) {
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

        public Object getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Object createtime) {
            this.createtime = createtime;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
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

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
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
    }
}
