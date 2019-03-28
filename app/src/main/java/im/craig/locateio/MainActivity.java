package im.craig.locateio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText usernameInput;
    private EditText passwordInput;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private String login_url = "https://craig.im/locateio/login.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.fNameInput);

        Button register = findViewById(R.id.registerButton);
        Button login = findViewById(R.id.loginButton);

        //Launch Registration screen when Register Button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = usernameInput.getText().toString().toLowerCase().trim();
                password = passwordInput.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        finish();

    }

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(username,response.getString(KEY_FIRST_NAME));
                                loadDashboard();

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

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(username)){
            usernameInput.setError("Username cannot be empty");
            usernameInput.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            passwordInput.setError("Password cannot be empty");
            passwordInput.requestFocus();
            return false;
        }
        return true;
    }
}



//        username = (EditText)findViewById(R.id.username);
//
//        password = (EditText)findViewById(R.id.username);
//
//        Button loginButton = (Button)findViewById(R.id.loginButton);
//
//        Button registerButton = (Button)findViewById(R.id.registerButton);
//
//
//
//
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//
//            public void onClick(View v) {
//
//                usernameInput = username.getText().toString();
//                String passwordInput = username.getText().toString();
//
//                if(usernameInput.equals("") || passwordInput.equals("")){
//
//                    Toast.makeText(MainActivity.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
//
//                    return;
//
//                }
//
//                if(usernameInput.length() <= 1 || passwordInput.length() <= 1){
//
//                    Toast.makeText(MainActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
//
//                    return;
//
//                }
//
//
//
//            }
//
//        });
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
//
//                startActivity(intent);
//
//            }
//
//        });
//
//    }
//}

