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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView textViewNome, textViewRuolo, textViewLivello;
    private ProgressBar progressBarExp;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textViewNome = root.findViewById(R.id.textViewNome);
        textViewRuolo = root.findViewById(R.id.textViewRuolo);
        textViewLivello = root.findViewById(R.id.textViewLivello);
        progressBarExp = root.findViewById(R.id.progressBarExp);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(userId);
            loadUserData();
        } else {
            Toast.makeText(getContext(), "Utente non loggato!", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private void loadUserData() {
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nickname = dataSnapshot.child("nickname").getValue(String.class);
                    String ruolo = dataSnapshot.child("ruolo").getValue(String.class);
                    Long exp = dataSnapshot.child("exp").getValue(Long.class);

                    if (nickname != null) {
                        textViewNome.setText(nickname);
                    } else {
                        textViewNome.setText("Nessun nickname");
                        Toast.makeText(getContext(), "Nessun nickname", Toast.LENGTH_SHORT).show();
                    }

                    if (ruolo != null) {
                        textViewRuolo.setText(ruolo);
                    } else {
                        textViewRuolo.setText("Nessun ruolo");
                        Toast.makeText(getContext(), "Nessun ruolo", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Documento utente NON trovato nel database!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Errore nel caricamento: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
