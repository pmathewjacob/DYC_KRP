package com.mtc.app.dyc.krp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.mtc.app.dyc.krp.fragment.Fragment24;
import com.mtc.app.dyc.krp.fragment.Fragment25;
import com.mtc.app.dyc.krp.fragment.Fragment26;
import com.mtc.app.dyc.krp.fragment.Fragment27;

public class ScheduleActivity extends BaseActivity {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private Fragment[] mFragments;
    private String[] mFragmentNames;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DYC 2018 Schedule");
        setContentView(R.layout.activity_schedule);

        mFragments = new Fragment[]{
                new Fragment24(),
                new Fragment25(),
                new Fragment26(),
                new Fragment27()
        };

        mFragmentNames = new String[]{
                "24 May",
                "25 May",
                "26 May",
                "27 May"
        };

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
