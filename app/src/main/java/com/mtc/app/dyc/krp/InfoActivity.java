/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mtc.app.dyc.krp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.mtc.app.dyc.krp.fragment.ContactUsFragment;
import com.mtc.app.dyc.krp.fragment.GeneralRulesFragment;
import com.mtc.app.dyc.krp.fragment.InfoMainFragment;
import com.mtc.app.dyc.krp.fragment.InfoThemeFragment;

public class InfoActivity extends BaseActivity {

    private static final String TAG = "InfoActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private Fragment[] mFragments;
    private String[] mFragmentNames;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Create the adapter that will return a fragment for each section

        mFragments = new Fragment[]{
                new InfoMainFragment(),
                new InfoThemeFragment(),
                new ContactUsFragment(),
                new GeneralRulesFragment()
        };

        mFragmentNames = new String[]{
                "Info",
                "Speakers",
                "Contact Us",
                "General Rules"
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
