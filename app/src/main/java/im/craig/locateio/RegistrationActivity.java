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

public class RegistrationActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_LNAME = "lName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText usernameInput;
    private EditText fNameInput;
    private EditText lNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordConfirm;
    private String username;
    private String password;
    private String confirmPassword;
    private String fName;
    private String lName;
    private String email;
    private ProgressDialog pDialog;
    private String register_url = "https://craig.im/locateio/register.php";
    private SessionHandler session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        ///actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        session = new SessionHandler(getApplicationContext());

        usernameInput = findViewById(R.id.usernameInput);
        usernameInput.requestFocus();
        fNameInput = findViewById(R.id.fNameInput);
        lNameInput = findViewById(R.id.lNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        Button login = findViewById(R.id.goBack);
        Button register = findViewById(R.id.registerButton);

        //Launch Login screen when Login Button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = usernameInput.getText().toString().toLowerCase().trim();
                password = passwordInput.getText().toString().trim();
                confirmPassword = passwordConfirm.getText().toString().trim();
                fName = fNameInput.getText().toString().trim();
                lName = lNameInput.getText().toString().trim();
                email = emailInput.getText().toString().trim();

                if (validateInputs()) {
                    registerUser();
                }

            }
        });

    }

    //display progress when registering account
    private void displayLoader() {
        pDialog = new ProgressDialog(RegistrationActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }


     //Launch Dashboard Activity on success
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        finish();

    }

    private void registerUser() {


        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //populate request params
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_FNAME, fName);
            request.put(KEY_LNAME, lName);
            request.put(KEY_EMAIL, email);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Create a session for the user
                                session.loginUser(username,fName);
                                loadDashboard();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //if error 1, user is taken and display message
                                usernameInput.setError("Username already taken!");
                                usernameInput.requestFocus();

                            }else if(response.getInt(KEY_STATUS) == 2){
                                //display if error message 2 missing user input
                                usernameInput.setError("Key parameters missing!");
                                usernameInput.requestFocus();

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

                        //display error message
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        //get request queue from singleton
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


    //Validates inputs and/or shows errors
    //if errors lie, set focus on related form input
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(username)) {
            usernameInput.setError("Username cannot be empty");
            usernameInput.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(fName)) {
            fNameInput.setError("First name cannot be empty");
            fNameInput.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(lName)) {
            lNameInput.setError("Last name cannot be empty");
            fNameInput.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            passwordInput.setError("Password cannot be empty");
            passwordInput.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(confirmPassword)) {
            passwordConfirm.setError("Confirm Password cannot be empty");
            passwordConfirm.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            passwordConfirm.setError("Password and Confirm Password does not match");
            passwordConfirm.requestFocus();
            return false;
        }

        return true;
    }
}