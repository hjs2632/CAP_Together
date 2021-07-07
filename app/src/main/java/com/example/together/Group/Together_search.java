package com.example.together.Group;
//검색 화면 부분이다


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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


public class Together_search extends AppCompatActivity {// 히히

    ImageButton back_btn; // 이미지버튼 변수 생성
    EditText search_edit;// 검색 에딧텍스트

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Together_group_list> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid(); // 유저 아이디
    String uname;
    String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.together_search);

        search_edit = findViewById(R.id.search_edit); // 에딧 아이디 연결

        //커스텀 리스트뷰 시작
        recyclerView = findViewById(R.id.recyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        //에딧 텍스트 변화 감지
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchText = search_edit.getText().toString(); // 검색창에 입력된 값

                // 검색 값의 그룹을 찾기
                databaseReference.child("Together_group_list").orderByKey().equalTo(searchText).addValueEventListener(new ValueEventListener() {
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

                // 사용자 닉네임 추출
                databaseReference.child("User").child(uid).child("username").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        uname = value;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });

                adapter = new Search_Adapter(arrayList, uname, Together_search.this); // 추출한 닉네임과 리스트를 어댑터로 넘김
                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

            }
        });


        //뒤로가기 버튼 변수 연결
        back_btn = (ImageButton) findViewById(R.id.back_btn);

        //뒤로가기 버튼 누르면 화면을 닫음
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}

