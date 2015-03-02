package com.pangff.volleyplusextension;

import android.support.v4.app.Fragment;

/**
 * Created by mac on 15-2-13.
 */
public class FragmentFactory {

    public static Fragment getFragment(int position){
        Fragment fragment = null;
        switch (position){
            case 1:
                fragment = LocalFragment.newInstance();
                break;
            case 2:
                fragment = StrategyFragment.newInstance();
                break;
            case 3:
                fragment = AboutMeFragment.newInstance();
                break;
        }
        return fragment;
    }
}
