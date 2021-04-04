package com.example.together.Group;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;


public class Together_search extends AppCompatActivity {

    ImageButton back_btn, search_btn; //이미지버튼 변수 생성


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.together_search);

        //변수 연결
        back_btn = (ImageButton) findViewById(R.id.back_btn);


        //뒤로가기 버튼 누르면 화면을 닫음
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}