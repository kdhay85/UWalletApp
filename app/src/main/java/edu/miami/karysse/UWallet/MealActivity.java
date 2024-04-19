package edu.miami.karysse.UWallet;

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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private DocumentReference emailDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_page);

        // Get the currently logged-in user
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Get the user's cane_id
            String email = user.getEmail();

            // Get a reference to the "student_and_meal" collection
            emailDoc = db.collection("users").document(email);

            // Get the document
            // Get the document
            emailDoc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the meal_plan document within the person subcollection of the user's document
                        DocumentReference mealPlanRef = emailDoc.collection("person").document("meal_plan");

                        // Get the meal plan document
                        mealPlanRef.get().addOnCompleteListener(mealPlanTask -> {
                            if (mealPlanTask.isSuccessful()) {
                                DocumentSnapshot mealPlanDocument = mealPlanTask.getResult();
                                if (mealPlanDocument.exists()) {
                                    // Get the meal plan data

                                    long mealSwipes = mealPlanDocument.getLong("swipes");
                                    long guestSwipes = mealPlanDocument.getLong("guest_swipes");
                                    long diningDollars = mealPlanDocument.getLong("dining_dollars");
                                    String mealPlanName = mealPlanDocument.getString("meal_plan_name");

                                    // Convert to strings
                                    String mealSwipesString = String.valueOf(mealSwipes);
                                    String guestSwipesString = String.valueOf(guestSwipes);
                                    String diningDollarsString = String.valueOf(diningDollars);

                                    TextView mealSwipesValueTextView = findViewById(R.id.mealSwipesValueTextView);
                                    mealSwipesValueTextView.setText(mealSwipesString);

                                    TextView guestSwipesValueTextView = findViewById(R.id.guestSwipesValueTextView);
                                    guestSwipesValueTextView.setText(guestSwipesString);

                                    TextView diningDollarsValueTextView = findViewById(R.id.diningDollarsValueTextView);
                                    diningDollarsValueTextView.setText(diningDollarsString);
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