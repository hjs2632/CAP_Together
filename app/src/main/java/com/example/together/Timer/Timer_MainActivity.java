package com.example.together.Timer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//추가된 부분
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.together.FdActivity;
import com.example.together.Group.Together_CustomAdapter;
import com.example.together.Group.gmake_list;
import com.example.together.R;
//추가된 부분
import com.example.together.Calendar.Calendar_note;
import com.example.together.User_Name;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Timer_MainActivity extends Fragment {

    ImageButton calendar_btn;//일정 이동 버튼
    ImageButton focus_btn;//집중모드 이동 버튼




    //파이어베이스
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid(); //유저 아이디


    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private int count = -1;

    public String uname;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.timer_activity_main, container, false);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        //닉네임 중복 확인
        databaseReference.child("User").child(user.getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(value==null){
                    Intent intent = new Intent(getActivity().getApplicationContext(), User_Name.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }

        }
        );



        //오늘 날짜를 받아오는 부분
        Calendar today= Calendar.getInstance();
        int todayYear=today.get(Calendar.YEAR);
        int todayMonth=today.get(Calendar.MONTH);
        int todayDay=today.get(Calendar.DAY_OF_MONTH);




        calendar_btn = (ImageButton) v.findViewById(R.id.calendar_btn); //일정 이동 버튼



        calendar_btn.setOnClickListener(new View.OnClickListener() { // 일정으로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Calendar_note.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });




        //리사이클러뷰 부분
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_main_list);
        mArrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트 (어댑터쪽으로)

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //값이 변경되는걸 감지하는 함수
        databaseReference.child("timer").child(uid).child("study").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                mArrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    Dictionary dictionary = snapshot.getValue(Dictionary.class);
                    String StudyKey = snapshot.getKey();//키값 받아오기(getKey()함수 사용)
                    databaseReference.child("timer").child(uid).child("study").child(StudyKey).child("key").setValue(StudyKey);//push로 넣은 키값 직접 넣기
                    mArrayList.add(dictionary); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                mAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        mAdapter = new CustomAdapter(getContext(),mArrayList);
        mRecyclerView.setAdapter(mAdapter); // 리사이클러뷰에 어댑터 연결



        Button buttonInsert = (Button) v.findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.timer_edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText editTextSubject = (EditText) view.findViewById(R.id.edittext_dialog_subject);
                final EditText editTextPage = (EditText) view.findViewById(R.id.edittext_dialog_page);

                ButtonSubmit.setText("삽입");


                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strSubject = editTextSubject.getText().toString();
                        String strPage = editTextPage.getText().toString();

                        int time =0;//누적 공부 시간은 0으로 생성
                        Dictionary dict = new Dictionary(strSubject, strPage, time);//삽입할 리스트 요소

                        databaseReference.child("timer").child(uid).child("study").push().setValue(dict);//push로 저장

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
return v;
    }}


