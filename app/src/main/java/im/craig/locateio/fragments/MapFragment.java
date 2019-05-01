package im.craig.locateio.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import im.craig.locateio.R;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;


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


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}