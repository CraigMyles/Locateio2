package im.craig.locateio.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import im.craig.locateio.fragments.CameraFragment;
import im.craig.locateio.fragments.FeedFragment;
import im.craig.locateio.fragments.MapFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return CameraFragment.create();
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
        return super.getPageTitle(position);

    }

    @Override
    public int getCount() {
        return 3;
    }


}
