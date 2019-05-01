package im.craig.locateio.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import im.craig.locateio.R;


public class ShareFragment extends BaseFragment {

    public static ShareFragment create(){
        return new ShareFragment();
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_share;
    }

    @Override
    public void inOnCreateView(View root, ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}