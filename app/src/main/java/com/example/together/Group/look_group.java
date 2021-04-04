package com.example.together.Group;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;

public class look_group extends AppCompatActivity {

    ImageButton backmain2; //이미지버튼 변수 선언

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_group);

        backmain2 = (ImageButton) findViewById(R.id.backmain2); //버튼 연결

        //버튼 클릭 시 해당레이아웃 종료
        backmain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
