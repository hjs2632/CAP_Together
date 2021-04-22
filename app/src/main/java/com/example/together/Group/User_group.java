package com.example.together.Group;
//그룹 가입하고.. 그런거! 가입 후 활동 내역 관리


public class User_group {

    private String uid; //유저 아이디
    private String uname;//유저 닉네임
    private String master;//그룹장 여부

    public User_group(){}



    public String getuid() {
        return uid;
    }

    public void setuid(String useruid) {
        this.uid = uid;
    }

    public String getuname() {
        return uname;
    }

    public void setuname(String uname) {
        this.uname = uname;
    }

    public String getmaster() {
        return master;
    }

    public void setmaster(String master) {
        this.master = master;
    }


    //그룹 가입, 나중에 시간 부분도 추가하자
    public User_group(String uid, String uname, String master) {
        this.uid = uid;
        this.uname = uname;
        this.master = master;
    }
}