package com.example.together;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Chart_VP extends FragmentPagerAdapter {
    private ArrayList<Fragment> Items;//일간, 주간, 월간 통계의 Fragment
    private ArrayList<String> ItemText=new ArrayList<String>();//일간, 주간, 월간 통계 이름(탭과 뷰페이저 연동 위해서 필요)



    public Chart_VP(FragmentManager fm) {
        super(fm);
        //ArrayList를 Fragment형으로 정의해 추가
        Items=new ArrayList<Fragment>();
        Items.add(new Daily_main());
        Items.add(new Weekly());
        Items.add(new Monthly());

        //ArrayList를 String형으로 정의해 추가(뷰페이저 이동시 탭의 명칭도 보일수 있게)
        ItemText.add("일간통계");
        ItemText.add("주간통계");
        ItemText.add("월간통계");

    }

    @Override
    public Fragment getItem(int position) {
        return Items.get(position);
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    //현재 위치에 따라 Tab 아이템의 텍스트를 표시시
   @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return ItemText.get(position);
    }

}

