package com.example.together.Group;

public class chat_list {

    private String message;
    private String user;
    private String uid;
    private String time;


    // 뭔가 두줄씩 써주면 된다. 그냥 나는 이름을 통일시켜서 만들었음.
    public chat_list(){}


    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public String getuser() {
        return user;
    }

    public void setuser(String user) {
        this.user = user;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }




    public chat_list(String message, String user,String uid, String time) {
        this.message = message;
        this.user = user;
        this.uid = uid;
        this.time = time;
    }



}