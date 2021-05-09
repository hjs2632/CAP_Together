package com.example.together.Timer;

public class Dictionary { //RecyclerView의 한 줄에 보여줄 데이터(item_list.xml에서 정의한 TextView 개수에 맞추어야 한다.)

    private String subject;
    private String page;


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


    public Dictionary(String subject, String page) {
        this.subject = subject;
        this.page = page;
    }
}