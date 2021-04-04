package com.example.together;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Weekly#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Weekly extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PieChart pieChart;
    int[] colorArray=new int[]{Color.rgb(252,197,238),Color.rgb(250,243,175), Color.rgb(175,222,250),Color.rgb(148,229,220)};

    public Weekly() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Weekly.
     */
    // TODO: Rename and change types and number of parameters
    public static Weekly newInstance(String param1, String param2) {
        Weekly fragment = new Weekly();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Fragment를 사용하고 있기 때문에 inflater 하기 전에 findViewById 호출시 문제가 생길 수 있다.
        //onCreateView에서 View 객체를 만들고 바로 findViewById를 통해 xml에 등록된 객체를 등록한다.
        View v=inflater.inflate(R.layout.fragment_weekly,container,false);

        //PieChart 구현
        pieChart=v.findViewById(R.id.weeklyPicChart);

        //PieChart 이름
        PieDataSet pieDataSet=new PieDataSet(WeekData(),"주간 공부");

        //차트의 색상
        pieDataSet.setColors(colorArray);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);

        //내용 글씨 크기
        pieData.setValueTextSize(25);

        //가운데 글씨 크기
        pieChart.setCenterText("주간공부");
        pieChart.setCenterTextSize(25);
        pieChart.setData(pieData);
        pieChart.invalidate();

        return v;
    }

    private ArrayList<PieEntry> WeekData(){
        ArrayList<PieEntry> devalues;
        devalues = new ArrayList<>();

        //PieChart 내부 내용 추가 PieEntry(퍼센트,텍스트)
        devalues.add(new PieEntry(30,"정처기"));
        devalues.add(new PieEntry(30,"자바"));
        devalues.add(new PieEntry(20,"C언어"));
        devalues.add(new PieEntry(10,"기타"));

        return devalues;
    }

}