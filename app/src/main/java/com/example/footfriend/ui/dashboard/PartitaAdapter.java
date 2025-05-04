package com.example.footfriend.ui.dashboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.footfriend.data.model.Partita;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.footfriend.R;

import java.util.List;

public class PartitaAdapter extends RecyclerView.Adapter<PartitaAdapter.PartitaViewHolder> {

    private OnPartecipazioneClickListener listener;

    private List<Partita> partitaList;
    Partita p = new Partita();

    public PartitaAdapter(List<Partita> partitaList, OnPartecipazioneClickListener listener) {
        this.partitaList = partitaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partita, parent, false);
        return new PartitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartitaViewHolder holder, int position) {
        Partita partita = partitaList.get(position);
        holder.bind(partita);
    }

    @Override
    public int getItemCount() {
        return partitaList.size();
    }

    class PartitaViewHolder extends RecyclerView.ViewHolder {
        TextView textLuogo, textCreatore, textPosti, textOrario;
        Button buttonPartecipa;

        public PartitaViewHolder(@NonNull View itemView) {
            super(itemView);
            textLuogo = itemView.findViewById(R.id.textLuogo);
            textCreatore = itemView.findViewById(R.id.textCreatore);
            textPosti = itemView.findViewById(R.id.textPosti);
            textOrario = itemView.findViewById(R.id.textOrario);
            buttonPartecipa = itemView.findViewById(R.id.buttonPartecipa);
        }

        public void bind(Partita partita) {
            textLuogo.setText("Luogo: " + partita.getLuogo());
            textCreatore.setText("Creatore: " + partita.getCreatore());
            textPosti.setText("Posti: " + partita.getPartecipantiAttuali() + "/" + partita.getMaxPartecipanti());
            textOrario.setText("Orario: " + partita.getOrario());

            buttonPartecipa.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPartecipaClick(partita);
                }
            });
        }
    }
}
