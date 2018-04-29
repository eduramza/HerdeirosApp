package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.bumptech.glide.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

/**
 * Created by rafah on 15/10/2017.
 */

public class Cronograma {
    //Atributos
    private String dia;
    private String horaInicio;
    private String horaFim;
    private String atividade;
    private String categoria; //Sendo elas, religiosa, gincanas, lazer

    public Cronograma(){

    }

    public void salvarCronograma(){
        //gerando o nó
        String cripto = CriptografiaBase64.criptografarCronograma(dia);

        //Salvando os dados
        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
        reference.child("RETIRO")
                .child(Atalhos.getAno())
                .child("CRONOGRAMA")
                .child(dia)
                .push()
                .setValue(this);

    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
