package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivityFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.Viewpager);
        viewPager.setAdapter(new MyPagerAdapter(this.getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {hideSoftKeyboard();}
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {hideSoftKeyboard();}
        });

        return view;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int pos){
            switch(pos){
                case 0: return CounterFragment.newInstance();
                case 1: return HistoryFragment.newInstance();
                default: return CounterFragment.newInstance();
            }
        }
        @Override
        public CharSequence getPageTitle(int pos){
            switch(pos){
                case 0: return getString(R.string.counter);
                case 1: return getString(R.string.history);
                default: return getString(R.string.counter);
            }
        }
        @Override
        public int getCount(){
            return 2;
        }
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null){
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }
}
