package tvz.projekt.rma;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import database.Ad;
import database.AdsList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private ListView listViewAds;
    private List<Ad> adList;
    private FirebaseAuth mAuth;
    private ConstraintLayout homeLayout;
    private String location;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        listViewAds = findViewById(R.id.listViewAds);
        adList = new ArrayList<>();
        homeLayout = (ConstraintLayout) findViewById(R.id.home);

        mAuth = FirebaseAuth.getInstance();

        Button submitAd_home = findViewById(R.id.button_submitAd_home);
        Button logOut = findViewById(R.id.buttonLogOut);
        Button myAdsButton = findViewById(R.id.myAdsButton);
        Button reviewAd = findViewById(R.id.reviewAdButton);
        Button filter = findViewById(R.id.filterButton);

        submitAd_home.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AdFormActivity.class)));

        logOut.setOnClickListener(v -> {
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        myAdsButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MyAdsActivity.class)));

        reviewAd.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ReviewActivity.class)));

        filter.setOnClickListener(view -> {
            LayoutInflater layoutInflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            @SuppressLint("InflateParams")
            View customView = layoutInflater.inflate(R.layout.filter, null);

            Button close = customView.findViewById(R.id.closeButton);
            Button find = customView.findViewById(R.id.findButton);

            PopupWindow popupWindow = new PopupWindow(customView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(homeLayout, Gravity.CENTER, 0, 0);

            addListenerOnSpinnerSubject(customView);
            addListenerOnSpinnerLocation(customView);

            find.setOnClickListener(view1 -> {
                if (subject.isEmpty() || location.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Molimo odaberite barem jedan filter", Toast.LENGTH_LONG).show();
                } else {
                    fetchDataFilter();
                    popupWindow.dismiss();
                }
            });
            close.setOnClickListener(v -> popupWindow.dismiss());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        adList.clear();
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult()).getDocuments()) {
                            fetchData(document);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }


    public void fetchDataFilter() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ads")
                .whereEqualTo("subject", subject)
                .whereEqualTo("location", location)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                            System.out.println("EMPTY!");
                            LayoutInflater layoutInflater = (LayoutInflater)
                                    HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = layoutInflater.inflate(R.layout.no_result, null);
                            Button close = customView.findViewById(R.id.closeNoResult);
                            PopupWindow popupWindow = new PopupWindow(
                                    customView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                            popupWindow.showAtLocation(homeLayout, Gravity.CENTER, 0, 0);
                            close.setOnClickListener(view -> popupWindow.dismiss());
                        } else {
                            adList.clear();
                            int numberOfResult = 0;
                            for (DocumentSnapshot document : Objects.requireNonNull(task.getResult()).getDocuments()) {
                                fetchData(document);
                                numberOfResult++;
                            }
                            Toast.makeText(HomeActivity.this, "Pronađeno je " + numberOfResult + " rezultat/a.",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void fetchData(DocumentSnapshot document) {
        String _id = Objects.requireNonNull(document.get("id")).toString();
        int id = Integer.parseInt(_id);
        String name = Objects.requireNonNull(document.get("name")).toString();
        String subject1 = Objects.requireNonNull(document.get("subject")).toString();
        String location1 = Objects.requireNonNull(document.get("location")).toString();
        String priceString = Objects.requireNonNull(document.get("price")).toString();
        Integer price = Integer.parseInt(priceString);
        String phone = Objects.requireNonNull(document.get("phone")).toString();
        String arrival = Objects.requireNonNull(document.get("arrival")).toString();
        String date = Objects.requireNonNull(document.get("date")).toString();
        String email = Objects.requireNonNull(document.get("email")).toString();

        if (document.get("rating") != null) {
            String _rating = Objects.requireNonNull(document.get("rating")).toString();
            float rating = Float.parseFloat(_rating);
            Ad ad = new Ad(id, rating, name, subject1, price, location1, email, phone, arrival, date);
            adList.add(ad);

            AdsList adapter = new AdsList(HomeActivity.this, adList);
            listViewAds.setAdapter(adapter);
        } else {
            Ad ad = new Ad(id, name, subject1, price, location1, email, phone, arrival, date);
            adList.add(ad);

            AdsList adapter = new AdsList(HomeActivity.this, adList);
            listViewAds.setAdapter(adapter);
        }
    }

    public void addListenerOnSpinnerSubject(View view) {
        Spinner spinnerSubject = view.findViewById(R.id.filterSubject);
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addListenerOnSpinnerLocation(View view) {
        Spinner spinnerLocation = view.findViewById(R.id.filterLocation);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}