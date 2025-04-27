package com.example.footfriend.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboardprofilo extends Fragment{


        private FragmentProfileBinding binding;
        private FirebaseAuth mAuth;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            binding = binding.inflate(inflater, container, false);
            View root = binding.getRoot();

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {
                binding.textEmail.setText("Email: " + user.getEmail());
                binding.textNickname.setText("Nickname: " + (user.getDisplayName() != null ? user.getDisplayName() : "Non impostato"));
                binding.textRuolo.setText("Ruolo: Attaccante"); // 👈 puoi cambiarlo dinamicamente se vuoi
                binding.textEta.setText("Età: 20 anni"); // 👈 anche questa dinamica volendo
            }

            binding.buttonLogout.setOnClickListener(v -> {
                mAuth.signOut();
                Toast.makeText(getContext(), "Logout effettuato", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                requireActivity().finish();
            });

            return root;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }
}
