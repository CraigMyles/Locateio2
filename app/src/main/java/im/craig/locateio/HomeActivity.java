package im.craig.locateio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import im.craig.locateio.adapter.MainPagerAdapter;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private SessionHandler session;
    private ProgressDialog pDialog;
    GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    TextView tv1;
    TextView tv2;

    Double globalLong;
    Double globalLat;



    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_EMPTY = "";
    private String username;
    private String stringLongitude;
    private String stringLatitude;
    private String locationTitle;
    private String locationDesc;
    private EditText locationTitleInput;
    private EditText locationDescInput;





    Location mLastLocation;

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
                    final EditText locationDescriptionInput = findViewById(R.id.locationDescInput);

                    final RatingBar ratingBar = findViewById(R.id.ratingBar);


                    Button shareLocationBtn = (Button) findViewById(R.id.shareLocationBtn);

                    shareLocationBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
l                            //Retrieve the data entered in the edit texts
//                            username = usernameInput.getText().toString().toLowerCase().trim();
//                            password = passwordInput.getText().toString().trim();
//                            confirmPassword = passwordConfirm.getText().toString().trim();
//                            fName = fNameInput.getText().toString().trim();
//                            lName = lNameInput.getText().toString().trim();
//                            email = emailInput.getText().toString().trim();


                            String localLong = Double.toString(globalLong);
                            String localLat = Double.toString(globalLat);

                            username = user.getUsername();
                            stringLongitude = Double.toString(globalLong);
                            stringLatitude = Double.toString(globalLat);
                            locationTitle = locationTitleInput.getText().toString();
                            locationDesc = locationDescInput.getText().toString();







//                            private String username;
//                            private String stringLongitude;
//                            private String stringLatitude;
//                            private String locationTitle;
//                            private String locationDesc;



//                            Toast.makeText(HomeActivity.this,
//                                    "Longitude = "+localLong+" Lat = "+localLat , Toast.LENGTH_LONG).show();
                            Toast.makeText(HomeActivity.this,
                                    "test" , Toast.LENGTH_LONG).show();

                            //displayLoader();




//                            JSONObject request = new JSONObject();
//                            try {
//                                //input key parameters for external php json database query
//                                request.put(KEY_USERNAME, username);
//                                request.put(KEY_LONGITUDE, stringLongitude);
//                                request.put(KEY_LATITUDE, stringLatitude);
//                                request.put(KEY_TITLE, locationTitle);
//                                request.put(KEY_DESCRIPTION, locationDesc);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
//                                    (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//                                            pDialog.dismiss();
//                                            try {
//                                                //check if successful response
//
//                                                if (response.getInt(KEY_STATUS) == 0) {
//                                                    session.loginUser(username,response.getString(KEY_FIRST_NAME));
//                                                    loadDashboard();
//
//                                                }else{
//                                                    Toast.makeText(getApplicationContext(),
//                                                            response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }, new Response.ErrorListener() {
//
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            pDialog.dismiss();
//
//                                            //Display error message whenever an error occurs
//                                            Toast.makeText(getApplicationContext(),
//                                                    error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
//
//                            // access singleton's request queue method
//                            MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);



                            /*if (validateInputs()) {
                                registerUser();
                            }*/

                        }
                    });





                    if(tv1 == null)
                        tv1 = (TextView)findViewById(R.id.longitudeValue);
                        tv1.setText(Double.toString(globalLong));

                    if(tv2 == null)
                        tv2 = (TextView)findViewById(R.id.latitudeValue);
                        tv2.setText(Double.toString(globalLat));




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
    //progress bar for login
    private void displayLoader() {
        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Posting.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
}