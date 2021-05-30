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

public class chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<chat_list> arrayList;
    private String gname, uname;
    private Context context;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();


    public chat_Adapter(ArrayList<chat_list> arrayList, String gname, Context context) {
        this.arrayList = arrayList;
        this.gname = gname;
        this.context = context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_list2, parent, false);
                return new CustomViewHoler2(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_list, parent, false);
                return new CustomViewHoler(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list, parent, false);
        return new CustomViewHoler(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final chat_list model = arrayList.get(position);
        if (model.getuid().equals(uid)) { //내 메세지인 경우
            CustomViewHoler2 holder2 = (CustomViewHoler2) holder;
            holder2.my_chat.setText(model.getmessage());
            holder2.my_time.setText(model.gettime());
        } else {
            CustomViewHoler holder1 = (CustomViewHoler) holder;
            holder1.user_name.setText(model.getuser());
            holder1.you_chat.setText(model.getmessage());
            holder1.you_time.setText(model.gettime());
        }


    }

    @Override
    public int getItemViewType(int position) {
        chat_list chatMessage = arrayList.get(position);
        if (chatMessage.getuid().equals(uid)) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }


    //상대방 채팅일 경우
    public class CustomViewHoler extends RecyclerView.ViewHolder {
        TextView you_chat;
        TextView user_name;
        TextView you_time;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.you_chat = itemView.findViewById(R.id.you_chat);
            this.you_time = itemView.findViewById(R.id.you_time);
        }
    }

    //내 채팅일 경우
    public class CustomViewHoler2 extends RecyclerView.ViewHolder {
        TextView my_chat;
        TextView my_time;

        public CustomViewHoler2(@NonNull View itemView) {
            super(itemView);
            this.my_chat = itemView.findViewById(R.id.my_chat);
            this.my_time = itemView.findViewById(R.id.my_time);
        }
    }


}