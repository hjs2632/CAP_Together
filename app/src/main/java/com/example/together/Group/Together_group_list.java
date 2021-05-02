package com.example.together.Group;
//그룹 생성시 사용.
//투게더 그룹 리스트 키 안으로 들어간다.

public class Together_group_list {

    private int GAP; //그룹 전체 인원
    private String Gname; //그룹 이름
    private String Gintro; //그룹 설명
    private String Goaltime; //목표 시간
    private String master; //그룹장
    private String Gcate;//카테고리
    private int gmp;
    private String iv_people; //인원 앞에 사람 이미지


    // 뭔가 두줄씩 써주면 된다. 그냥 나는 이름을 통일시켜서 만들었음.
    public Together_group_list(){}
    /* 사진 부분인데 그룹에서는 굳이 데이터베이스로 쓸 이유가 없어서 일단 잠궈둠
    public String getiv_people() {
        return iv_people;
    }
    public void setiv_people(String iv_people) { this.iv_people = iv_people; }
     */

    //여기 작성 잘못하면 데이터 저장 안됩니다~~
    public String getGname() {
        return Gname;
    }

    public void setGname(String Gname) {
        this.Gname = Gname;
    }

    public int getGAP() {
        return GAP;
    }

    public void setGAP(int GAP) {
        this.GAP = GAP;
    }

    public String getGintro() {
        return Gintro;
    }

    public void setGintro(String Gintro) {
        this.Gintro = Gintro;
    }

    public String getGoaltime() {
        return Goaltime;
    }

    public void setGoaltime(String Goaltime) { this.Goaltime = Goaltime; }

    public String getmaster() {
        return master;
    }

    public void setmaster(String userId) {
        this.master = master;
    }

    public String getGcate() {
        return Gcate;
    }

    public void setGcate(String Gcate) { this.Gcate = Gcate; }

    public int getgmp() {
        return gmp;
    }

    public void setgmp(int gmp) { this.gmp = gmp; }



    //그룹 만들기에서 사용할 녀석
    public Together_group_list(String Gname, String Gintro, String Gcate, int gmp, int GAP, String Goaltime, String master) {
        this.Gname = Gname;
        this.Gintro = Gintro;
        this.Gcate = Gcate;
        this.gmp = gmp;
        this.GAP = GAP;
        this.Goaltime = Goaltime;
        this.master = master;

    }



}