package com.example.together.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;

public class pro_sound extends AppCompatActivity {
    ImageButton back;
    Button sound1, choice1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_sound);

        back = (ImageButton) findViewById(R.id.back_sound);
        sound1 = (Button) findViewById(R.id.sound1);
        choice1 = (Button) findViewById(R.id.choice1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), MusicService.class));
            }
        });
    }
}
