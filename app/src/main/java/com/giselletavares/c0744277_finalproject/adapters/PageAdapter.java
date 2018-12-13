package com.giselletavares.c0744277_finalproject.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.giselletavares.c0744277_finalproject.fragments.HistoryFragment;
import com.giselletavares.c0744277_finalproject.fragments.InboxFragment;
import com.giselletavares.c0744277_finalproject.fragments.NextDaysFragment;
import com.giselletavares.c0744277_finalproject.fragments.TodayFragment;

import java.util.HashMap;
import java.util.Map;

public class PageAdapter extends FragmentPagerAdapter {

    private Map<Integer, String> mFragmentsTag;
    private FragmentManager mFragmentManager;
    private int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mFragmentManager = fm;
        this.mNumOfTabs = numOfTabs;
        this.mFragmentsTag = new HashMap<>();
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
            case 3:
                return new HistoryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        Object obj = super.instantiateItem(container, position);
        if(obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            String tag = fragment.getTag();
            mFragmentsTag.put(position, tag);
        }

        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentsTag.get(position);
        if(tag == null) {
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }
}
