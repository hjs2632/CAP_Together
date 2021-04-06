package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;

public class Study extends AppCompatActivity {

    ImageButton calendar_btn;//일정 이동 버튼
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study);
        calendar_btn = (ImageButton) findViewById(R.id.calendar_btn); //일정 이동 버튼
        calendar_btn.setOnClickListener(new View.OnClickListener() { // 일정으로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Calendar_note.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        })
        ;}
}