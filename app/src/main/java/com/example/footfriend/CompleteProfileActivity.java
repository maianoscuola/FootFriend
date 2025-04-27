package com.example.footfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CompleteProfileActivity extends AppCompatActivity {

    private EditText nicknameEditText, ageEditText;
    private Spinner roleSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complet_profile);

        nicknameEditText = findViewById(R.id.nicknameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        saveButton = findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Save button clicked", Toast.LENGTH_SHORT).show();

            String nickname = nicknameEditText.getText().toString().trim();
            String age = ageEditText.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString();

            if (nickname.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "Errore: utente non autenticato", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = user.getUid();
            Toast.makeText(this, "User ID trovato: " + uid, Toast.LENGTH_SHORT).show();

            UserProfile userProfile = new UserProfile(nickname, age, role);


            FirebaseDatabase.getInstance("https://footfriend-bb6e3-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users")
                    .child(uid)
                    .setValue(userProfile)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Profilo salvato correttamente!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Errore durante il salvataggio: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    public static class UserProfile {
        public String nickname;
        public String age;
        public String role;

        public UserProfile() {}

        public UserProfile(String nickname, String age, String role) {
            this.nickname = nickname;
            this.age = age;
            this.role = role;
        }
    }
}
