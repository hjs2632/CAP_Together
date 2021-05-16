package com.example.together.Group;

import android.app.Dialog;
import android.content.Context;
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

public class chat_Adapter extends RecyclerView.Adapter<chat_Adapter.CustomViewHoler> {


    private ArrayList<chat_list> arrayList;
    private String gname, uname;
    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();




    public chat_Adapter(ArrayList<chat_list> arrayList,String gname ,Context context) {
        this.arrayList = arrayList;
        this.gname = gname;
        this.context = context;


    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false);
        CustomViewHoler holer = new CustomViewHoler(view);
        return holer;

    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) {

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference();

        holder.you_chat.setText(arrayList.get(position).getmessage());
        holder.my_chat.setText(arrayList.get(position).getmessage());
        holder.user_id.setText(arrayList.get(position).getuid());
        holder.itemView.setTag(position);
        String chat = holder.you_chat.getText().toString();
        String user_id = holder.user_id.getText().toString();

        if(user_id.equals(uid)){
            holder.you_chat.setVisibility(View.INVISIBLE);
        }
        else{
            holder.my_chat.setVisibility(View.INVISIBLE);
        }



    }


    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewHoler extends RecyclerView.ViewHolder {
        TextView you_chat;
        TextView my_chat;
        TextView user_id;


        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.you_chat = itemView.findViewById(R.id.you_chat);
            this.my_chat = itemView.findViewById(R.id.my_chat);
            this.user_id = itemView.findViewById(R.id.user_id);
        }
    }




}