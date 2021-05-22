package com.example.together.Timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Study_Timer extends AppCompatActivity {


    ImageButton backbtn;
    Button startbtn;
    TextView Timer_view;
    TextView timer_subject;
    TextView total_counting;

    private  static final int INIT=0; //처음
    private static final int RUN=1; //실행중
    private static final int PAUSE=2; //정지

    //파이어베이스
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    public static int status = INIT; //현재 상태를 저장할 변수 초기화
    long mBaseTime,mPauseTime; //타이머 시간 값을 저장할 변수
    int min,hour,sec,detect;
    public int f_sec=0;
    public int f_min=0;
    public int f_hour=0;
    public int first_detect=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_check);

        backbtn = (ImageButton) findViewById(R.id.back_btn);
        startbtn = (Button) findViewById(R.id.start_btn);
        Timer_view = (TextView) findViewById(R.id.timer_view);
        timer_subject = (TextView) findViewById(R.id.timer_subject);

        Intent intent =getIntent(); //subject값 받아오는 부분
        String Subject=intent.getExtras().getString("Subject");
        String Key=intent.getExtras().getString("Key");
        String time = intent.getExtras().getString("time"); //전에 공부했던 값 받아온거
        timer_subject.setText(Subject);

        backbtn.setOnClickListener(onClickListener);
        startbtn.setOnClickListener(onClickListener);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();

        //DB 레퍼런스 경로 설정
        databaseReference=database.getReference().child("timer").child(user.getUid()).child("study").child(Key).child("time");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String str = dataSnapshot.getValue(String.class);
                if (str == null) {
                    //데이터가 없으면 공부기록이 없음
                    total_counting.setText("");
                }else{
                    //저장된 문자열 받아오기
                    first_detect=Integer.valueOf(str);
                    f_min=first_detect/600;
                    f_hour=first_detect/36000;
                    f_sec=(first_detect/10)-(f_min*60)-(f_hour*3600);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });



        //뒤로가기 버튼 누르면 화면을 닫음
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //임시 데이터베이스 전송(시간이 문자열로 가기 때문에 가공 절차가 필요)
                Toast.makeText(getApplicationContext(),Subject+" 과목을"+String.valueOf(hour)+"시 "+String.valueOf(min)+"분 "+String.valueOf(sec)+"초 공부시간이 저장되었습니다.",Toast.LENGTH_SHORT).show();
                databaseReference=database.getReference().child("timer").child(user.getUid()).child("study").child(Key).child("time");
                detect=first_detect+detect;
                String content=String.valueOf(detect);
                databaseReference.setValue(content);//선택한 날짜에 일정 저장
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            switch(view.getId()){
                case R.id.start_btn:
                    startButton();
                    break;
                //case R.id.back_btn:
                //  backButton();
                //break;
            }
        }
    };

    private void startButton(){
        switch (status){
            case INIT: //초기 상태일 경우
                mBaseTime = SystemClock.elapsedRealtime();//애플리케이션이 실행되고 난 뒤 실제로 경과된 시간
                mTimer.sendEmptyMessage(0); //핸들러 실행
                startbtn.setText("STOP"); //정지로 바뀜

                status = RUN; //RUN으로 상태변환
                break;

            case RUN:
                mTimer.removeMessages(0); //핸들러 정지
                mPauseTime=SystemClock.elapsedRealtime();
                startbtn.setText("START");

                status=PAUSE;
                break;

            case PAUSE:
                mTimer.sendEmptyMessage(0);
                startbtn.setText("STOP");
                status = RUN;
        }



    }

       // private void backButton() {

        //switch(status){
        //    case INIT:

       // }


     //}


    Handler mTimer = new Handler(){
        public void handleMessage(Message msg){
            Timer_view.setText(getTimeOut());
            mTimer.sendEmptyMessage(0);//비어있는 메세지를 핸들러에 전달
        }
    };

    String getTimeOut(){ //현재시간을 계속 구하여 출력하는 메소드
        //long now = SystemClock.elapsedRealtime();
        //long outTime = now-mBaseTime;

        //long m = outTime/1000/60;
        //long s = (outTime/1000)%60;
        //long ms = (outTime % 1000)/10;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        detect+=1;
        min=detect/600;
        hour=detect/36000;
        sec=(detect/10)-(min*60)-(hour*3600);
        String Out_Time=String.format("%02d:%02d:%02d",hour,min,sec);
        //String Out_Time=String.valueOf(hour)+": "+String.valueOf(min)+": "+String.valueOf(sec);
        return Out_Time;

    }



}