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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.lang.reflect.Type;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import im.craig.locateio.adapter.MainPagerAdapter;
import im.craig.locateio.adapter.MyRecyclerViewAdapter;

class LocationModel {
    @SerializedName("locationID")
    public String mlocationID;
    @SerializedName("username")
    public String musername;
    @SerializedName("title")
    public String mtitle;
    @SerializedName("description")
    public String mdescription;
    @SerializedName("extraInfo")
    public String mextraInfo;
    @SerializedName("lat")
    public String mlat;
    @SerializedName("lng")
    public String mlng;
    @SerializedName("rating")
    public String mrating;
    @SerializedName("posted")
    public String mposted;

}

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, MyRecyclerViewAdapter.ItemClickListener, OnMapReadyCallback {

    private SessionHandler session;
    private ProgressDialog pDialog;
    GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    TextView tv1;
    TextView tv2;

    Double globalLong;
    Double globalLat;


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

    GoogleMap mGoogleMap;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionHandler(getApplicationContext());
        final User user = session.getUserDetails();



        final View background = findViewById(R.id.home_background_view);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_view_pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        final int colourGreen = ContextCompat.getColor(this, R.color.Green);
        final int colourPink = ContextCompat.getColor(this, R.color.Pink);
        final int colourBlue = ContextCompat.getColor(this, R.color.CafeSeaBlue);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.am_tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        loadLocations();


        viewPager.setCurrentItem(1);


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


                            Geocoder gcd = new Geocoder(HomeActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(globalLat, globalLong, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses.size() > 0) {

                                Log.d("address admin area", addresses.get(0).getAdminArea());

                                Log.d("address.get", String.valueOf(addresses.get(0)));

                                Log.d("get features name", addresses.get(0).getFeatureName());

                                Log.d("get address line 0", addresses.get(0).getAddressLine(0));


                                if (addresses.get(0).getLocality() != null && addresses.get(0).getLocality().length() > 0) {
                                    extraLocationInfo = "Locality: "+addresses.get(0).getLocality();
                                }

                                if(addresses.get(0).getLocale() != null) {
                                    extraLocationInfo = "Postal Code: "+addresses.get(0).getPostalCode();
                                }

                                if (extraLocationInfo==null)
                                    extraLocationInfo="No Additional Info Found";
                            }
                            else {
                                extraLocationInfo="No Additional Info Found";
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

                                Log.d("sadness", "username: " +username);
                                Log.d("sadness", "title: " +locationTitle);
                                Log.d("sadness", "description: " +locationDesc);
                                Log.d("sadness", "extraInfo: " +extraLocationInfo);
                                Log.d("sadness", "lat: " +stringLatitude);
                                Log.d("sadness", "lng: " +stringLongitude);
                                Log.d("sadness", "rating: " +rating);

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
                                                    Toast.makeText(getApplicationContext(),
                                                            "Successfully posted!", Toast.LENGTH_LONG).show();

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
                            MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsArrayRequest);

                        }
                        });


                    if(tv1 == null)
                        tv1 = (TextView)findViewById(R.id.longitudeValue);
                        tv1.setText(Double.toString(globalLong));

                    if(tv2 == null)
                        tv2 = (TextView)findViewById(R.id.latitudeValue);
                        tv2.setText(Double.toString(globalLat));




                }if(position == 2){
                    //code for when screen is on the Map View




                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void loadLocations() {

        displayLoader();
        JSONObject request = new JSONObject();
        key = "reqData";
        try {
            //input key parameters for external php json database query
            request.put(KEY_KEY, key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, loadLocations_api_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //check if successful response

                            if (response.getInt(KEY_STATUS) == 0) {

                                ArrayList<String> myLocations = new ArrayList<>();

                                Gson gson = new Gson();
                                LocationModel[] locationList = gson.fromJson(response.getString(KEY_MESSAGE), LocationModel[].class);
                                for (LocationModel location : locationList){

                                    myLocations.add(location.mlocationID);
                                    myLocations.add(location.musername);
                                    myLocations.add(location.mtitle);
                                    myLocations.add(location.mdescription);
                                    myLocations.add(location.mextraInfo);
                                    myLocations.add(location.mlat);
                                    myLocations.add(location.mlng);
                                    myLocations.add(location.mrating);
                                    myLocations.add(location.mposted);

                                    //Log.d("test", "onResponse:"+location.mlocationID);
                                    //Log.d("test2", "onResponse:"+location.mlocationID + "-" + location.musername + "-" + location.mtitle + "-" + location.mdescription + "-" + location.mextraInfo + "-" + location.mlat + "-" + location.mlng + "-" + location.mrating + "-" + location.mposted);

                                    //Log.d("test", "onResponse: LocationList Length "+locationList.length);
                                }




                                // set up the RecyclerView
                                RecyclerView recyclerView = findViewById(R.id.rv_feed);
                                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                                adapter = new MyRecyclerViewAdapter(HomeActivity.this, myLocations);
                                adapter.setClickListener(HomeActivity.this);
                                recyclerView.setAdapter(adapter);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
                                recyclerView.addItemDecoration(dividerItemDecoration);






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
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        //super.onview(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        MapsInitializer.initialize(HomeActivity.this);
//        mGoogleMap = googleMap;
//        googleMap.setMapStyle(1);
    }


}