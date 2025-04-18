package com.example.footfriend.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footfriend.MainActivity;
import com.example.footfriend.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import android.text.TextWatcher;
import android.text.Editable;
import com.google.android.gms.common.api.ApiException;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, googleSignInButton;
    private ProgressBar loadingProgressBar;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String CLIENT_ID_WEB = "101685718122-j38cqhocqgthl2l83hq0aa5ptohmqo72.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 9001; // Costante per il risultato del login Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(this, "Creazione dell'activity avvenuta con successo.", Toast.LENGTH_SHORT).show();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        googleSignInButton = findViewById(R.id.google_sign_in_button); // Bottone Google
        loadingProgressBar = findViewById(R.id.loading);

        googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton = findViewById(R.id.google_sign_in_button);

        if (findViewById(R.id.google_sign_in_button) == null) {
            Toast.makeText(this, "Errore: il bottone Google Sign-In non è stato trovato!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bottone Google Sign-In trovato con successo.", Toast.LENGTH_SHORT).show();
        }


        // Configura Firebase Authentication e Google Sign-In
        mAuth = FirebaseAuth.getInstance();

        // Configurazione Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID_WEB)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setEnabled(true); // Abilita il bottone


        // Abilita il bottone di login quando la username e la password sono validi
        loginButton.setEnabled(false);
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                loginButton.setEnabled(!username.isEmpty() && password.length() > 5);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                loginButton.setEnabled(!username.isEmpty() && password.length() > 5);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Gestione del click sul bottone di login (email/password)
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.equals("user@example.com") && password.equals("password123")) {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Login riuscito!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Credenziali non valide", Toast.LENGTH_SHORT).show();
            }
        });

        // Gestione del click sul bottone di Google Sign-In
        googleSignInButton.setOnClickListener((View v) -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    // Gestisce il risultato del login con Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Login fallito con Google", Toast.LENGTH_SHORT).show();
            }
        }
        loadingProgressBar.setVisibility(View.GONE);  // Nasconde il progress bar

    }

    // Authentica l'utente con Firebase usando il token di Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            loadingProgressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "Benvenuto, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Autenticazione fallita", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

