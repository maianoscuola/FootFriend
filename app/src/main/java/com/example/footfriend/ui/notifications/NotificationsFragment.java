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

import com.example.footfriend.R;
import com.example.footfriend.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class NotificationsFragment extends Fragment {

    private TextView textViewEmail, textViewNickname, textViewEta;
    private Button buttonLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        textViewEmail = root.findViewById(R.id.textViewEmail);
        textViewNickname = root.findViewById(R.id.textViewNickname);
        textViewEta = root.findViewById(R.id.textViewEta);
        buttonLogout = root.findViewById(R.id.buttonLogout);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            textViewEmail.setText(user.getEmail());

            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nickname = snapshot.child("nickname").getValue(String.class);
                        Long eta = snapshot.child("eta").getValue(Long.class);

                        textViewNickname.setText(nickname != null ? nickname : "Nessun nickname");
                        textViewEta.setText(eta != null ? eta + " anni" : "Età non disponibile");
                    } else {
                        Toast.makeText(getContext(), "Dati utente non trovati", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getContext(), "Errore: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return root;
    }
}
