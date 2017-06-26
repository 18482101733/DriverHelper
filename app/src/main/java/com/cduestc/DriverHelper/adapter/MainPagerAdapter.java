package com.cduestc.DriverHelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/4.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> tabs;
    private Context context;

    public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> tabs, Context context) {
        super(fm);
        this.tabs = tabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        return super.instantiateItem(container, position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }


}
