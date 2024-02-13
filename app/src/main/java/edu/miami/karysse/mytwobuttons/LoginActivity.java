package edu.miami.karysse.mytwobuttons;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
    }
    public void loginClick(View view) {

        //Button clicked;


        switch (view.getId()) {
            case R.id.loginSubmitButton:
                // Start the Login Activity
                startActivity(new Intent(this, HomeActivity.class));
                break;
            default:
                break;
        }
    }

}
