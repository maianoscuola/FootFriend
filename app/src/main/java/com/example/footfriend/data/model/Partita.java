package com.example.footfriend.data.model;

public class Partita {
    private String luogo;
    private String creatore;
    private int maxPartecipanti;
    private int postiRimanenti;
    private int partecipantiAttuali;
    private String orario;

    public Partita() {
        // Necessario per Firebase
    }

    public Partita(String luogo, String creatore, int maxPartecipanti, int postiRimanenti, String orario) {
        this.luogo = luogo;
        this.creatore = creatore;
        this.maxPartecipanti = maxPartecipanti;
        this.postiRimanenti = postiRimanenti;
        this.orario = orario;
    }

    // Getter e Setter
    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getCreatore() {
        return creatore;
    }

    public void setCreatore(String creatore) {
        this.creatore = creatore;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }
    public int getPartecipantiAttuali() {
        partecipantiAttuali = maxPartecipanti - postiRimanenti;
        return partecipantiAttuali;
    }
    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public int getPostiRimanenti() {
        return postiRimanenti;
    }

    public void setPostiRimanenti(int postiRimanenti) {
        this.postiRimanenti = postiRimanenti;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }
}