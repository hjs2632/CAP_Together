package com.example.together.Calendar;


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
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    TextView Plan;//일정을 표시할 TextView
    ImageButton PlanAddButton;//일정 기록을 위해 Dialog 호출 버튼
    ImageButton RemoveButton;//일정 기록 삭제를 위해 삭제 버튼
    Dialog PlanDialog;//일정 기록을 위한 Dialog

    /*로그인 기능 추가시 주석 제거*/
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    int checkYear;
    int checkMonth;
    int checkDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_note);


        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();


        database=FirebaseDatabase.getInstance();

        //위젯 관련 설정
        Plan=(TextView)findViewById(R.id.Plan);
        CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView);
        List<EventDay> events=new ArrayList<>();

        //다이얼로그 관련 설정
        PlanDialog=new Dialog(Calendar_note.this);
        PlanDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        PlanDialog.setContentView(R.layout.plan_dialog);
        PlanAddButton=(ImageButton)findViewById(R.id.plan_add_btn);//일정 추가 버튼
        RemoveButton=(ImageButton)findViewById(R.id.remove_btn);//일정 삭제 버튼

        //오늘 날짜를 받아오는 부분
        Calendar today= Calendar.getInstance();
        int todayYear=today.get(Calendar.YEAR);
        int todayMonth=today.get(Calendar.MONTH);
        int todayDay=today.get(Calendar.DAY_OF_MONTH);

        //현재 선택(터치)한 날짜 받아오는 부분
        checkYear=todayYear;
        checkMonth=todayMonth;
        checkDay=todayDay;

        //실행시 일정이 있으면 아이콘으로 표시
        database.getReference().child("calendar").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            //onDataChange() 메서드를 사용하여 이벤트 발생 시점에 특정 경로에 있던 콘텐츠의 정적 스냅샷을 읽음(리스너가 연결될 때 한번 트리거된 후 하위 요소를 포함한 데이터가 변경될때 마다 다시 트리거)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    int[] date=splitDate(key);
                    Calendar event_calendar=Calendar.getInstance();
                    event_calendar.set(date[0], date[1], date[2]);
                    EventDay event=new EventDay(event_calendar, R.drawable.ic_flag);//아이콘 지정
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
                //선택한 날짜에 이미 일정이 있는지 없는지 확인
                checkedDay(clickedDayCalendar.get(Calendar.YEAR),
                        clickedDayCalendar.get(Calendar.MONTH),
                        clickedDayCalendar.get(Calendar.DATE));
                //선택한 날짜 변경
                checkYear = clickedDayCalendar.get(Calendar.YEAR);
                checkMonth = clickedDayCalendar.get(Calendar.MONTH);
                checkDay = clickedDayCalendar.get(Calendar.DATE);
                Plan.setText("");
            }
        });

        //일정 추가 버튼을 누르면 실행되는 리스너
        PlanAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //일정 추가 다이얼로그 호출
                showPlanDialog();
            }
        });

        //일정 삭제 버튼을 누르면 실행되는 리스너
        RemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("calendar").child(user.getUid()).child(checkYear + "-" + checkMonth + "-" + checkDay);
                    databaseReference.removeValue();//선택한 날짜의 데이터를 삭제
                    Plan.setText("");
                    Toast.makeText(getApplicationContext(),"일정 삭제 완료",Toast.LENGTH_SHORT).show();
                    database=FirebaseDatabase.getInstance();

                    //임시로 아이콘을 지우기 위해 자기 자신을 다시 호출하는 방식을 사용했지만, 좋지 않아 보임
                    //혹시 자기 코드에 문제가 없는데 오류가 뜬다면 아래 3줄을 삭제하고 테스트 진행 그래도 문제 생기면 다시 복구
                    finish();//인텐트 종료
                    Intent intent = new Intent(getApplicationContext(), Calendar_note.class); //인텐트
                    startActivity(intent); //액티비티 열기

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
            }
        });
    }

    //일정을 입력할 수 있는 다이얼로그 호출(다이얼로그 관련 코드)
    public void showPlanDialog(){
        PlanDialog.show(); //다이얼로그 출력
        PlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//끝부분을 둥굴게 하기 위해 투명색 지정
        Button noBtn= PlanDialog.findViewById(R.id.noBtn);//취소 버튼
        Button yesBtn=PlanDialog.findViewById(R.id.yesBtn);//저장 버튼
        EditText test_Edit=PlanDialog.findViewById(R.id.test_Edit);//일정을 입력할 수 있는 EditText
        test_Edit.setText("");

        //취소 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDialog.dismiss();//다이얼로그 닫기
            }
        });

        //저장 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference=database.getReference().child("calendar").child(user.getUid()).child(checkYear + "-" + checkMonth + "-" + checkDay);
                    String content=test_Edit.getText().toString();
                    databaseReference.setValue(content);//선택한 날짜에 일정 저장
                    Toast.makeText(getApplicationContext(),"일정 저장 완료",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }catch(Exception e){//예외
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
                PlanDialog.dismiss();//다이얼로그 닫기
            }
        });

        }

    //일정 DB 읽기
    private void checkedDay(int year, int monthOfYear, int dayOfMonth){
        //DB 레퍼런스 경로 설정
        databaseReference=database.getReference().child("calendar").child(user.getUid()).child(year+"-"+monthOfYear+"-"+dayOfMonth);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue(String.class);
                if (str == null) {
                    //데이터가 없으면 일정이 없음
                    Plan.setText("");
                }else{
                    //저장된 문자열 받아오기
                    Plan.setText("- "+str);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });


    }


    private int[] splitDate(String date){
        String[] splitText=date.split("-");
        //문자열을 int로 변환
        int[] result_date={Integer.parseInt(splitText[0]),Integer.parseInt(splitText[1]),Integer.parseInt(splitText[2])};
        return result_date;
    }
}