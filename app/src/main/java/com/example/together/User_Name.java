package com.example.together;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_Name extends AppCompatActivity {

    Button name_btn;//이름 저장 버튼
    EditText user_name;//이름 입력

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(); // DB 테이블 연결
        user_name=(EditText)findViewById(R.id.user_name);
        name_btn=(Button)findViewById(R.id.name_btn);
        name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("User").child(user.getUid()).child("username");
                    String content=user_name.getText().toString();
                    databaseReference.setValue(content);//선택한 날짜에 일정 저장
                    Toast.makeText(getApplicationContext(),"닉네임 저장 완료",Toast.LENGTH_SHORT).show();//토스메세지 출력
                    finish();
                }catch(Exception e){//예외
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                    finish();
                }
            }
        });
    }
}