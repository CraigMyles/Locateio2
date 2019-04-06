package im.craig.locateio.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import im.craig.locateio.R;

public class FeedFragment extends BaseFragment {

    public static FeedFragment create(){
        return new FeedFragment();
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_feed;

    }

    @Override
    public void inOnCreateView(View root, ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
