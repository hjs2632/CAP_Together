package com.example.together.Group;
//그룹 상세보기 화면

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class Group_setting extends AppCompatActivity {
    private Intent intent;
    private String Gname,master;

    ImageButton back,setting;
    TextView gname_tv,goaltime_tv,gintro_tv,gcate_tv,gmp_tv;
    Button guse;
    int gmp;
    String gintro,goaltime,gcate;


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //String uid = user.getUid(); //유저 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_setting);

        intent = getIntent();
        Gname = intent.getStringExtra("Gname");
        master = intent.getStringExtra("master");//본인의 마스터정보(yes인지 no인지)



        //변수들 레이아웃 id값이랑 연결
        back = (ImageButton)findViewById(R.id.back);
        gname_tv = (TextView)findViewById(R.id.gname);
        guse = (Button)findViewById(R.id.guse);
        goaltime_tv = (TextView)findViewById(R.id.goaltime);
        gintro_tv = (TextView)findViewById(R.id.gintro);
        gcate_tv = (TextView)findViewById(R.id.gcate);
        gmp_tv = (TextView)findViewById(R.id.gmp);

        gname_tv.setText(gname_tv.getText().toString()+Gname); //그룹명 연결

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        databaseReference.child("Together_group_list").child(Gname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Together_group_list group = dataSnapshot.getValue(Together_group_list.class);
                gintro = group.getGintro();
                goaltime = group.getGoaltime();
                gcate = group.getGcate();
                gmp = group.getgmp();
                goaltime_tv.setText(goaltime_tv.getText().toString()+goaltime+" 시간"); //목표시간
                gintro_tv.setText(gintro_tv.getText().toString()+gintro); //그룹소개
                gcate_tv.setText(gcate_tv.getText().toString()+gcate); //그룹소개
                gmp_tv.setText(gmp_tv.getText().toString()+gmp+" 명");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }



        });

        if(master.equals("yes")){ //수정하기 버튼은 그룹장만 보이도록!
            guse.setVisibility(View.VISIBLE);
        }


        //뒤로가기 버튼
        guse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), make_group.class); //그룹 상세 화면으로 연결
                intent.putExtra("Gname", Gname); //그룹 이름 넘겨서 열기
                startActivity(intent); //액티비티 열기
                finish();
            }
        });


        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
