package com.example.together.Group;
//그룹 생성시 쓰는 리스트. 유저키 안으로 들어간다.
//본인이 가입되어있는 그룹들이 모여있는 쪽으로 저장된다.

public class gmake_list {

    private String gname;
    private String master;


    // 뭔가 두줄씩 써주면 된다. 그냥 나는 이름을 통일시켜서 만들었음.
    public gmake_list(){}


    public String getgname() {
        return gname;
    }

    public void setgname(String gname) {
        this.gname = gname;
    }

    public String getmaster() {
        return master;
    }

    public void setmaster(String master) {
        this.master = master;
    }




    public gmake_list(String gname, String master) {
        this.gname = gname;
        this.master = master;
    }



}