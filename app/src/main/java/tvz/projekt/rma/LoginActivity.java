package tvz.projekt.rma;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "FACEBOOK SUCCESS:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FACEBOOK CANCEL.");
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "FACEBOOK ERROR", error);
                updateUI(null);
            }
        });

        Button loginManual = findViewById(R.id.loginManual);
        loginManual.setOnClickListener(view -> {
            TextView _email = findViewById(R.id.emailLogIn);
            final String email = _email.getText().toString();
            TextView _password = findViewById(R.id.passwordLogIn);
            final String password = _password.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (Objects.requireNonNull(task.isSuccessful())) {
                                Log.d(TAG, "SIGN IN SUCCESS");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.w(TAG, "SIGN IN FAILURE", task.getException());
                                Toast.makeText(LoginActivity.this, "Prijava je neupješna. Molimo pokušajte ponovno",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        });

        Button register = findViewById(R.id.buttonRegister);
        register.setOnClickListener(v -> {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
        });
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "FACEBOOK ACCESS TOKEN:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "SIGN IN CREDENTIAL SUCCESS");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "SIGN IN CREDENTIAL FAILURE", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent startIntent2 = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(startIntent2);
        } else {
            Toast.makeText(LoginActivity.this, "You are not logged in.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}