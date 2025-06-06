package com.example.footfriend.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footfriend.ui.login.LoginActivity;
import com.example.footfriend.databinding.FragmentNotificationsBinding;
import com.example.footfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private TextView textEmail, textNickname, textEta;
    private Button buttonLogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 🟢 CORREZIONE QUI: gli ID ora corrispondono al layout
        textEmail = root.findViewById(R.id.textEmail);
        textNickname = root.findViewById(R.id.textNickname);
        textEta = root.findViewById(R.id.textEta);
        buttonLogout = root.findViewById(R.id.buttonLogout);

        loadUserData();

        buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return root;
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textEmail.setText("Email: " + user.getEmail());

            String uid = user.getUid();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nickname = documentSnapshot.getString("nickname");
                            Long eta = documentSnapshot.getLong("eta");

                            textNickname.setText("Nickname: " + (nickname != null ? nickname : "N/D"));
                            textEta.setText("Età: " + (eta != null ? eta : "N/D"));
                        } else {
                            Toast.makeText(getContext(), "Dati utente non trovati.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Errore caricamento dati: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

