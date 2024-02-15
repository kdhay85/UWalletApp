package edu.miami.karysse.mytwobuttons;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import java.io.File;


public class LoginActivity extends AppCompatActivity {

    private DBhandler dbHandler;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        File dbFile = new File(getExternalFilesDir(null), "uwallet_db.sqlite");

        dbHandler = new DBhandler(this);

        // Initialize the EditText views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);


    }

    public void loginClick(View view) {
        // Get user's email and password from the EditText views
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if the email and password match an existing record in the database
        boolean isValid = dbHandler.isValidCredentials(email, password);

        if (isValid) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            // Display an error message
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
