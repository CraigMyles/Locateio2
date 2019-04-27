package im.craig.locateio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionHandler(getApplicationContext());
        final User user = session.getUserDetails();
        //getSupportActionBar().setTitle("Locateio - "+user.getUsername()+"");

        TextView welcomeText = findViewById(R.id.welcomeText);


        welcomeText.setText("Welcome "+user.getFName()+", your session will expire on "+user.getSessionExpiryDate());

        Button logoutBtn = findViewById(R.id.btnLogout);
        Button questionBtn = findViewById(R.id.btnQuestion);

        Button homeBtn = findViewById(R.id.btnHome);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(Dashboard.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });


        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Welcome " + user.getFName() + ", your session will expire on " + user.getSessionExpiryDate(), Toast.LENGTH_LONG).show();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
