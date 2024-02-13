package edu.miami.karysse.mytwobuttons;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.app.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myClickHandler(View view) {

        //Button clicked;


        switch (view.getId()) {
            case R.id.login_button:
                // Start the Login Activity
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }
    }
}