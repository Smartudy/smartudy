package com.sharewith.smartudy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Simjae on 2018-07-14.
 */

public class WriteFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> datas;
    private int mTabCount;

    public WriteFragmentPagerAdapter(FragmentManager fm,List<Fragment> datas,int count) {
        super(fm);
        this.datas = datas;
        this.mTabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == mTabCount-1)
            return datas.get(1);
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }
}
