package com.example.together;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Daily extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily);

        //ViewPager와 Adapter의 연동(https://godog.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%B7%B0%ED%8E%98%EC%9D%B4%EC%A0%80-%ED%83%AD-%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83-%EA%B5%AC%ED%98%84-1-%EC%A2%8C%EC%9A%B0%EB%A1%9C-%EB%B0%80%EC%96%B4%EC%84%9C-%ED%8E%98%EC%9D%B4%EC%A7%80-%EC%A0%84%ED%99%98?category=781741)
        ViewPager vp=findViewById(R.id.viewpager);
        Chart_VP adapter=new Chart_VP(getSupportFragmentManager());
        vp.setAdapter(adapter);

        //Tab과 ViewPager의 연동
        TabLayout tab=findViewById(R.id.chart_tab);
        tab.setupWithViewPager(vp);
    }
}
