package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.google.firebase.database.DatabaseReference;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafah on 10/10/2017.
 */

public class Placar {
    //ATRIBUTOS
    private String atualizacao;
    private String placarEmanuel;
    private String placarGideoes;

    public Placar(){

    }

    public void salvarPlacar(){
        //Pegando o ano atual
        Date ano = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy");
        String anoRetiro = format.format(ano);

        String cripto = CriptografiaBase64.criptografarData(atualizacao);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
        reference.child("RETIRO")
                .child(anoRetiro)
                .child("PLACAR")
                .child(atualizacao + cripto)
                .setValue(this);
    }

    public String getAtualizacao() {
        return atualizacao;
    }

    public void setAtualizacao(String atualizacao) {
        this.atualizacao = atualizacao;
    }

    public String getPlacarEmanuel() {
        return placarEmanuel;
    }

    public void setPlacarEmanuel(String placarA) {
        this.placarEmanuel = placarA;
    }

    public String getPlacarGideoes() {
        return placarGideoes;
    }

    public void setPlacarGideoes(String placarB) {
        this.placarGideoes = placarB;
    }
}
