package com.example.together.Group;
//그룹 만들기 화면

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class make_group extends AppCompatActivity {

    ImageButton back; //이미지버튼 변수 선언
    EditText Gname_edit, Gintro_edit; //그룹 이름, 그룹 설명, 목표시간 변수 선언
    TextView Gcate_tv,title_tv, goaltime_tv, gmp_tv;
    Button addGroup_btn; //그룹 추가하기 버튼
    Intent intent;
    String Gname="",gcate,gintro,goaltime="",announce="공지사항이 아직 없습니다.";
    int gmp=0;
    LinearLayout Linear1;


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid(); //유저 아이디
    String uname;
    String master = "yes";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_group);

        Gname_edit = (EditText)findViewById(R.id.Gname_edit); //그룹명 에딧
        Gintro_edit = (EditText)findViewById(R.id.Gintro_edit); //그룹 설명 에딧
        goaltime_tv = (TextView) findViewById(R.id.goaltime_tv); //목표시간
        addGroup_btn = (Button)findViewById(R.id.addGroup_btn); //그룹 추가 버튼
        back = (ImageButton)findViewById(R.id.backmain1); //뒤로가기 버튼 연결
        Gcate_tv =(TextView)findViewById(R.id.Gcate_tv);//카테고리 선택
        title_tv = (TextView)findViewById(R.id.title_tv);//타이틀 이름 변경
        gmp_tv = (TextView)findViewById(R.id.gmp_tv);
        Linear1 = (LinearLayout)findViewById(R.id.Linear1);

        intent = getIntent();
        Gname = intent.getStringExtra("Gname");


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결


        databaseReference.child("User").child(uid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                uname= value;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }

        });

        if(Gname!=null){//그룹정보 수정이라면

            title_tv.setText("그룹 정보 수정");
            Linear1.setBackgroundColor(ContextCompat.getColor(this,R.color.Gray));
            Gname_edit.setBackgroundColor(ContextCompat.getColor(this,R.color.Gray));
            Gname_edit.setText(Gname);
            Gname_edit.setFocusable(false);//포커싱과
            Gname_edit.setClickable(false);//터치가 안되도록!
            addGroup_btn.setText("수정하기");

            databaseReference.child("Together_group_list").child(Gname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Together_group_list group = dataSnapshot.getValue(Together_group_list.class);
                gintro = group.getGintro();
                goaltime = group.getGoaltime();
                gcate = group.getGcate();
                gmp = group.getgmp();
                goaltime_tv.setText(goaltime_tv.getText().toString()+goaltime+" 시간"); //목표시간
                Gintro_edit.setText(Gintro_edit.getText().toString()+gintro); //그룹소개
                Gcate_tv.setText(Gcate_tv.getText().toString()+gcate); //그룹소개
                gmp_tv.setText(gmp_tv.getText().toString()+gmp+" 명");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }

        });

            addGroup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fixGroup(Gname_edit.getText().toString(),Gintro_edit.getText().toString(),Gcate_tv.getText().toString(), goaltime, gmp);
                }
            });

        }

        else{
            //그룹 추가 버튼 누르면 그룹 생성 후 해당 레이아웃 종료
            addGroup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference.child("Together_group_list").child(Gname_edit.getText().toString()).child("gname").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //String key=snapshot.getKey();
                            String value = snapshot.getValue(String.class);

                            if(value!=null){
                                Toast.makeText(getApplicationContext(),"이미 존재하는 그룹명입니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력
                            }
                            else{

                                addGroup(Gname_edit.getText().toString(),Gintro_edit.getText().toString(),Gcate_tv.getText().toString(), goaltime, gmp);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 디비를 가져오던중 에러 발생 시
                            //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                        }



                    });
                }
            });
        }

        //카테고리 누르면 팝업메뉴 나오도록함
        Gcate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup= new PopupMenu(make_group.this, view);

                popup.getMenuInflater().inflate(R.menu.gcate_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m1:
                                Gcate_tv.setText("자체 스터디");
                                break;
                            case R.id.m2:
                                Gcate_tv.setText("초등학교");
                                break;
                            case R.id.m3:
                                Gcate_tv.setText("중학교");
                                break;
                            case R.id.m4:
                                Gcate_tv.setText("고등학교");
                                break;
                            case R.id.m5:
                                Gcate_tv.setText("대학교");
                                break;
                            case R.id.m6:
                                Gcate_tv.setText("입시");
                                break;
                            case R.id.m7:
                                Gcate_tv.setText("어학");
                                break;
                            case R.id.m8:
                                Gcate_tv.setText("자격증");
                                break;
                            case R.id.m9:
                                Gcate_tv.setText("공무원");
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

        goaltime_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup= new PopupMenu(make_group.this, view);

                popup.getMenuInflater().inflate(R.menu.group_goaltime_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m1:
                                goaltime_tv.setText("1 시간");
                                goaltime = "1";
                                break;
                            case R.id.m2:
                                goaltime_tv.setText("2 시간");
                                goaltime = "2";
                                break;
                            case R.id.m3:
                                goaltime_tv.setText("3 시간");
                                goaltime = "3";
                                break;
                            case R.id.m4:
                                goaltime_tv.setText("4 시간");
                                goaltime = "4";
                                break;
                            case R.id.m5:
                                goaltime_tv.setText("5 시간");
                                goaltime = "5";
                                break;
                            case R.id.m6:
                                goaltime_tv.setText("6 시간");
                                goaltime = "6";
                                break;
                            case R.id.m7:
                                goaltime_tv.setText("7 시간");
                                goaltime = "7";
                                break;
                            case R.id.m8:
                                goaltime_tv.setText("8 시간");
                                goaltime = "8";
                                break;
                            case R.id.m9:
                                goaltime_tv.setText("9 시간");
                                goaltime = "9";
                                break;
                            case R.id.m10:
                                goaltime_tv.setText("10 시간");
                                goaltime = "10";
                                break;
                            case R.id.m11:
                                goaltime_tv.setText("11 시간");
                                goaltime = "11";
                                break;
                            case R.id.m12:
                                goaltime_tv.setText("12 시간");
                                goaltime = "12";
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



        gmp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup= new PopupMenu(make_group.this, view);

                popup.getMenuInflater().inflate(R.menu.gmax_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m1:
                                gmp_tv.setText("5 명");
                                gmp = 5;
                                break;
                            case R.id.m2:
                                gmp_tv.setText("10 명");
                                gmp = 10;
                                break;
                            case R.id.m3:
                                gmp_tv.setText("15 명");
                                gmp = 15;
                                break;
                            case R.id.m4:
                                gmp_tv.setText("20 명");
                                gmp = 20;
                                break;
                            case R.id.m5:
                                gmp_tv.setText("25 명");
                                gmp = 25;
                                break;
                            case R.id.m6:
                                gmp_tv.setText("30 명");
                                gmp = 30;
                                break;
                            case R.id.m7:
                                gmp_tv.setText("35 명");
                                gmp = 35;
                                break;
                            case R.id.m8:
                                gmp_tv.setText("40 명");
                                gmp = 40;
                                break;
                            case R.id.m9:
                                gmp_tv.setText("45 명");
                                gmp = 45;
                                break;
                            case R.id.m10:
                                gmp_tv.setText("50 명");
                                gmp = 50;
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






        //뒤로가기 버튼 (레이아웃 종료)
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void addGroup(String Gname, String Gintro, String Gcate, String Goaltime, int gmp) {

        if(Gname.equals("")){
            Toast.makeText(getApplicationContext(),"그룹명을 입력하세요.",Toast.LENGTH_SHORT).show();
        }
        else if(Gintro.equals("")){
            Toast.makeText(getApplicationContext(),"그룹 소개를 입력하세요.",Toast.LENGTH_SHORT).show();
        }
        else if(Gcate.equals("")){
            Toast.makeText(getApplicationContext(),"카테고리를 선택하세요.",Toast.LENGTH_SHORT).show();
        }
        else if(Goaltime.equals("")){
            Toast.makeText(getApplicationContext(),"목표시간을 선택하세요.",Toast.LENGTH_SHORT).show();
        }
        else if(gmp==0){
            Toast.makeText(getApplicationContext(),"최대인원을 선택하세요.",Toast.LENGTH_SHORT).show();
        }
        else {
            int GAP = 1; //그룹 전체 인원은 방금 만든 본인이 한명 있으니까 1
            //int Goaltime_n = Integer.valueOf(goaltime).intValue();//문자를 숫자로 변환

            //그룹 생성
            Together_group_list Group = new Together_group_list(Gname, Gintro, Gcate, gmp, GAP, Goaltime, uid, announce);
            databaseReference.child("Together_group_list").child(Gname).setValue(Group);

            //그룹 마스터도 그룹에 포함
            User_group user = new User_group(uid, uname, master);
            databaseReference.child("Together_group_list").child(Gname).child("user").child(uid).setValue(user);
            //내 그룹 보기 하려고 만든거
            gmake_list gmake_list = new gmake_list(Gname, master);
            databaseReference.child("User").child(uid).child("Group").child(Gname).setValue(gmake_list);

            finish();
        }
    }

    //그룹 수정
    public void fixGroup(String Gname, String Gintro, String Gcate, String Goaltime, int gmp) {

        if(Gintro.equals("")){
            Toast.makeText(getApplicationContext(),"그룹 소개를 입력하세요.",Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.child("Together_group_list").child(Gname).child("gcate").setValue(Gcate);//카테고리 수정 반영
            databaseReference.child("Together_group_list").child(Gname).child("gintro").setValue(Gintro);//그룹소개 수정 반영
            databaseReference.child("Together_group_list").child(Gname).child("goaltime").setValue(Goaltime);//목표시간 수정 반영
            databaseReference.child("Together_group_list").child(Gname).child("gmp").setValue(gmp);//최대인원
            finish();
        }

    }

}
