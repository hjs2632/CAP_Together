package com.example.together.Timer;

public class Dictionary { //RecyclerView의 한 줄에 보여줄 데이터(item_list.xml에서 정의한 TextView 개수에 맞추어야 한다.)

    private String subject;
    private String page;
    private int time;
    private String Key; //리스트 키값 직접 넣은거

    public Dictionary() {}



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getTime() { return time; }
    public void setTime(int time){this.time= time;}

    public String getKey() { return Key; }
    public void setKey(String Key){this.Key= Key;}


    public Dictionary(String subject, String page, int time) {
        this.subject = subject;
        this.page = page;
        this.time=time;
    }


}
