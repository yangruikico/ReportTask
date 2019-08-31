package com.gcstorage.reportservice.mode;

import java.util.ArrayList;
import java.util.List;

public class TaskBean {


    private List<DataBean> data;

    private List<UserBean> user;

    public List<UserBean> getUser() {
        if (user == null) {
            return new ArrayList<>();
        }
        return user;
    }

    public void setUser(List<UserBean> user) {
        this.user = user;
    }

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * planid : a87cd837572b4c409f70d1226ed287f2
         * mjcount : 5
         * fjcount : 5
         * carcount : 5
         * receivecount : 0
         * isReceive : 0
         * dutystarttimes : 1564588800000
         * dutyendtimes : 1567180800000
         * tbLal : []
         */

        private String planid;
        private int mjcount;
        private int fjcount;
        private int carcount;
        private int receivecount;
        private int isReceive;
        private long dutystarttimes;
        private long dutyendtimes;
        private List<?> tbLal;


        private String upid;


        public String getUpid() {
            return upid == null ? "" : upid;
        }

        public void setUpid(String upid) {
            this.upid = upid;
        }

        public String getPlanid() {
            return planid;
        }

        public void setPlanid(String planid) {
            this.planid = planid;
        }

        public int getMjcount() {
            return mjcount;
        }

        public void setMjcount(int mjcount) {
            this.mjcount = mjcount;
        }

        public int getFjcount() {
            return fjcount;
        }

        public void setFjcount(int fjcount) {
            this.fjcount = fjcount;
        }

        public int getCarcount() {
            return carcount;
        }

        public void setCarcount(int carcount) {
            this.carcount = carcount;
        }

        public int getReceivecount() {
            return receivecount;
        }

        public void setReceivecount(int receivecount) {
            this.receivecount = receivecount;
        }

        public int getIsReceive() {
            return isReceive;
        }

        public void setIsReceive(int isReceive) {
            this.isReceive = isReceive;
        }

        public long getDutystarttimes() {
            return dutystarttimes;
        }

        public void setDutystarttimes(long dutystarttimes) {
            this.dutystarttimes = dutystarttimes;
        }

        public long getDutyendtimes() {
            return dutyendtimes;
        }

        public void setDutyendtimes(long dutyendtimes) {
            this.dutyendtimes = dutyendtimes;
        }

        public List<?> getTbLal() {
            return tbLal;
        }

        public void setTbLal(List<?> tbLal) {
            this.tbLal = tbLal;
        }
    }


    public static class UserBean{


        /**
         * uid : ef3de0c0c2ea11e8b81600163e000838
         * orgname : 湖北省武汉市公安局治安管理支队警务指挥室
         * policenum : 015621
         * username : 张青
         */

        private String name;
        private String orgname;
        private String policenum;
        private String username;

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrgname() {
            return orgname == null ? "" : orgname;
        }

        public void setOrgname(String orgname) {
            this.orgname = orgname;
        }

        public String getPolicenum() {
            return policenum == null ? "" : policenum;
        }

        public void setPolicenum(String policenum) {
            this.policenum = policenum;
        }

        public String getUsername() {
            return username == null ? "" : username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
