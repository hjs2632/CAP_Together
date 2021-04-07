package com.example.together.Group;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class make_group extends AppCompatActivity { //우왁굳 개싫어.. ->by.오혜지

    ImageButton backmain1; //이미지버튼 변수 선언
    EditText Gname_edit, Gintro_edit; //그룹 이름, 그룹 설명 변수 선언
    Button addGroup_btn; //그룹 추가하기 버튼

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_group);

        Gname_edit = (EditText)findViewById(R.id.Gname_edit); //그룹명 에딧
        Gintro_edit = (EditText)findViewById(R.id.Gintro_edit); //그룹 설명 에딧
        addGroup_btn = (Button)findViewById(R.id.addGroup_btn); //그룹 추가 버튼
        backmain1 = (ImageButton)findViewById(R.id.backmain1); //뒤로가기 버튼 연결


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결




        //뒤로가기 버튼 (레이아웃 종료)
        backmain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //그룹 추가 버튼 누르면 그룹 생성 후 해당 레이아웃 종료
        addGroup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addGroup(Gname_edit.getText().toString(),Gintro_edit.getText().toString());
                finish();
            }
        });
    }

    public void addGroup(String Gname, String Gintro) {

        int GCP=0; //현재 공부중인 인원은 처음 만든거니까 0
        int GAP=1; //그룹 전체 인원은 방금 만든 본인이 한명 있으니까 1

        Together_group_list Group = new Together_group_list(Gname, Gintro, GCP, GAP);

        //push()를 사용하여 상위 키값이 랜덤으로 나오도록 함. 키 값을 지정하고싶다면 child를 사용하자.
        //그리고 원래 push()가 새로운 생성의 개념으로 많이 쓰이는 것 같음.
        databaseReference.child("Together_group_list").push().setValue(Group);
    }

}
