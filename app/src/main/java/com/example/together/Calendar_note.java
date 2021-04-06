package com.example.together;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* 참고 자료:https://github.com/JJinTae/MakeYouStudy */
public class Calendar_note extends AppCompatActivity {
    TextView Plan;

    ImageButton PlanAddButton;
    ImageButton RemoveButton;
    Dialog PlanDialog;

    //private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //private FirebaseUser user;

    int checkYear;
    int checkMonth;
    int checkDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_note);

        /*로그인 기능 추가후 주석 제거
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
         */

        database=FirebaseDatabase.getInstance();

        //위젯 관련
        Plan=(TextView)findViewById(R.id.Plan);
        CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView);
        List<EventDay> events=new ArrayList<>();

        PlanDialog=new Dialog(Calendar_note.this);
        PlanDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PlanDialog.setContentView(R.layout.plan_dialog);
        PlanAddButton=(ImageButton)findViewById(R.id.plan_add_btn);
        RemoveButton=(ImageButton)findViewById(R.id.remove_btn);

        //오늘의 날짜
        Calendar today= Calendar.getInstance();
        int todayYear=today.get(Calendar.YEAR);
        int todayMonth=today.get(Calendar.MONTH);
        int todayDay=today.get(Calendar.DAY_OF_MONTH);

        //현재 선택한 날짜
        checkYear=todayYear;
        checkMonth=todayMonth;
        checkDay=todayDay;

        //실행시 일정이 있으면 표시
        database.getReference().child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    int[] date=splitDate(key);
                    Calendar event_calendar=Calendar.getInstance();
                    event_calendar.set(date[0], date[1], date[2]);
                    EventDay event=new EventDay(event_calendar, R.drawable.test_ic);
                    events.add(event);
                }
                calendarView.setEvents(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //시작할 때 오늘 날짜 일정 읽기
        checkedDay(todayYear,todayMonth,todayDay);

        // 선택 날짜가 변경될 때 호출되는 리스너
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                //이미 선택한 날짜에 일기가 있는지 없는지 체크
                checkedDay(clickedDayCalendar.get(Calendar.YEAR),
                        clickedDayCalendar.get(Calendar.MONTH),
                        clickedDayCalendar.get(Calendar.DATE));
                //체크한 날짜 변경
                checkYear = clickedDayCalendar.get(Calendar.YEAR);
                checkMonth = clickedDayCalendar.get(Calendar.MONTH);
                checkDay = clickedDayCalendar.get(Calendar.DATE);
                Plan.setText("");
            }
        });


        PlanAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlanDialog();

            }
        });

        RemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("calendar").child(checkYear + "-" + checkMonth + "-" + checkDay);
                    databaseReference.removeValue();
                    Plan.setText("");

                    Toast.makeText(getApplicationContext(),"일정 삭제 완료",Toast.LENGTH_SHORT).show();

                    database=FirebaseDatabase.getInstance();
                    //임시로 아이콘을 지우기 위해 자기 자신을 다시 호출하는 방식을 사용했지만, 좋지 않아 보임
                    Intent intent = new Intent(getApplicationContext(), Calendar_note.class); //인텐트
                    startActivity(intent); //액티비티 열기

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showPlanDialog(){
        PlanDialog.show();
        PlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button noBtn= PlanDialog.findViewById(R.id.noBtn);
        Button yesBtn=PlanDialog.findViewById(R.id.yesBtn);
        EditText test_Edit=PlanDialog.findViewById(R.id.test_Edit);
        test_Edit.setText("");
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("calendar").child(checkYear + "-" + checkMonth + "-" + checkDay);
                    String content=test_Edit.getText().toString();
                    databaseReference.setValue(content);
                    Toast.makeText(getApplicationContext(),"일정 저장 완료",Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();
                }
                PlanDialog.dismiss();
            }
        });

        }




    //일정 DB 읽기
    private void checkedDay(int year, int monthOfYear, int dayOfMonth){
        //DB 레퍼런스 경로 설정
        databaseReference=database.getReference().child("calendar").child(year+"-"+monthOfYear+"-"+dayOfMonth);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue(String.class);
                if (str == null) {
                    //데이터가 없으면 일정이 없음->일정 써야함.
                    Plan.setText("");
                }else{
                    //저장된 문자 받아오기
                    Plan.setText("- "+str);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });


    }


    private int[] splitDate(String date){
        String[] splitText=date.split("-");
        int[] result_date={Integer.parseInt(splitText[0]),Integer.parseInt(splitText[1]),Integer.parseInt(splitText[2])};
        return result_date;
    }
}