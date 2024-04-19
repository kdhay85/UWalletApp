package edu.miami.karysse.UWallet;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class DoorActivity extends AppCompatActivity {

    private static final String TAG = "DoorActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ListView universityBuildingAccessList;

    private DocumentReference emailDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_page);

        // Get the currently logged-in user
        FirebaseUser user = auth.getCurrentUser();

        universityBuildingAccessList = findViewById(R.id.tuniversity_building_access_lis);
        ArrayList<String> buildingAccessEntries = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, buildingAccessEntries);
        universityBuildingAccessList.setAdapter(adapter);

        if (user != null) {
            // Get the user's email
            String email = user.getEmail();
            checkAndFetchBuildingAccess(email, adapter);

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

    private void checkAndFetchBuildingAccess(String email, ArrayAdapter<String> adapter) {
        String[] buildings = {"Dooly", "McArthur", "Whitten"};
        for (String building : buildings) {
            fetchRoomsForBuilding(email, building, adapter);
        }
    }

    private void fetchRoomsForBuilding(String email, String building, ArrayAdapter<String> adapter) {
        db.collection("users").document(email).collection(building).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        StringBuilder roomsBuilder = new StringBuilder();
                        boolean isFirstRoom = true;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!isFirstRoom) {
                                roomsBuilder.append(", ");
                            }
                            roomsBuilder.append(document.getId());
                            isFirstRoom = false;
                        }
                        String entry = building + "\nRooms: " + roomsBuilder.toString();
                        adapter.add(entry);
                        adapter.notifyDataSetChanged();
                    } else if (task.getResult().isEmpty()) {
                        Log.d(TAG, building + " has no rooms or does not exist for " + email);
                    } else {
                        Log.d(TAG, "Error accessing " + building + ": ", task.getException());
                    }
                });
    }

}