package com.example.together;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.together.Group.Group;



public class PagerAdapter extends FragmentStateAdapter {

    private int tabsNumber;

    public PagerAdapter(@NonNull FragmentManager fm, Lifecycle l, int tabs) {
        super(fm, l);
        this.tabsNumber = tabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Study();
            case 1:
                return new Daily();
            case 2:
                return new Group();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabsNumber;
    }
}