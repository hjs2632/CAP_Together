package com.example.together.Group;
//그룹 상세보기 화면

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


public class look_group extends AppCompatActivity {
    private Intent intent;
    private String Gname,master;

    ImageButton back;
    TextView gname;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User_group> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //String uid = user.getUid(); //유저 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_group);

        intent = getIntent();
        Gname = intent.getStringExtra("Gname");
        master = intent.getStringExtra("master");//본인의 마스터정보(yes인지 no인지)

        //변수들 레이아웃 id값이랑 연결
        back = (ImageButton)findViewById(R.id.back);
        gname = (TextView)findViewById(R.id.gname);


        gname.setText(Gname); //그룹명 연결


        //커스텀 리스트뷰 시작
        recyclerView = findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference(); // DB 테이블 연결

        //값이 변경되는걸 감지하는 함수! 지금 설정한 addValueEventListener은 채팅기능처럼 데이터가 바뀔때마다 반영되도록 하는 것.
        databaseReference.child("Together_group_list").child(Gname).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    User_group User_group = snapshot.getValue(User_group.class); // 만들어뒀던 Glook_list 객체에 데이터를 담는다.
                    arrayList.add(User_group); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }



        });

        adapter = new Glook_Adapter(arrayList,Gname ,master,this);//그룹이름과 마스터정보를 넘김
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결



        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
