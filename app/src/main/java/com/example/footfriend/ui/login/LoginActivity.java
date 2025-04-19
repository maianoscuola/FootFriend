package com.example.footfriend.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footfriend.MainActivity;
import com.example.footfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Se l'utente è già loggato, vai direttamente alla MainActivity
        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }*/

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        loginButton.setEnabled(false);

        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                loginButton.setEnabled(!email.isEmpty() && password.length() >= 6);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        usernameEditText.addTextChangedListener(loginTextWatcher);
        passwordEditText.addTextChangedListener(loginTextWatcher);

        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            loadingProgressBar.setVisibility(View.VISIBLE);

            // Prova ad effettuare il login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Login riuscito
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Se fallisce, prova a registrare il nuovo utente
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, registerTask -> {
                                        loadingProgressBar.setVisibility(View.GONE);
                                        if (registerTask.isSuccessful()) {
                                            Toast.makeText(this, "Registrazione completata!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Errore: " + registerTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        });
    }
}


