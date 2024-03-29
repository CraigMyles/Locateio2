package im.craig.locateio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import im.craig.locateio.adapter.MainPagerAdapter;
import im.craig.locateio.adapter.MyRecyclerViewAdapter;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, MyRecyclerViewAdapter.ItemClickListener, OnMapReadyCallback {

    private SessionHandler session;
    private ProgressDialog pDialog;
    GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    TextView tv1;
    TextView tv2;

    Double globalLong;
    Double globalLat;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String shareLocation_api_url = "https://craig.im/locateio/shareLocation.php";
    private String loadLocations_api_url = "https://craig.im/locateio/loadLocations.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LONGITUDE = "stringLongitude";
    private static final String KEY_LATITUDE = "stringLatitude";
    private static final String KEY_TITLE = "locationTitle";
    private static final String KEY_DESCRIPTION = "locationDesc";
    private static final String KEY_EXTRAINFO = "extraLocationInfo";
    private static final String KEY_RATING = "rating";
    private static final String KEY_EMPTY = "";
    private static final String KEY_KEY = "reqData";
    private String username;
    private String stringLongitude;
    private String stringLatitude;
    private String locationTitle;
    private String locationDesc;
    private String extraLocationInfo;
    private String rating;

    private String key;

    private ArrayList mLocationList;

    private MyRecyclerViewAdapter adapter;

    Location mLastLocation;

//    private GoogleMap mMap;
//
//    private static MapView mapView;
    private static MapView mapFragment;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        //get session
        session = new SessionHandler(getApplicationContext());
        final User user = session.getUserDetails();

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        final View background = findViewById(R.id.home_background_view);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_view_pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        final int colourGreen = ContextCompat.getColor(this, R.color.Green);
        final int colourPink = ContextCompat.getColor(this, R.color.Pink);
        final int colourBlue = ContextCompat.getColor(this, R.color.CafeSeaBlue);


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.am_tab_layout);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tabLayout.setupWithViewPager(viewPager);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);



        //call method to load the data to the feed
        loadLocations();


        //set screen to view fragment 1 (centre)
        viewPager.setCurrentItem(1);


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//        if(mapFragment == null)
            //mapFragment.onCreate(null);
            //mapFragment.onResume();
            //mapFragment.getMapAsync(this);


