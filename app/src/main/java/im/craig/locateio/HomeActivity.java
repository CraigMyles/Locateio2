package im.craig.locateio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import im.craig.locateio.adapter.MainPagerAdapter;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    TextView tv1;
    TextView tv2;
//    TextView latitudeValue;
//    TextView longitudeValue;


    //    mLastLocation mLastLocation;
    Location mLastLocation;
    //TextView latitudeValue = (TextView) findViewById(R.id.latitudeValue);
    //TextView longitudeValue = (TextView) findViewById(R.id.longitudeValue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //tv1.setText("TESTING");
        //longitudeValue.setText("testing 2");

        final View background = findViewById(R.id.home_background_view);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_view_pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        final int colourGreen = ContextCompat.getColor(this, R.color.Green);
        final int colourPink = ContextCompat.getColor(this, R.color.Pink);
        final int colourBlue = ContextCompat.getColor(this, R.color.CafeSeaBlue);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.am_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);


//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//
//        }
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            //If you have permission;
//            if (mGoogleApiClient == null) {
//                mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
//                        .addConnectionCallbacks(HomeActivity.this)
//                        .addOnConnectionFailedListener(HomeActivity.this)
//                        .addApi(LocationServices.API)
//                        .build();
//            }
//            mLocationManager = (LocationManager)
//                    getSystemService(LOCATION_SERVICE);
//
//
//            Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_FINE);
//            criteria.setAltitudeRequired(true);
//            criteria.setBearingRequired(true);
//            criteria.setCostAllowed(true);
//            criteria.setPowerRequirement(Criteria.POWER_LOW);
//            String provider = mLocationManager.getBestProvider(criteria, true);
//
//            if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
//
//            Location lastLocale = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            //if(lastLocale != null)
//                //onLocationChanged(lastLocale);
//
//        }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //if on middle screen
                if (position == 0)
                {
                    background.setBackgroundColor(colourBlue);
                    //background.setAlpha(1-positionOffset);
//
//                    TextView latitudeValue = (TextView) findViewById(R.id.latitudeValue);
//                    TextView longitudeValue = (TextView) findViewById(R.id.longitudeValue);


                    //tv1.setText("testing");




                    Button getLocationBtn = (Button) findViewById(R.id.getLocationBtn);







                }



                    //if on middle screen
                    else if (position == 1) {
                        background.setBackgroundColor(colourPink);
                        //background.setAlpha(positionOffset);
                    } else if (position == 2) {
                        background.setBackgroundColor(colourGreen);
                        //background.setAlpha(1+positionOffset);
                    }


                }


            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                    }
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //If you have permission;
                        if (mGoogleApiClient == null) {
                            mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                                    .addConnectionCallbacks(HomeActivity.this)
                                    .addOnConnectionFailedListener(HomeActivity.this)
                                    .addApi(LocationServices.API)
                                    .build();
                        }
                        mLocationManager = (LocationManager)
                                getSystemService(LOCATION_SERVICE);


                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        criteria.setAltitudeRequired(true);
                        criteria.setBearingRequired(true);
                        criteria.setCostAllowed(true);
                        criteria.setPowerRequirement(Criteria.POWER_LOW);
                        String provider = mLocationManager.getBestProvider(criteria, true);

                        if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, HomeActivity.this);

                        Location lastLocale = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(lastLocale != null)
                            onLocationChanged(lastLocale);



                    }



                    if(tv1 == null)
                        tv1 = (TextView)findViewById(R.id.longitudeValue);

                    if(tv2 == null)
                        tv2 = (TextView)findViewById(R.id.latitudeValue);

//                    Button getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
//                    getLocationBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            viewPager.setCurrentItem(1);
//                             viewPager.setCurrentItem(0);
//                        }
//                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        //mLocationManager.removeUpdates((android.location.LocationListener) HomeActivity.this);
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

//        String currentLatitudeString = Double.toString(currentLatitude);
//        String currentLongitudeString = Double.toString(currentLongitude);

        Log.d("Location","Longitude = "+currentLongitude);
        Log.d("Location","Latitude = "+currentLatitude);

//        Log.d("Location","Longitude String = "+currentLatitudeString);
//        Log.d("Location", "Latitude String = "+currentLongitudeString);


//        latitudeValue.setText(Double.toString(location.getLatitude()));
//        longitudeValue.setText(Double.toString(location.getLongitude()));

//        latitudeValue.setText(Double.toString(currentLatitude));
//        longitudeValue.setText(Double.toString(currentLongitude));

        //tv1.setText(Double.toString(currentLatitude));
        //tv2.setText(Double.toString(currentLongitude));

        if(tv1 != null)
            tv1.setText(Double.toString(currentLatitude));


        if(tv2 != null)
            tv2.setText(Double.toString(currentLongitude));

        //Toast.makeText(this, "Location Known", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}