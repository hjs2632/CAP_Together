package com.example.together.Group;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//사진 관련 부분은 일단 주석처리 했습니다. 굳이 그룹에서 데이터베이스 연동할 이유 없어보여서요.
public class Glook_Adapter extends RecyclerView.Adapter<Glook_Adapter.CustomViewHoler> {


    private ArrayList<Glook_list> arrayList;
    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //String uid = user.getUid();


    public Glook_Adapter(ArrayList<Glook_list> arrayList, Context context) {
        this.arrayList = arrayList;
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
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) { //사진 만드는 그런것..
        /*
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getiv_people())
                .into(holder.iv_people); */
        holder.uid.setText(arrayList.get(position).getuname());

        /*

        //클릭 이벤트
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GName = holder.Gname.getText().toString(); //그룹 이름을 저 변수에 담는다!

                Intent intent = new Intent(context.getApplicationContext(), look_group.class); //그룹 상세 화면으로 연결
                intent.putExtra("Gname", GName); //그룹 이름 넘겨서 열기
                context.startActivity(intent); //액티비티 열기
                Toast.makeText(view.getContext(), GName,Toast.LENGTH_SHORT).show(); //토스트로 실험
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String GName = holder.Gname.getText().toString(); //그룹 이름을 저 변수에 담는다!
                return true;
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewHoler extends RecyclerView.ViewHolder {
        //ImageView iv_people;
        TextView uid;


        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            //this.iv_people = itemView.findViewById(R.id.iv_people);
            this.uid = itemView.findViewById(R.id.username);

        }
    }


}

