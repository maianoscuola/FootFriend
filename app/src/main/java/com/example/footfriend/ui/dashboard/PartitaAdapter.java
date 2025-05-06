package com.example.footfriend.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footfriend.R;
import com.example.footfriend.data.model.Partita;

import java.util.List;

public class PartitaAdapter extends RecyclerView.Adapter<PartitaAdapter.PartitaViewHolder> {

    private List<Partita> listaPartite;
    private OnPartecipazioneClickListener listener;

    public PartitaAdapter(List<Partita> listaPartite, OnPartecipazioneClickListener listener) {
        this.listaPartite = listaPartite;
        this.listener = listener;
    }

    public interface OnPartecipazioneClickListener {
        void onPartecipaClick(Partita partita);
    }

    @NonNull
    @Override
    public PartitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partita, parent, false);
        return new PartitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartitaViewHolder holder, int position) {
        Partita partita = listaPartite.get(position);

        holder.textLuogo.setText(partita.getLuogo());
        holder.textCreatore.setText("Creatore: " + partita.getCreatore());
        holder.textMaxPartecipanti.setText("Max: " + partita.getMaxPartecipanti());
        holder.textPostiRimanenti.setText("Posti rimasti: " + partita.getPostiRimanenti());
        holder.textOrario.setText("Orario: " + partita.getOrario());

        holder.btnPartecipa.setOnClickListener(v -> listener.onPartecipaClick(partita));
    }

    @Override
    public int getItemCount() {
        return listaPartite.size();
    }

    public static class PartitaViewHolder extends RecyclerView.ViewHolder {
        TextView textLuogo, textCreatore, textMaxPartecipanti, textPostiRimanenti, textOrario;
        Button btnPartecipa;

        public PartitaViewHolder(@NonNull View itemView) {
            super(itemView);
            textLuogo = itemView.findViewById(R.id.textLuogo);
            textCreatore = itemView.findViewById(R.id.textCreatore);
            textMaxPartecipanti = itemView.findViewById(R.id.textMaxPartecipanti);
            textPostiRimanenti = itemView.findViewById(R.id.textPosti);
            textOrario = itemView.findViewById(R.id.textOrario);
            btnPartecipa = itemView.findViewById(R.id.buttonPartecipa);
        }
    }
}