//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                //  wait for 3 seconds to update
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // cancle the Visual indication of a refresh
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        //reload data
//                        loadLocations();
//                    }
//                }, 3000);
//            }
//        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //if on first screen
                if (position == 0)
                {
                    background.setBackgroundColor(colourBlue);
                    //background.setAlpha(1-positionOffset);
                }

                //if on mid screen
                else if (position == 1) {
                    background.setBackgroundColor(colourPink);
                    //background.setAlpha(positionOffset);
                }

                //if on last screen
                else if (position == 2) {
                    background.setBackgroundColor(colourGreen);
                    //background.setAlpha(1+positionOffset);
                }


            }


            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                {
                    //get perms if none for location
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

                    }
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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


                    final EditText locationTitleInput = findViewById(R.id.locationTitleInput);
                    final EditText locationDescInput = findViewById(R.id.locationDescInput);

                    final RatingBar ratingBar = findViewById(R.id.ratingBar);


                    Button shareLocationBtn = (Button) findViewById(R.id.shareLocationBtn);

                    shareLocationBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String localLong = Double.toString(globalLong);
                            String localLat = Double.toString(globalLat);

                            username = user.getUsername();
                            stringLongitude = Double.toString(globalLong);
                            stringLatitude = Double.toString(globalLat);
                            locationTitle = locationTitleInput.getText().toString();
                            locationDesc = locationDescInput.getText().toString();

                            rating = String.valueOf(ratingBar.getRating());


                            if (!KEY_EMPTY.equals(locationDesc) || !KEY_EMPTY.equals(locationTitle)) {

                                Geocoder gcd = new Geocoder(HomeActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = gcd.getFromLocation(globalLat, globalLong, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (addresses.size() > 0) {


                                    if (addresses.get(0).getLocality() != null && addresses.get(0).getLocality().length() > 0) {
                                        extraLocationInfo = "Locality: " + addresses.get(0).getLocality();
                                    }

                                    if (addresses.get(0).getLocale() != null) {
                                        extraLocationInfo = "Postal Code: " + addresses.get(0).getPostalCode();
                                    }

                                    if (extraLocationInfo == null)
                                        extraLocationInfo = "No Additional Info Found";
                                } else {
                                    extraLocationInfo = "No Additional Info Found";
                                }


                                displayLoader();


                                JSONObject request = new JSONObject();
                                try {
                                    //input key parameters for external php json database query
                                    request.put(KEY_USERNAME, username);
                                    request.put(KEY_LONGITUDE, stringLongitude);
                                    request.put(KEY_LATITUDE, stringLatitude);
                                    request.put(KEY_TITLE, locationTitle);
                                    request.put(KEY_DESCRIPTION, locationDesc);
                                    request.put(KEY_EXTRAINFO, extraLocationInfo);
                                    request.put(KEY_RATING, rating);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                                        (Request.Method.POST, shareLocation_api_url, request, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                pDialog.dismiss();
                                                try {
                                                    //check if successful response

                                                    if (response.getInt(KEY_STATUS) == 0) {

                                                        //reset text
                                                        locationTitleInput.setText("");
                                                        locationDescInput.setText("");
                                                        ratingBar.setRating(0);

                                                        //reset view to feed
                                                        viewPager.setCurrentItem(1);
                                                        loadLocations();


                                                        Toast.makeText(getApplicationContext(),
                                                                "Successfully posted!", Toast.LENGTH_LONG).show();

                                                    } else {
                                                        Toast.makeText(getApplicationContext(),
                                                                response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                pDialog.dismiss();

                                                //Display error message whenever an error occurs
                                                Toast.makeText(getApplicationContext(),
                                                        error.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                // access singleton's request queue method
                                MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsArrayRequest);
                            }else{
                                if (KEY_EMPTY.equals(locationDesc) ) {
                                    locationDescInput.setError("Description cannot be empty");
                                    locationDescInput.requestFocus();
                                }
                                if (KEY_EMPTY.equals(locationTitle) ) {
                                    locationTitleInput.setError("Title cannot be empty");
                                    locationTitleInput.requestFocus();
                                }
                            }

                        }
                    });


                    if(tv1 == null)
                        tv1 = (TextView)findViewById(R.id.longitudeValue);
                    tv1.setText(Double.toString(globalLong));

                    if(tv2 == null)
                        tv2 = (TextView)findViewById(R.id.latitudeValue);
                    tv2.setText(Double.toString(globalLat));

                    //if on middle screen (Feed)
                }if(position == 1){

                    Button reload = (Button) findViewById(R.id.reload);

                    reload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //when reload icon is pressed, reload data
                            loadLocations();
                        }
                    });

                }if(position ==2){
//                    GoogleMap mMap;
//
//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(HomeActivity.this);
                    //private static MapView mapView;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    Toast.makeText(HomeActivity.this, "Position 2", Toast.LENGTH_SHORT).show();

                    //GoogleMap gmap;



                    onMapReady(gmap);



                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void loadLocations() {
        //getting the list of locations from the database
        displayLoader();
        JSONObject request = new JSONObject();
        key = "reqData";
        try {
            //input key parameters for external php json database query
            request.put(KEY_KEY, key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //the request
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, loadLocations_api_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //check if successful response

                            //0 means success
                            if (response.getInt(KEY_STATUS) == 0) {
                                Gson gson = new Gson();
                                LocationModel[] locationList = gson.fromJson(response.getString(KEY_MESSAGE), LocationModel[].class);

                                // set up the RecyclerView
                                RecyclerView recyclerView = findViewById(R.id.rv_feed);
                                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                                adapter = new MyRecyclerViewAdapter(HomeActivity.this, locationList);
                                adapter.setClickListener(HomeActivity.this);
                                recyclerView.setAdapter(adapter);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
                                recyclerView.addItemDecoration(dividerItemDecoration);

                                mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
                                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {
                                        // Your code here
                                        Toast.makeText(getApplicationContext(), "Reloading", Toast.LENGTH_LONG).show();

                                        // Stop animation after 4 seconds
                                        new Handler().postDelayed(new Runnable() {
                                            @Override public void run() {
                                                loadLocations();
                                                //stop animation
                                                mSwipeRefreshLayout.setRefreshing(false);
                                            }
                                        }, 3000);
                                    }
                                });



                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


        // access singleton's request queue method
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        globalLat = location.getLatitude();
        globalLong = location.getLongitude();

        Log.d("Location","Longitude = "+currentLongitude);
        Log.d("Location","Latitude = "+currentLatitude);



        if(tv1 != null)
            tv1.setText(Double.toString(currentLatitude));


        if(tv2 != null)
            tv2.setText(Double.toString(currentLongitude));

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
    //progress bar
    private void displayLoader() {
        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Loading.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "This is item number " + adapter.getItem(position), Toast.LENGTH_SHORT).show();

    }

    @Override //saves instant state
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

    }

    //life cycle
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //Toast.makeText(this, "some toast", Toast.LENGTH_SHORT).show();

        //mMap.setMapStyle()
//        gmap=map;
//
//        gmap.addMarker(new MarkerOptions()
//                .position(new LatLng(10, 10))
//                .title("Hello world"));

        //gmap.clear();
    }

//    void refreshItems() {
//        // Load items
//        loadLocations();
//        // Load complete
//        onItemsLoadComplete();
//    }
//
//    void onItemsLoadComplete() {
//        // Update the adapter and notify data set changed
//        // ...
//
//        // Stop refresh animation
//        mSwipeRefreshLayout.setRefreshing(false);
//    }

}