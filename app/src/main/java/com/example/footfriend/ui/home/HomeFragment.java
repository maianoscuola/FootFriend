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

    private TextView textViewNome, textViewRuolo;
    private ProgressBar progressBarExp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textViewNome = root.findViewById(R.id.textViewNome);
        textViewRuolo = root.findViewById(R.id.textViewRuolo);
        progressBarExp = root.findViewById(R.id.progressBarExp);

        loadUserData();

        return root;
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nickname = snapshot.child("nickname").getValue(String.class);
                        String ruolo = snapshot.child("ruolo").getValue(String.class);
                        Long livello = snapshot.child("livello").getValue(Long.class);

                        textViewNome.setText(nickname != null ? nickname : "Nessun nickname");
                        textViewRuolo.setText(ruolo != null ? ruolo : "Nessun ruolo");
                        progressBarExp.setProgress(livello != null ? livello.intValue() : 0);
                    } else {
                        Toast.makeText(getContext(), "Utente non trovato", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getContext(), "Errore: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Utente non loggato!", Toast.LENGTH_SHORT).show();
        }
    }
}
