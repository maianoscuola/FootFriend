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

import com.example.footfriend.CompleteProfileActivity;
import com.example.footfriend.MainActivity;
import com.example.footfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkIfProfileIsComplete(user.getUid());
                            }
                        } else {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, registerTask -> {
                                        loadingProgressBar.setVisibility(View.GONE);
                                        if (registerTask.isSuccessful()) {
                                            Toast.makeText(this, "Registrazione completata!", Toast.LENGTH_SHORT).show();
                                            FirebaseUser newUser = mAuth.getCurrentUser();
                                            if (newUser != null) {
                                                startActivity(new Intent(LoginActivity.this, CompleteProfileActivity.class));
                                                finish();
                                            }
                                        } else {
                                            Toast.makeText(this, "Errore: " + registerTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        });
    }

    private void checkIfProfileIsComplete(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    if (documentSnapshot.exists() &&
                            documentSnapshot.contains("nickname") &&
                            documentSnapshot.contains("ruolo") &&
                            documentSnapshot.contains("eta")) {

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, CompleteProfileActivity.class));
                    }
                    finish();
                })
                .addOnFailureListener(e -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Errore nel controllo del profilo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}



