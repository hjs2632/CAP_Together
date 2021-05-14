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
import com.example.together.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> { //ArrayList에 있는 Dictionary 클래스 타입의 데이터를 RecyclerView에 보여주는 처리를 함.
    private ArrayList<Dictionary> mList;
    private Context mContext;

    public class CustomViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{ //리스너 추가

        protected TextView subject;
        protected TextView page;

        public Button btn_focus;

        public CustomViewHolder(View view) {
            super(view);
            this.subject = (TextView) view.findViewById(R.id.subject_listitem);
            this.page = (TextView) view.findViewById(R.id.page_listitem);
            this.btn_focus=(Button)view.findViewById(R.id.btn_focus);
            view.setOnCreateContextMenuListener(this); //2. 리스너 등록

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
                                String strSubject = editTextSubject.getText().toString();
                                String strPage = editTextPage.getText().toString();

                                Dictionary dict = new Dictionary(strSubject, strPage);

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        break;

                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

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
        //집중모드 연결
        viewholder.btn_focus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), FdActivity.class); //인텐트
                mContext.startActivity(intent); //액티비티 열기
            }

        });


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
