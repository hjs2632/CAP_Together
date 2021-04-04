package com.example.together.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Together_home extends AppCompatActivity {

    ImageButton menu_btn, search_btn; //이미지버튼 변수 생성
    LinearLayout group1; //임시로 그룹 누르면 그룹 상세화면 나오도록!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.together_home);

        //변수들 레이아웃 id값이랑 연결
        menu_btn = (ImageButton) findViewById(R.id.menu_btn); //메뉴 버튼
        search_btn = (ImageButton) findViewById(R.id.search_btn); //그룹 검색 버튼


        //플로팅 버튼
        FloatingActionButton fab = findViewById(R.id.fab_btn);

        //그룹 생성 버튼 (플로팅 버튼)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), make_group.class); //그룹 만들기 화면으로 연결
                //intent.putExtra("User", strId); 이거는 이제 변수 넘겨줄거 있으면 쓰는거
                startActivity(intent); //액티비티 열기
            }
        });


        //검색 버튼 누르면 검색화면 나오도록 함
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Together_search.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });

        //그룹 누르면 그룹 상세화면 나오도록 함
        group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), look_group.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });


    }
}