package edu.miami.karysse.mytwobuttons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    //private DataSQLiteDB dataSQLiteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginSubmitButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if(!password.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (user != null) {
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "User is not authenticated", Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        //finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        passwordEditText.setError("Password cannot be empty");
                    }
                }else if(email.isEmpty()){
                    emailEditText.setError("Email cannot be empty");
                }else{
                    emailEditText.setError("Please enter valid email");
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //auth.auth.signOut();
        //dataSQLiteDB.close();
    }
}
