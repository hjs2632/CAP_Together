package com.example.together.Group;
// 그룹 누르면 그룹원들이랑 공부 시간들 볼수있는 커스텀 리스트뷰 어댑터 부분

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//사진 관련 부분은 일단 주석처리 했습니다. 굳이 그룹에서 데이터베이스 연동할 이유 없어보여서요.
public class Glook_Adapter extends RecyclerView.Adapter<Glook_Adapter.CustomViewHoler> {


    private ArrayList<User_group> arrayList;
    private String gname, master,uname;
    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String x;//다이얼로그에서 위임인지 추방인지 확인하는 변수

    Dialog Gdialog;//그룹 가입을 위한 Dialog
    TextView dia_content; //다이얼로그 내용



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
        Gdialog=new Dialog(context); //context로 하니까 잘 됩니다.
        Gdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        Gdialog.setContentView(R.layout.group_dialog);

        dia_content = (TextView)Gdialog.findViewById(R.id.dia_content);// setContentView에 대한 고찰..

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference();

        holder.uname.setText(arrayList.get(position).getuname());
        holder.userid.setText(arrayList.get(position).getuid());
        holder.itemView.setTag(position);
        //holder.master.setText(arrayList.get(position).getmaster());
        String Uname = holder.uname.getText().toString();
        String userid = holder.userid.getText().toString();
        //String Master = holder.master.getText().toString();

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GName = holder.Gname.getText().toString(); //그룹 이름을 저 변수에 담는다!

                Intent intent = new Intent(context.getApplicationContext(), look_group.class); //그룹 상세 화면으로 연결
                intent.putExtra("Gname", GName); //그룹 이름 넘겨서 열기
                context.startActivity(intent); //액티비티 열기
                Toast.makeText(view.getContext(), GName,Toast.LENGTH_SHORT).show(); //토스트로 실험
            }
        }); */


        if(master.equals("yes") && !Uname.equals(uname)) {

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    PopupMenu popup = new PopupMenu(context.getApplicationContext(), view);//v는 클릭된 뷰를 의미

                    popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.m1://그룹장 위임
                                    dia_content.setText(Uname + " 그룹원에게\n그룹장을 위임하시겠습니까?");
                                    x = "위임";
                                    showGdialog(userid,x);
                                    break;
                                case R.id.m2://추방
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


        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.uname = itemView.findViewById(R.id.username);
            this.userid = itemView.findViewById(R.id.userid);
        }
    }

    //그룹 가입 다이얼로그 호출(다이얼로그 관련 코드)
    public void showGdialog(String userid,String x){
        Gdialog.show(); //다이얼로그 출력
        Gdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//끝부분을 둥굴게 하기 위해 투명색 지정
        Button noBtn= Gdialog.findViewById(R.id.noBtn);//취소 버튼
        Button yesBtn=Gdialog.findViewById(R.id.yesBtn);//저장 버튼
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference();

        //취소 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gdialog.dismiss();//다이얼로그 닫기
            }
        });

        //확인 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(x.equals("추방")) {
                    try {//그룹원 추방
                        databaseReference.child("Together_group_list").child(gname).child("user").child(userid).removeValue();
                        databaseReference.child("User").child(userid).child("Group").child(gname).removeValue();

                    } catch (Exception e) {//예외
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext().getContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                    }
                    Gdialog.dismiss();//다이얼로그 닫기
                }
                else{//그룹장 위임
                    try {
                        //상대에게 위임부분
                        databaseReference.child("Together_group_list").child(gname).child("user").child(userid).child("master").setValue("yes");
                        databaseReference.child("User").child(userid).child("Group").child(gname).child("master").setValue("yes");
                        //본인의 권한을 없애는 부분
                        databaseReference.child("Together_group_list").child(gname).child("user").child(uid).child("master").setValue("no");
                        databaseReference.child("User").child(uid).child("Group").child(gname).child("master").setValue("no");
                        //그룹 안에서의 마스터를 변경
                        databaseReference.child("Together_group_list").child(gname).child("master").setValue(userid);

                    } catch (Exception e) {//예외
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext().getContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                    }
                    Gdialog.dismiss();//다이얼로그 닫기
                }
            }
        });

    }






}

