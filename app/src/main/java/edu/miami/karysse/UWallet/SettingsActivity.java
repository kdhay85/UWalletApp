package edu.miami.karysse.UWallet;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button ViewCardButton;
    private Button ViewInfoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);


        // Set click listeners for buttons
        logoutButton = findViewById(R.id.logoutSubmitButton);
        ViewCardButton = findViewById(R.id.ViewCardButton);
        ViewInfoButton = findViewById(R.id.ViewInfoButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });

        ViewCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, CaneCardActivity.class));
                //finish();
            }
        });

        ViewInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, InfoActivity.class));
                //finish();
            }
        });
    }

}
