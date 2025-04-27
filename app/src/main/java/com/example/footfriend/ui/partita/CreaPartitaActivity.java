package com.example.footfriend.ui.partita;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreaPartitaActivity extends AppCompatActivity {

    private EditText campoEditText, dataOraEditText;
    private Button creaPartitaButton;
    private ProgressBar loadingProgressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creapartita);

        campoEditText = findViewById(R.id.nome_campo);
        dataOraEditText = findViewById(R.id.data_ora);
        creaPartitaButton = findViewById(R.id.crea_partita_button);
        loadingProgressBar = findViewById(R.id.loading_crea_partita);

        db = FirebaseFirestore.getInstance();

        creaPartitaButton.setEnabled(false);

        campoEditText.addTextChangedListener(textWatcher);
        dataOraEditText.addTextChangedListener(textWatcher);

        creaPartitaButton.setOnClickListener(v -> creaPartita());
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String campo = campoEditText.getText().toString().trim();
            String dataOra = dataOraEditText.getText().toString().trim();
            creaPartitaButton.setEnabled(!campo.isEmpty() && !dataOra.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void creaPartita() {
        String campo = campoEditText.getText().toString().trim();
        String dataOra = dataOraEditText.getText().toString().trim();

        if (campo.isEmpty() || dataOra.isEmpty()) {
            Toast.makeText(this, "Completa tutti i campi!", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingProgressBar.setVisibility(View.VISIBLE);

        Map<String, Object> partita = new HashMap<>();
        partita.put("campo", campo);
        partita.put("dataOra", dataOra);

        db.collection("partite")
                .add(partita)
                .addOnSuccessListener(documentReference -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(CreaPartitaActivity.this, "Partita creata!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreaPartitaActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(CreaPartitaActivity.this, "Errore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
