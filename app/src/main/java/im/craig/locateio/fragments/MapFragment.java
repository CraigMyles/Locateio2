package im.craig.locateio.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import im.craig.locateio.R;

public class MapFragment extends BaseFragment {

    public static MapFragment create(){
        return new MapFragment();
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_map;
    }

    @Override
    public void inOnCreateView(View root, ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}