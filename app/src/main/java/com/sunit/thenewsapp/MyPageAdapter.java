package com.sunit.thenewsapp;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {


    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        MainFragment frag = new MainFragment(i);
    return frag;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0)
            return "VICE-News";
        else if(position == 1)
            return "Ary News";  //ary-news
        else if(position ==2)
            return "BBC News";  //bbc-news
        else if(position ==3)
            return "BBC Sport"; //bbc-sport
        else if(position == 4)
            return "USA Today";    //USA Today
        else if(position ==5)
            return "CNN";    //cnn
        else if(position ==6)
            return "Fox News";  //fox-news
        else if(position ==7)
            return "Google News"; //google-news
        else if(position ==8)
            return "The Verge";
        else if(position ==9)
            return "News24";    //news24
        else
            return "hello";
    }
}

