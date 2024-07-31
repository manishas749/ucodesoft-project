package com.errolapplications.bible365kjv;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentTabAdapter extends FragmentPagerAdapter {

    private Context mContext;


    public FragmentTabAdapter(Context context, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new OldTestFragment();
        } else {
            return new NewTestFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:

                return mContext.getString(R.string.tab_old_test);

            case 1:

                return mContext.getString(R.string.tab_new_test);

        }
        return null;
    }
}
