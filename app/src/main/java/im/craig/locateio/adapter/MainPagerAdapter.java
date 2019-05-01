package im.craig.locateio.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import im.craig.locateio.fragments.ShareFragment;
import im.craig.locateio.fragments.FeedFragment;
import im.craig.locateio.fragments.MapFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    //fragment system for
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return ShareFragment.create();
            case 1:
                return FeedFragment.create();
            case 2:
                return MapFragment.create();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Share";
            case 1:
                return "Feed";
            case 2:
                return "Map";
        }
        //returns the current title based on what the current page position is
        return super.getPageTitle(position);

    }

    @Override
    //set how many frags there are
    public int getCount() {
        return 3;
    }


}
