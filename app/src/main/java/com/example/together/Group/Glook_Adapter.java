package com.example.together.Group;
// 그룹 누르면 그룹원들이랑 공부 시간들 볼수있는 커스텀 리스트뷰 어댑터 부분

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.util.Calendar;

public class Glook_Adapter extends RecyclerView.Adapter<Glook_Adapter.CustomViewHoler> {

    private ArrayList<User_group> arrayList;
    private String gname, master,uname;
    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String x;//다이얼로그에서 위임인지 추방인지 확인하는 변수

    Dialog Gdialog; // 그룹원 추방 또는 그룹장 양도를 위한 Dialog
    TextView dia_content; // 다이얼로그 내용

    //오늘 날짜를 받아오는 부분
    Calendar today= Calendar.getInstance();
    int todayYear=today.get(Calendar.YEAR);
    int todayMonth=today.get(Calendar.MONTH);
    int todayDay=today.get(Calendar.DAY_OF_MONTH);

    public Glook_Adapter(ArrayList<User_group> arrayList,String gname ,String master,String uname,Context context) {
        this.arrayList = arrayList;
        this.gname = gname;
        this.master = master;
        this.uname = uname;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.glook_list, parent, false);
        CustomViewHoler holer = new CustomViewHoler(view);
        return holer;

    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) {
        //다이얼로그 관련 설정
        Gdialog=new Dialog(context);
        Gdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        Gdialog.setContentView(R.layout.group_dialog);
        dia_content = (TextView)Gdialog.findViewById(R.id.dia_content);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference();

        holder.uname.setText(arrayList.get(position).getuname());
        holder.userid.setText(arrayList.get(position).getuid());
        String Uname = holder.uname.getText().toString(); // 그룹원 이름
        String userid = holder.userid.getText().toString(); // 그룹원 아이디

        // 오늘 날짜의 공부시간합계 가져오기
        databaseReference.child("Together_group_list").child(gname).child("user").child(userid).child("studytime").child(todayYear + "-" + todayMonth + "-" + todayDay).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if(value == null){
                    String n_time=String.format("%02d:%02d:%02d",0,0,0);
                    holder.studytime.setText(n_time);
                }else{
                    // 공부한 시간 불러오기
                    int total_time=Integer.parseInt(value);
                    int t_min=total_time/600;
                    int t_hour=total_time/36000;
                    int t_sec=(total_time/10)-(t_min*60)-(t_hour*3600);
                    String n_time=String.format("%02d:%02d:%02d",t_hour,t_min,t_sec);
                    holder.studytime.setText(n_time);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        holder.itemView.setTag(position);

        // 본인은 그룹장이면서 롱클릭할 그룹원이 자신이 아닐 경우
        if(master.equals("yes") && !Uname.equals(uname)) {
            // 롱 클릭을 하여 그룹장 위임 또는 추방을 선택할 수 있다
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    PopupMenu popup = new PopupMenu(context.getApplicationContext(), view); // v는 클릭된 뷰를 의미

                    popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.m1: // 그룹장 위임
                                    dia_content.setText(Uname + " 그룹원에게\n그룹장을 위임하시겠습니까?");
                                    x = "위임";
                                    showGdialog(userid,x);
                                    break;
                                case R.id.m2: // 추방
                                    dia_content.setText(Uname + " 그룹원을\n추방하시겠습니까?");
                                    x = "추방";
                                    showGdialog(userid,x);
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHoler extends RecyclerView.ViewHolder {
        TextView uname;
        TextView userid;
        TextView studytime;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.uname = itemView.findViewById(R.id.username);
            this.userid = itemView.findViewById(R.id.userid);
            this.studytime = itemView.findViewById(R.id.studytime);
        }
    }

    // 그룹원 추방 또는 그룹장 양도 다이얼로그 호출(다이얼로그 관련 코드)
    public void showGdialog(String userid,String x){
        Gdialog.show(); //다이얼로그 출력
        Gdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 끝부분을 둥굴게 하기 위해 투명색 지정
        Button noBtn= Gdialog.findViewById(R.id.noBtn); // 취소 버튼
        Button yesBtn=Gdialog.findViewById(R.id.yesBtn); // 확인 버튼
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference();

        // 취소 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gdialog.dismiss();//다이얼로그 닫기
            }
        });

        // 확인 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(x.equals("추방")) {
                    try { // 그룹원 추방
                        databaseReference.child("Together_group_list").child(gname).child("gap").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int value = (int)snapshot.getValue(Integer.class);
                                value -=1; // 그룹원 인원을 1만큼 줄인다
                                databaseReference.child("Together_group_list").child(gname).child("gap").setValue(value);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // 디비를 가져오던중 에러 발생 시
                                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                            }
                        });
                        // 그룹원을 추방시키고 해당 그룹원의 내그룹 데이터베이스에서 그룹을 제거한다
                        databaseReference.child("Together_group_list").child(gname).child("user").child(userid).removeValue();
                        databaseReference.child("User").child(userid).child("Group").child(gname).removeValue();

                        // 그룹 상세보기 화면 새로고침
                        Intent intent = ((Activity)context).getIntent();
                        ((Activity)context).finish(); //현재 액티비티 종료 실시
                        ((Activity)context).overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                        ((Activity)context).startActivity(intent); //현재 액티비티 재실행 실시
                        ((Activity)context).overridePendingTransition(0, 0); //인텐트 애니메이션 없애기

                    } catch (Exception e) {//예외
                        e.printStackTrace();
                    }
                    Gdialog.dismiss(); // 다이얼로그 닫기
                }
                else{ // 그룹장 위임
                    try {
                        // 상대에게 위임부분
                        databaseReference.child("Together_group_list").child(gname).child("user").child(userid).child("master").setValue("yes");
                        databaseReference.child("User").child(userid).child("Group").child(gname).child("master").setValue("yes");
                        // 본인의 권한을 없애는 부분
                        databaseReference.child("Together_group_list").child(gname).child("user").child(uid).child("master").setValue("no");
                        databaseReference.child("User").child(uid).child("Group").child(gname).child("master").setValue("no");
                        // 그룹 안에서의 마스터를 변경
                        databaseReference.child("Together_group_list").child(gname).child("master").setValue(userid);

                    } catch (Exception e) { // 예외
                        e.printStackTrace();
                    }
                    Gdialog.dismiss(); // 다이얼로그 닫기
                }
            }
        });
    }
}