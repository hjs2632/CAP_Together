package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.Calendar.Calendar_note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Study extends AppCompatActivity {

    ImageButton calendar_btn;//일정 이동 버튼
    ImageButton focus_btn;//집중모드 이동 버튼

    EditText test_id;
    Button test_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();


        database=FirebaseDatabase.getInstance();

        calendar_btn = (ImageButton) findViewById(R.id.calendar_btn); //일정 이동 버튼
        focus_btn=(ImageButton)findViewById(R.id.focus_btn); //집중모드 이동 버튼

        test_id=(EditText)findViewById(R.id.test_id);
        test_btn=(Button)findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("User").child(user.getUid()).child("username");
                    String content=test_id.getText().toString();
                    databaseReference.setValue(content);//선택한 날짜에 일정 저장
                    Toast.makeText(getApplicationContext(),"일정 저장 완료",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }catch(Exception e){//예외
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() { // 일정으로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Calendar_note.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });
        focus_btn.setOnClickListener(new View.OnClickListener() { // 집중모드로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Focus_study.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });
    }
}