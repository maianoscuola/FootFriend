package com.example.footfriend.ui.create;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.footfriend.R;
import com.example.footfriend.data.model.Partita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePartitaActivity extends AppCompatActivity {

    private EditText editLuogo, editOrario;
    private Spinner spinnerPartecipanti;
    private Button btnConferma;

    private DatabaseReference databasePartite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_partita);

        editLuogo = findViewById(R.id.editLuogo);
        editOrario = findViewById(R.id.editOrario);
        spinnerPartecipanti = findViewById(R.id.spinnerPartecipanti);
        btnConferma = findViewById(R.id.btnConferma);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.numero_partecipanti,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPartecipanti.setAdapter(adapter);

        databasePartite = FirebaseDatabase.getInstance().getReference("partite");

        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creaPartita();
            }
        });
    }

    private void creaPartita() {
        String luogo = editLuogo.getText().toString().trim();
        String orario = editOrario.getText().toString().trim();
        String partecipantiStr = spinnerPartecipanti.getSelectedItem().toString();

        if (luogo.isEmpty() || orario.isEmpty()) {
            Toast.makeText(this, "Inserisci tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        int maxPartecipanti = Integer.parseInt(partecipantiStr);
        int postiRimanenti = maxPartecipanti;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Utente non autenticato", Toast.LENGTH_SHORT).show();
            return;
        }

        String creatore = user.getDisplayName(); // o da Firestore
        if (creatore == null || creatore.isEmpty()) {
            creatore = "Anonimo";
        }

        Partita partita = new Partita(luogo, creatore, maxPartecipanti, postiRimanenti, orario);

        // Usa push() per un ID automatico
        databasePartite.push().setValue(partita)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Partita creata!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Errore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}