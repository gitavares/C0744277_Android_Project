package com.giselletavares.c0744277_finalproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.giselletavares.c0744277_finalproject.fragments.InboxFragment;
import com.giselletavares.c0744277_finalproject.fragments.NextDaysFragment;
import com.giselletavares.c0744277_finalproject.fragments.TodayFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new TodayFragment();
            case 2:
                return new NextDaysFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
