package com.example.together;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.together.Group.Together_Customlist;

@SuppressWarnings("deprecation")

public class Tab_activity extends TabActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);

        TabHost tabHost = getTabHost(); //탭 호스트 객체 생성

        // 탭스팩 선언하고, 탭의 내부 명칭, 탭에 출력될 글 작성
        TabHost.TabSpec spec1, spec2, spec3;

        Intent intent;  //객체

        intent = new Intent().setClass(this, Study.class); //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        spec1 = tabHost.newTabSpec("공부하기"); // 객체를 생성
        spec1.setIndicator("공부하기"); //탭의 이름 설정
        spec1.setContent(intent);
        tabHost.addTab(spec1);

        intent = new Intent().setClass(this, Daily.class); //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        spec2 = tabHost.newTabSpec("Daily"); // 객체를 생성
        spec2.setIndicator("통계"); //탭의 이름 설정
        spec2.setContent(intent);
        tabHost.addTab(spec2);

        intent = new Intent().setClass(this, Together_Customlist.class); //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        spec3 = tabHost.newTabSpec("Together_home"); // 객체를 생성
        spec3.setIndicator("그룹"); //탭의 이름 설정
        spec3.setContent(intent);
        tabHost.addTab(spec3);

        tabHost.setCurrentTab(0); //먼저 열릴 탭을 선택! (0)로 해두면 메인화면(혼자공부)가 시작 화면!


    }


}
