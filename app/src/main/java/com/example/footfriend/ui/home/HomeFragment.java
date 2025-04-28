package com.example.footfriend.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private TextView textViewNome, textViewRuolo, textViewLivello;
    private ProgressBar progressBarExp;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textViewNome = root.findViewById(R.id.textViewNome);
        textViewRuolo = root.findViewById(R.id.textViewRuolo);
        textViewLivello = root.findViewById(R.id.textViewLivello);
        progressBarExp = root.findViewById(R.id.progressBarExp);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadUserData();

        return root;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nickname = documentSnapshot.getString("nickname");
                            String ruolo = documentSnapshot.getString("ruolo");
                            Long exp = documentSnapshot.getLong("exp");


                            if (nickname != null) {
                                textViewNome.setText(nickname);
                            } else {
                                textViewNome.setText("Nessun nickname");
                                Toast.makeText(getContext(), "nessun nickname", Toast.LENGTH_SHORT).show();
                            }

                            if (ruolo != null) {
                                textViewRuolo.setText(ruolo);
                            } else {
                                textViewRuolo.setText("Nessun ruolo");
                                Toast.makeText(getContext(), "nessun ruolo", Toast.LENGTH_SHORT).show();
                            }

                            if (exp != null) {
                                int livello = (int) (exp / 15);
                                int expNelLivello = (int) (exp % 15);
                                textViewLivello.setText("Livello " + livello);
                                progressBarExp.setMax(15);
                                progressBarExp.setProgress(expNelLivello);
                            } else {
                                textViewLivello.setText("Livello 0");
                                progressBarExp.setMax(15);
                                progressBarExp.setProgress(0);
                            }
                        } else {
                            Toast.makeText(getContext(), "Documento utente NON trovato!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Errore nel caricamento: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    });
        } else {
            Toast.makeText(getContext(), "Utente non loggato!", Toast.LENGTH_SHORT).show();
        }
    }
}
