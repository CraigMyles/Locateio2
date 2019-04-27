package im.craig.locateio.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private View mRoot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(getLayoutResId(), container, false);
        inOnCreateView(mRoot, container, savedInstanceState);
        return mRoot;


    }

    @LayoutRes
    public abstract int getLayoutResId();



    public abstract void inOnCreateView(View root, @LayoutRes ViewGroup container, @Nullable Bundle savedInstanceState);
}
