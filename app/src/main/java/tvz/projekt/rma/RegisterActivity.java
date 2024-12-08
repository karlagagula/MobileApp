package tvz.projekt.rma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();

        Button register = findViewById(R.id.registerButton);

        register.setOnClickListener(view -> {
            EditText _name = findViewById(R.id.nameSurnameRegister);
            final String name = _name.getText().toString();

            EditText _email = findViewById(R.id.emailRegister);
            final String email = _email.getText().toString();

            EditText _password = findViewById(R.id.passwordRegister);
            final String password = _password.getText().toString();

            EditText _passwordRepeat = findViewById(R.id.repeatPasswordRegister);
            final String passwordRepeat = _passwordRepeat.getText().toString();

            if (!password.equals(passwordRepeat)) {
                Toast.makeText(RegisterActivity.this, "Lozinke nisu jednake.", Toast.LENGTH_SHORT).show();
            }
            else if(password.length() < 5){
                Toast.makeText(RegisterActivity.this, "Lozinka mora sadržavati više od 5 znakova", Toast.LENGTH_LONG).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                assert fbUser != null;
                                fbUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "User profile updated. User name is: " +
                                                        fbUser.getDisplayName());
                                                Intent startIntent2 = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(startIntent2);
                                            }
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "CREATE USER FAILURE", task.getException());
                                Toast.makeText(RegisterActivity.this,
                                        "Registracija neuspješna. Molimo pokušajte ponovno.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
