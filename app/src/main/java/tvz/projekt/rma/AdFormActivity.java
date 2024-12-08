package tvz.projekt.rma;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Random;

import database.Ad;


public class AdFormActivity extends AppCompatActivity {

    private static final String TAG = "AdFormActivity";

    private String name;
    private String email;
    private String location;
    private String subject;
    private String price;
    private String phone;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_form);

        addListenerOnSpinnerSubject();
        addListenerOnSpinnerLocation();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            name = currentUser.getDisplayName();
            email = currentUser.getEmail();
        } else {
            throw new NullPointerException("Ne postoji korisnik!");
        }
        onSubmit();
    }

    public void addListenerOnSpinnerSubject() {
        Spinner spinnerSubject = findViewById(R.id.spinnerSubject);
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

    public void addListenerOnSpinnerLocation() {
        Spinner spinnerLocation = findViewById(R.id.spinnerLocation);
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

    public void onSubmit() {

        Button submitAd = findViewById(R.id.button_submitAd);
        submitAd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                EditText priceText = findViewById(R.id.input_price);
                EditText phoneText = findViewById(R.id.input_phone);
                RadioGroup rgText = findViewById(R.id.radioGroup);

                int checked = rgText.getCheckedRadioButtonId();
                phone = phoneText.getText().toString();
                price = priceText.getText().toString();
                Random random = new Random();
                id = random.nextInt(1000000);

                String arrival = "";
                if (checked == -1 || location.isEmpty() || subject.isEmpty() || phone.isEmpty() || price.isEmpty()) {
                    Toast.makeText(AdFormActivity.this, "Molimo popunite sve podatke.",
                            Toast.LENGTH_LONG).show();
                } else {
                    RadioButton rbText = findViewById(checked);
                    arrival = rbText.getText().toString();
                    Integer priceInt = Integer.parseInt(price);

                    final Ad ad = new Ad(id,0f, name, subject, priceInt, location, email,
                            phone, arrival, LocalDate.now().toString(), 0);
                    db.collection("ads")
                            .add(ad)
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Intent intent = new Intent(getApplicationContext(), OnSuccessActivity.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                }
            }
        });
    }
}
