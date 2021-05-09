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

public class Timer_MainActivity extends Fragment {

    EditText test_id;
    ImageButton calendar_btn;//일정 이동 버튼
    ImageButton focus_btn;//집중모드 이동 버튼
    Button test_btn;

    private Button mStartBtn, mSaveBtn;
    private TextView mTimeTextView, mSubjectTimeTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private Thread timeThread = null;
    private Boolean isRunning = true;

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

        //파이어베이스
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
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



        calendar_btn = (ImageButton) v.findViewById(R.id.calendar_btn); //일정 이동 버튼
        focus_btn=(ImageButton)v.findViewById(R.id.focus_btn); //집중모드 이동 버튼

        test_id=(EditText)v.findViewById(R.id.test_id);
        test_btn=(Button)v.findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("User").child(user.getUid()).child("username");
                    String content=test_id.getText().toString();
                    databaseReference.setValue(content);//선택한 날짜에 일정 저장
                    Toast.makeText(getActivity().getApplicationContext(),"일정 저장 완료",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }catch(Exception e){//예외
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),"오류발생", Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
            }
        });

        calendar_btn.setOnClickListener(new View.OnClickListener() { // 일정으로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Calendar_note.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });

        focus_btn.setOnClickListener(new View.OnClickListener() { // 집중모드로 이동
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), FdActivity.class); //인텐트
                startActivity(intent); //액티비티 열기

            }
        });



        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_main_list);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();


        mAdapter = new CustomAdapter(getActivity(), mArrayList);


        mRecyclerView.setAdapter(mAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


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

                        Dictionary dict = new Dictionary(strSubject, strPage);

                        mArrayList.add(0, dict); //첫 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
return v;
    }}


