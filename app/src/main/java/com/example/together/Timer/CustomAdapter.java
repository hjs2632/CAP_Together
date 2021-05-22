package com.example.together.Timer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.together.FdActivity;
import com.example.together.Group.Together_group_list;
import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> { //ArrayList에 있는 Dictionary 클래스 타입의 데이터를 RecyclerView에 보여주는 처리를 함.
    private ArrayList<Dictionary> mList;
    private Context mContext;
    private String Focus_Subject;
    private String Timer_Subject;
    String Key;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid(); //유저 아이디

    public class CustomViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{ //리스너 추가

        protected TextView subject;
        protected TextView page;
        protected TextView time;

        public Button btn_focus;
        public Button btn_start;


        public CustomViewHolder(View view) {
            super(view);
            this.subject = (TextView) view.findViewById(R.id.subject_listitem);
            this.page = (TextView) view.findViewById(R.id.page_listitem);
            this.time = (TextView) view.findViewById(R.id.subject_timeView);

            this.btn_focus=(Button)view.findViewById(R.id.btn_focus);//집중모드 연결 버튼
            this.btn_start=(Button)view.findViewById(R.id.subject_start);//일반 측정 연결 버튼
            view.setOnCreateContextMenuListener(this); //리스너 등록

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가


            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {
                    case 1001: //편집 항목 선택시


                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext); //다이얼로그를 보여주기 위해 edit_box.xml 파일을 사용한다.
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.timer_edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                        final EditText editTextSubject = (EditText) view.findViewById(R.id.edittext_dialog_subject);
                        final EditText editTextPage = (EditText) view.findViewById(R.id.edittext_dialog_page);

                        editTextSubject.setText(mList.get(getAdapterPosition()).getSubject());
                        editTextPage.setText(mList.get(getAdapterPosition()).getPage());

                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Key = mList.get(getAdapterPosition()).getKey();//push로 넣었던 리스트 키값 받아오기
                                database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                                databaseReference = database.getReference(); // DB 테이블 연결
                                databaseReference.child("timer").child(uid).child("study").child(Key).child("subject").setValue(editTextSubject.getText().toString());
                                databaseReference.child("timer").child(uid).child("study").child(Key).child("page").setValue(editTextPage.getText().toString());

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        break;

                    case 1002:
                        Key = mList.get(getAdapterPosition()).getKey();//push로 넣었던 리스트 키값 받아오기
                        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                        databaseReference = database.getReference(); // DB 테이블 연결
                        databaseReference.child("timer").child(uid).child("study").child(Key).removeValue();
                        break;

                }
                return true;
            }
        };
    }



    public CustomAdapter(Context context, ArrayList<Dictionary> list) {
        mList = list;
        mContext = context;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.timer_item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.subject.setText(mList.get(position).getSubject());
        viewholder.page.setText(mList.get(position).getPage());
        viewholder.time.setText(mList.get(position).getTime());
        String time = viewholder.time.getText().toString();

        //시간 형식으로 변환(start)
        int t_time=Integer.valueOf(time);
        int min=t_time/600;
        int hour=t_time/36000;
        int sec=(t_time/10)-(min*60)-(hour*3600);
        String newstr=String.format("%02d:%02d:%02d",hour,min,sec);
        viewholder.time.setText(newstr);
        //시간 형식으로 변환(end)

        Key = mList.get(position).getKey();//push로 넣었던 리스트 키값 받아오기

        //타이머 연결(start)
        viewholder.btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Key = mList.get(position).getKey();//push로 넣었던 리스트 키값 받아오기
                Timer_Subject=mList.get(position).getSubject();
                Intent intent = new Intent(mContext.getApplicationContext(),Study_Timer.class); //인텐트
                intent = intent.putExtra("Subject",Timer_Subject);
                intent = intent.putExtra("Key",Key);
                intent = intent.putExtra("time",time);
                mContext.startActivity(intent); //액티비티 열기

            }
        });
        //타이머 연결(end)

        //집중모드 연결(start)
        viewholder.btn_focus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Key = mList.get(position).getKey();//push로 넣었던 리스트 키값 받아오기
                Focus_Subject=mList.get(position).getSubject();
                Intent intent = new Intent(mContext.getApplicationContext(), FdActivity.class); //인텐트
                intent = intent.putExtra("Subject",Focus_Subject);
                intent = intent.putExtra("Key",Key);
                intent = intent.putExtra("time",time);
                mContext.startActivity(intent); //액티비티 열기

            }

        });

        //집중모드 연결 (end)
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

