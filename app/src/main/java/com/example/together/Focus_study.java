package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.together.FdActivity;
import com.example.together.R;

public class Focus_study extends AppCompatActivity {

    ImageButton camera_btn;//카메라 이동 버튼
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_study);
        camera_btn=(ImageButton)findViewById(R.id.camera_btn); //카메라 이동 버튼
        camera_btn.setOnClickListener(new View.OnClickListener() { // 카메라로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FdActivity.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });
    }

}
