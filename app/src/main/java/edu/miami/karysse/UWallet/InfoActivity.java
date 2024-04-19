package edu.miami.karysse.UWallet;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG = "InfoActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private DocumentReference emailDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // Get the currently logged-in user
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Get the user's cane_id
            String email = user.getEmail();

            // Get a reference to the "student_and_meal" collection
            emailDoc = db.collection("users").document(email);

            emailDoc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the info document within the person subcollection of the user's document
                        DocumentReference infoRef = emailDoc.collection("person").document("info");

                        // Get the meal plan document
                        infoRef.get().addOnCompleteListener(infoTask -> {
                            if (infoTask.isSuccessful()) {
                                DocumentSnapshot infoDocument = infoTask.getResult();
                                if (infoDocument.exists()) {
                                    // Get the info data

                                    String firstName = infoDocument.getString("first_name");
                                    String lastName = infoDocument.getString("last_name");
                                    //Email already declared above
                                    String caneID = infoDocument.getString("cane_id");


                                    TextView FirstNameValueTextView = findViewById(R.id.FirstNameValueTextView);
                                    FirstNameValueTextView.setText(firstName);

                                    TextView LastNameValueTextView = findViewById(R.id.LastNameValueTextView);
                                    LastNameValueTextView.setText(lastName);

                                    TextView EmailValueTextView = findViewById(R.id.EmailValueTextView);
                                    EmailValueTextView.setText(email);

                                    TextView CaneIDValueTextView = findViewById(R.id.CaneIDValueTextView);
                                    CaneIDValueTextView.setText(caneID);
                                } else {
                                    Log.d(TAG, "No such info document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", infoTask.getException());
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
    }
}


