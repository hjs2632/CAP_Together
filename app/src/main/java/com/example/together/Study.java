package com.example.together;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.together.Calendar.Calendar_note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Study extends Fragment {

    ImageButton calendar_btn;//일정 이동 버튼
    ImageButton focus_btn;//집중모드 이동 버튼

    EditText test_id;
    Button test_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.study, container, false);


        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();


        database=FirebaseDatabase.getInstance();

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
                    Toast.makeText(getActivity().getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
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

        return v;
    }
}