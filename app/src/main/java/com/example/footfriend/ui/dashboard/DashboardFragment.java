package com.example.footfriend.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.footfriend.R;
import com.example.footfriend.data.model.Partita;
import com.example.footfriend.ui.create.CreatePartitaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements PartitaAdapter.OnPartecipazioneClickListener {

    private RecyclerView recyclerView;
    private Button btnCreaPartita;
    private List<Partita> listaPartite = new ArrayList<>();
    private PartitaAdapter adapter;
    private DatabaseReference databasePartite;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewPartite);
        btnCreaPartita = root.findViewById(R.id.btnCreaPartita);

        adapter = new PartitaAdapter(listaPartite, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnCreaPartita.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreatePartitaActivity.class);
            startActivity(intent);
        });

        databasePartite = FirebaseDatabase.getInstance().getReference("partite");
        caricaPartite();

        return root;
    }

    private void caricaPartite() {
        databasePartite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPartite.clear();
                for (DataSnapshot partitaSnapshot : snapshot.getChildren()) {
                    Partita partita = partitaSnapshot.getValue(Partita.class);
                    if (partita != null) {
                        listaPartite.add(partita);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Errore nel caricamento delle partite", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPartecipaClick(Partita partita) {
        Toast.makeText(getContext(), "Hai partecipato alla partita a " + partita.getLuogo(), Toast.LENGTH_SHORT).show();
        // Aggiungi qui: aggiornamento XP, decremento posti, ecc.
    }
}
