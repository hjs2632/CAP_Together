package com.example.together.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chat_room extends AppCompatActivity {
    private Intent intent;
    private String Gname, uname;

    TextView gname_tv;
    ImageButton back;
    EditText chat_edit;
    Button send;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<chat_list> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid(); //유저 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        gname_tv = (TextView) findViewById(R.id.gname_tv);
        back = (ImageButton) findViewById(R.id.back);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        send = (Button) findViewById(R.id.send);

        // intent로 받아온 변수
        intent = getIntent();
        Gname = intent.getStringExtra("Gname"); // 그룹명
        uname = intent.getStringExtra("uname"); // 유저 닉네임
        gname_tv.setText(Gname); // 그룹명 연결

        // 커스텀 리스트뷰 시작
        recyclerView = findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        adapter = new chat_Adapter(arrayList, Gname, chat_room.this); // 그룹 이름을 어댑터로 넘기기
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        // 기존 채팅 데이터 불러오기
        databaseReference.child("Together_group_list").child(Gname).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    chat_list chat_list = snapshot.getValue(chat_list.class);
                    arrayList.add(chat_list); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1); // 채팅을 불러왔을때 화면을 스크롤 가장 아래쪽으로 위치시키기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        // DB에 새로운 채팅 데이터가 추가될 경우
        databaseReference.child("Together_group_list").child(Gname).child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chat_list chat_list = dataSnapshot.getValue(chat_list.class);
                arrayList.add(chat_list); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1); // 채팅을 불러올때마다 화면을 스크롤 가장 아래쪽으로 위치시키기
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // 뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
        뭘 하려고 했던걸까
        chat_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 2000);
            }
        });
         */

        // 채팅 보내기 버튼 클릭 시
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 값이 들어있는 경우만 채팅이 보내짐
                if (chat_edit.getText().toString().equals("")) {
                } else {
                    // 현재 시간 넣는 부분
                    long mNow = System.currentTimeMillis();
                    Date mReDate = new Date(mNow);
                    SimpleDateFormat mFormat = new SimpleDateFormat("aa hh:mm"); // ex) pm 7:12
                    String formatDate = mFormat.format(mReDate);

                    // DB에 사용자가 입력한 채팅 데이터 저장
                    chat_list list = new chat_list(chat_edit.getText().toString(), uname, uid, formatDate);
                    databaseReference.child("Together_group_list").child(Gname).child("chat").push().setValue(list);
                    chat_edit.setText("");
                }
            }
        });

    }
}
