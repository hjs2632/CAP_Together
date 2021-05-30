package com.example.together.Group;
// 내가 가입한 그룹 보는 어댑터

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Together_CustomAdapter extends RecyclerView.Adapter<Together_CustomAdapter.CustomViewHoler> {

    private ArrayList<gmake_list> arrayList; // 넘겨받은 배열 값 받을 곳
    private Context context; // 넘겨받은 context 받을 곳
    private Intent intent; // 화면 연결 시 사용
    private FirebaseDatabase database; // 데이터베이스
    private DatabaseReference databaseReference; // 레퍼런스
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인 된 유저 정보
    String uid = user.getUid(); // 유저 값
    String uname; // 넘겨받은 닉네임 받을 곳
    Dialog DelDialog;//그룹 삭제 및 탈퇴 Dialog
    TextView dia_content; //다이얼로그 내용

    public Together_CustomAdapter(ArrayList<gmake_list> arrayList, String uname, Context context) {
        // Group에서 받아온 값 연결
        this.arrayList = arrayList;
        this.uname = uname;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.together_list_item, parent, false); // 리스트 레이아웃 적용
        CustomViewHoler holer = new CustomViewHoler(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) {
        //다이얼로그 관련 설정
        DelDialog = new Dialog(context); //context로 하니까 잘 됩니다.
        DelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 다이얼로그 타이틀 제거
        DelDialog.setContentView(R.layout.group_dialog); // id 연결
        dia_content = (TextView) DelDialog.findViewById(R.id.dia_content); // 다이얼로그 내용 연결 (삭제, 탈퇴 시 다르게 적용)

        holder.Gname.setText(arrayList.get(position).getgname()); // 리스트 속 그룹명 연결
        holder.master.setText(arrayList.get(position).getmaster()); // 본인의 마스터 값 임시로 받아옴 (추후 변경)

        String Gname = holder.Gname.getText().toString(); // 리스트 속 그룹명 도출
        String master = holder.master.getText().toString(); //리스트 속 마스터 값 도출

        // 리스트 클릭 이벤트
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                databaseReference = database.getReference(); // 레퍼런스 연동

                databaseReference.child("Together_group_list").child(Gname).child("user").child(uid).child("uname").setValue(uname); // 접속하는 그룹마다 닉네임 바뀐거 적용

                intent = new Intent(context, look_group.class); // 그룹 상세 화면으로 연결
                intent.putExtra("uname", uname); // 닉네임 전송
                intent.putExtra("Gname", Gname); // 그룹명 전송
                intent.putExtra("master", master); // 본인의 마스터 정보 전송
                context.startActivity(intent); // 그룹 상세 화면 열기
            }
        });

        // 리스트 롱 클릭 이벤트
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (master.equals("yes")) { // 그룹장일 경우 그룹 삭제 다이얼로그
                    dia_content.setText(Gname + " 그룹을\n삭제하시겠습니까?");
                    showPlanDialog(Gname, master); // 다이얼로그 열기
                } else { // 그룹원일 경우 그룹 탈퇴 다이얼로그
                    dia_content.setText(Gname + " 그룹을\n탈퇴하시겠습니까?");
                    showPlanDialog(Gname, master); // 다이얼로그 열기
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() { // 리스트 개수 세는건데 필요없어보임 (추후 수정)
        // 삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHoler extends RecyclerView.ViewHolder {
        TextView Gname;
        TextView master;

        public CustomViewHoler(@NonNull View itemView) {
            // 리스트 레이아웃 아이디 연결
            super(itemView);
            this.Gname = itemView.findViewById(R.id.Gname);
            this.master = itemView.findViewById(R.id.master);

        }
    }

    // 그룹 삭제 및 탈퇴 다이얼로그 호출 함수
    public void showPlanDialog(String gname, String master) {
        DelDialog.show(); //다이얼로그 출력
        DelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 끝부분을 둥굴게 하기 위해 투명색 지정
        Button noBtn = DelDialog.findViewById(R.id.noBtn); // 취소 버튼
        Button yesBtn = DelDialog.findViewById(R.id.yesBtn); // 저장 버튼

        // 취소 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (master.equals("yes")) { // 그룹장일 경우
                    Toast.makeText(v.getContext(), "그룹을 탈퇴하고싶다면 그룹장을 양도하세요.", Toast.LENGTH_SHORT).show();
                }
                DelDialog.dismiss();//다이얼로그 닫기
            }
        });

        //확인 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    database = FirebaseDatabase.getInstance(); // 데이터베이스 연동
                    databaseReference = database.getReference(); //레퍼런스 연동

                    // Together_group_list -> 그룹명 -> gap에서 그룹 현재 인원을 가져온다
                    databaseReference.child("Together_group_list").child(gname).child("gap").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int value = (int) snapshot.getValue(Integer.class); // 그룹 현재 인원

                            if (master.equals("yes")) { // 그룹 삭제
                                if (value == 1) { // 본인이 그룹장인데 혼자밖에 없다면 그룹 삭제 가능
                                    databaseReference.child("Together_group_list").child(gname).removeValue(); // 그룹 자체를 삭제
                                    databaseReference.child("User").child(uid).child("Group").child(gname).removeValue(); // 내가 가입한 그룹에서 해당 그룹 삭제
                                } else { // 본인을 제외하고 남은 그룹원이 있다면 삭제 불가능
                                    Toast.makeText(v.getContext(), "그룹원이 남아있어서 그룹을 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else { // 그룹 탈퇴
                                value -= 1; //인원 1명 감소 (본인 탈퇴)
                                databaseReference.child("Together_group_list").child(gname).child("gap").setValue(value); // 인원 감소 적용

                                databaseReference.child("Together_group_list").child(gname).child("user").child(uid).removeValue(); // 그룹 내에서 나의 정보를 삭제
                                databaseReference.child("User").child(uid).child("Group").child(gname).removeValue(); // 내가 가입한 그룹에서 해당 그룹 삭제
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 디비를 가져오던중 에러 발생 시
                            //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext().getContext(),"오류발생",Toast.LENGTH_SHORT).show();
                }
                DelDialog.dismiss();//다이얼로그 닫기
            }
        });

    }
}