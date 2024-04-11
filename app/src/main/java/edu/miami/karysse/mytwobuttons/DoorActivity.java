package edu.miami.karysse.mytwobuttons;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoorActivity extends AppCompatActivity {

    private static final String TAG = "DoorActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private DocumentReference emailDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_page);

        // Get the currently logged-in user
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Get the user's email
            String email = user.getEmail();

            // Get a reference to the user's document in Firestore
            emailDoc = db.collection("users").document(email);

            // Get the document
            emailDoc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get dorm access information
                        DocumentReference dormRoomref = emailDoc.collection("person").document("dorm");

                        dormRoomref.get().addOnCompleteListener(dormRoomTask -> {
                            if (dormRoomTask.isSuccessful()) {
                                DocumentSnapshot dormRoomDocument = dormRoomTask.getResult();
                                if (dormRoomDocument.exists()) {
                                    // Get the meal plan data

                                    long dormRoom = dormRoomDocument.getLong("room_no");
                                    String dormName = dormRoomDocument.getString("dorm_name");


                                    // Convert to strings
                                    String dormRoomString = String.valueOf(dormRoom);

                                    TextView dormRoomValueTextView = findViewById(R.id.dormRoomValueTextView);
                                    dormRoomValueTextView.setText(dormRoomString);

                                    TextView dormNameValueTextView = findViewById(R.id.dormNameValueTextView);
                                    dormNameValueTextView.setText(dormName);


                                } else {
                                    Log.d(TAG, "No such Dorm Room document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", dormRoomTask.getException());
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
