package com.example.together.Group;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;

import java.util.ArrayList;

//사진 관련 부분은 일단 주석처리 했습니다. 굳이 그룹에서 데이터베이스 연동할 이유 없어보여서요.
public class Together_CustomAdapter extends RecyclerView.Adapter<Together_CustomAdapter.CustomViewHoler> {

    private ArrayList<Together_group_list> arrayList;
    private Context context;


    public Together_CustomAdapter(ArrayList<Together_group_list> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.together_list_item, parent, false);
        CustomViewHoler holer = new CustomViewHoler(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) { //사진 만드는 그런것..
        /*
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getiv_people())
                .into(holder.iv_people); */
        holder.Gname.setText(arrayList.get(position).getGname());
        holder.GAP.setText(String.valueOf(arrayList.get(position).getGAP()));
        holder.GCP.setText(String.valueOf(arrayList.get(position).getGCP()));

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

    }

    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHoler extends RecyclerView.ViewHolder {
        //ImageView iv_people;
        TextView Gname;
        TextView GCP;
        TextView GAP;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            //this.iv_people = itemView.findViewById(R.id.iv_people);
            this.Gname = itemView.findViewById(R.id.Gname);
            this.GAP = itemView.findViewById(R.id.GAP);
            this.GCP = itemView.findViewById(R.id.GCP);
        }
    }
}

