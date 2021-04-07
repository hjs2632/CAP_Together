package com.example.together.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Together_Customlist extends AppCompatActivity {

    ImageButton menu_btn, search_btn; //이미지버튼 변수 생성
    LinearLayout group1; //임시로 그룹 누르면 그룹 상세화면 나오도록!

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Together_group_list> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.together_recycler);

        //변수들 레이아웃 id값이랑 연결
        menu_btn = (ImageButton) findViewById(R.id.menu_btn); //메뉴 버튼
        search_btn = (ImageButton) findViewById(R.id.search_btn); //그룹 검색 버튼

        //플로팅 버튼
        FloatingActionButton fab = findViewById(R.id.fab_btn);


        //커스텀 리스트뷰 시작
        recyclerView = findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Together_group_list"); // DB 테이블 연결

        //값이 변경되는걸 감지하는 함수! 지금 설정한 addValueEventListener은 채팅기능처럼 데이터가 바뀔때마다 반영되도록 하는 것.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    Together_group_list Together_group_list = snapshot.getValue(Together_group_list.class); // 만들어뒀던 Together_group_list 객체에 데이터를 담는다.
                    arrayList.add(Together_group_list); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }



        });

        adapter = new Together_CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결


        //그룹 생성 버튼 (플로팅 버튼)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), make_group.class); //그룹 만들기 화면으로 연결
                //intent.putExtra("User", strId); 이거는 이제 변수 넘겨줄거 있으면 쓰는거
                startActivity(intent); //액티비티 열기
            }
        });


        //검색 버튼 누르면 검색화면 나오도록 함
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Together_search.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });


    }
}
