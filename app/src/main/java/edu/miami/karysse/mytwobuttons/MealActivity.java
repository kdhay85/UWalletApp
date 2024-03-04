package edu.miami.karysse.mytwobuttons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MealActivity extends AppCompatActivity {

    private static final String TAG = "MealActivity";

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_page);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Get the currently logged-in user
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Get the user's cane_id
            String cane_id = user.getUid();

            // Get a reference to the "student_and_meal" collection
            DocumentReference docRef = db.collection("student_and_meal").document(cane_id);

            // Get the document
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the meal_plan_id from the document
                        String mealPlanId = document.getString("meal_plan_id");

                        // Get a reference to the "meal_plans" collection
                        DocumentReference mealPlanRef = db.collection("meal_plans").document(mealPlanId);

                        // Get the meal plan document
                        mealPlanRef.get().addOnCompleteListener(mealPlanTask -> {
                            if (mealPlanTask.isSuccessful()) {
                                DocumentSnapshot mealPlanDocument = mealPlanTask.getResult();
                                if (mealPlanDocument.exists()) {
                                    // Get the meal plan data
                                    String mealSwipes = mealPlanDocument.getString("swipes");
                                    String guestSwipes = mealPlanDocument.getString("guest_swipes");
                                    String diningDollars = mealPlanDocument.getString("dining_dollars");

                                    // Update UI with the meal plan data
                                    TextView mealSwipesValueTextView = findViewById(R.id.mealSwipesValueTextView);
                                    mealSwipesValueTextView.setText(mealSwipes);

                                    TextView guestSwipesValueTextView = findViewById(R.id.guestSwipesValueTextView);
                                    guestSwipesValueTextView.setText(guestSwipes);

                                    TextView diningDollarsValueTextView = findViewById(R.id.diningDollarsValueTextView);
                                    diningDollarsValueTextView.setText(diningDollars);
                                } else {
                                    Log.d(TAG, "No such meal plan document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", mealPlanTask.getException());
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

