package edu.miami.karysse.mytwobuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

//KAILA
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Button mealPlanButton = findViewById(R.id.mealPlanButton);
        Button doorAccessButton = findViewById(R.id.doorAccessButton);
        Button cardAccessButton = findViewById(R.id.useCardNow);
        Button settingsButton = findViewById(R.id.settingsButton);
        mealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for meal plan button
                Toast.makeText(HomeActivity.this, "Meal Plan Button Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, MealActivity.class));
            }
        });

        doorAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for door access button
                Toast.makeText(HomeActivity.this, "Door Access Button Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, DoorActivity.class));
            }
        });
        cardAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for door access button
                Toast.makeText(HomeActivity.this, "Using card now", Toast.LENGTH_SHORT).show();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for settings button
                Toast.makeText(HomeActivity.this, "Settings Button Clicked", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

}