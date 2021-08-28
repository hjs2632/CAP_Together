package com.example.together.Group;
//그룹내 사용자 확인


public class User_group {

    private String uid; //유저 아이디
    private String uname;//유저 닉네임
    private String master;//그룹장 여부
    private int studytime;

    public User_group() {
    }


    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
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

    public int getstudytime() {
        return studytime;
    }

    public void setstudytime(int studytime) {
        this.studytime = studytime;
    }


    //그룹 가입
    public User_group(String uid, String uname, String master, int studytime) {
        this.uid = uid;
        this.uname = uname;
        this.master = master;
        this.studytime = studytime;
    }
}
