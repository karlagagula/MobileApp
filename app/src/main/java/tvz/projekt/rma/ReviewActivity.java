package tvz.projekt.rma;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            throw new NullPointerException("Morate biti prijavljeni!");
        } else {

            Button checkIdAdButton = findViewById(R.id.checkIdAdButton);
            checkIdAdButton.setOnClickListener(v -> {

                TextView checkIdAd = findViewById(R.id.checkIdAd);
                String _adId = checkIdAd.getText().toString();
                Integer adId = Integer.parseInt(_adId);
                RatingBar ratingBar = findViewById(R.id.ratingBar);
                float rating = ratingBar.getRating();

                Log.d(TAG, "Id ad: " + adId);
                Log.d(TAG, "Rating bar value: " + rating);

                CollectionReference itemsRef = db.collection("ads");
                Query query = itemsRef.whereEqualTo("id", adId);
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (Objects.equals(mAuth.getCurrentUser().getEmail(), document.getString("email"))) {
                                Toast.makeText(ReviewActivity.this, "Ne možete sami sebe ocijeniti.",
                                        Toast.LENGTH_SHORT).show();
                            } else{
                                    String _dbRating = Objects.requireNonNull(document.get("rating")).toString();
                                    float dbRating = Float.parseFloat(_dbRating);
                                    String _dbSum = Objects.requireNonNull(document.get("sum")).toString();
                                    float dbSum = Float.parseFloat(_dbSum);
                                    String _counter = Objects.requireNonNull(document.get("count")).toString();
                                    Integer counter = Integer.parseInt(_counter);
                                    dbSum += rating;
                                    counter++;
                                    dbRating = (dbSum / counter);
                                    itemsRef.document(document.getId()).update("count", counter);
                                    itemsRef.document(document.getId()).update("rating", dbRating);
                                    itemsRef.document(document.getId()).update("sum", dbSum);
                                    Log.d(TAG, "success " + document.getId());
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                }
                            }
                        } else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
                });
            }
        }
    }
