package com.example.glocal;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TabActivity extends FragmentActivity {
    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListFragment(), "LIST");
        adapter.addFragment(new MapFragment(), "MAP");
        viewPager.setAdapter(adapter);
    }
}
