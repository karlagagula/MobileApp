package tvz.projekt.rma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class OnSuccessActivity extends AppCompatActivity {

    private static final String TAG = "OnSuccessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        Button backHome = findViewById(R.id.buttonBackToHome);
        backHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });

        Button myAds = findViewById(R.id.myAds);
        myAds.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyAdsActivity.class);
            startActivity(intent);
        });

        }
    }

