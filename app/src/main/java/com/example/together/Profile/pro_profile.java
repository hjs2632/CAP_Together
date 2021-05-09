package com.example.together.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;

public class pro_profile extends AppCompatActivity {
    ImageButton back;
    Button edit_pro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_profile);

        back = (ImageButton)findViewById(R.id.back); //버튼 연결
        edit_pro = (Button)findViewById(R.id.edit_pro);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pro_edit_profile.class);
                startActivity(intent);
            }
        });
    }
}
