package com.sunit.thenewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {
    private TabLayout TabLayout;
    private ViewPager viewPager;
    private MyPageAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = findViewById(R.id.toolbar2);
//        setActionBar(toolbar);
//        getActionBar().setTitle("News");


        viewPager = findViewById(R.id.viewPager);
        TabLayout = findViewById(R.id.tablayout);

        adapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout.setTabsFromPagerAdapter(adapter);


        TabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TabLayout));

    }

}