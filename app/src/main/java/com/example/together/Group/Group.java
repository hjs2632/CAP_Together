package com.example.together.Group;
// 그룹 홈

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Group extends Fragment {

    FloatingActionButton fab; // 그룹 추가 버튼
    private RecyclerView recyclerView; // 리사이클러뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private RecyclerView.LayoutManager layoutManager; // 리사이클러뷰 레이아웃 매니저
    private ArrayList<gmake_list> arrayList; // 리사이클러뷰에 적용시킬 내용 배열
    private FirebaseDatabase database; // 파이어베이스
    private DatabaseReference databaseReference; // 레퍼런스
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인 된 유저정보
    String uid = user.getUid(); // 유저값
    String uname; // 닉네임


    public Group() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.together_recycler, container, false);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        //변수들 레이아웃 id값이랑 연결
        fab = (FloatingActionButton) v.findViewById(R.id.fab_btn);

        //커스텀 리스트뷰 시작
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트(어댑터쪽으로)

        // User -> 사용자 값 -> username로 들어가서 닉네임을 받아옴(변경된 닉네임을 적용하기 위함)
        databaseReference.child("User").child(uid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                String value = Snapshot.getValue(String.class);
                uname = value;

                // 값이 변경되는걸 감지하는 함수. 지금 설정한 addValueEventListener는 데이터가 바뀔때마다 반영되도록 하는 것
                // User -> 사용자 값 -> Group으로 들어가서 본인이 가입한 그룹 리스트를 불러옴
                databaseReference.child("User").child(uid).child("Group").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                            gmake_list gmake_list = snapshot.getValue(gmake_list.class); // gmake_list 객체에 데이터를 담는다
                            arrayList.add(gmake_list); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                        }
                        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });
                adapter = new Together_CustomAdapter(arrayList, uname, getContext()); //각 그룹에 변경된 닉네임을 적용시키기 위해 uname을 어댑터로 같이 전송
                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        // 그룹 생성 버튼 클릭 시 (플로팅 버튼)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), view); // view(플로팅 버튼)의 팝업메뉴 선언
                popup.getMenuInflater().inflate(R.menu.add_group_menu, popup.getMenu()); // 메뉴 연결

                // 팝업메뉴 클릭 이벤트
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.m1: // 그룹 생성하기
                                Intent intent = new Intent(getContext(), make_group.class); // 그룹 만들기 화면으로 연결
                                startActivity(intent); //액티비티 열기
                                break;
                            case R.id.m2: // 그룹 검색하기
                                Intent intent2 = new Intent(getContext(), Together_search.class); // 그룹 검색 화면으로 연결
                                startActivity(intent2); // 액티비티 열기
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        return v;
    }
}