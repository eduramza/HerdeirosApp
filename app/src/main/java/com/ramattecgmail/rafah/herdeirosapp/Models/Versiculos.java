package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.google.firebase.database.DatabaseReference;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;

/**
 * Created by rafah on 18/10/2017.
 */

public class Versiculos {
    //ATRIBUTOS
    private String mensagem;
    private String versiculo;
    private String dataPostagem;
    private String usuPostagem; //na versão 1 essa variavel recebia o nome do usuário que postou
    private String usuLike;

    public Versiculos(){

    }

    public void salvarVersiulo(){
        String cripto = CriptografiaBase64.criptografarVersiculo(versiculo);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
        reference.child("VERSICULOS")
                .child(cripto)
                .setValue(this);
    }

    public void curtirVersiculo(String idVersiculo, String idUsuario){
        String cripto = CriptografiaBase64.criptografarVersiculo(idVersiculo);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();

        //Salvando a curtida
        reference.child("VERSICULOS")
                .child(cripto)
                .child("LIKES")
                .child(idUsuario)
                //.child("idUsulike")
                .setValue(idUsuario);
    }

    public void descurtirVersiculo(String idVersiculo, String idUsuario){
        String cripto = CriptografiaBase64.criptografarVersiculo(idVersiculo);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();

        //Removendo curtida
        reference.child("VERSICULOS")
                .child(cripto)
                .child("LIKES")
                .child(idUsuario)
                .removeValue();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getVersiculo() {
        return versiculo;
    }

    public void setVersiculo(String versiculo) {
        this.versiculo = versiculo;
    }

    public String getDataPostagem() {
        return dataPostagem;
    }

    public void setDataPostagem(String dataPostagem) {
        this.dataPostagem = dataPostagem;
    }

    public String getUsuPostagem() {
        return usuPostagem;
    }

    public void setUsuPostagem(String usuPostagem) {
        this.usuPostagem = usuPostagem;
    }

    public String getUsuLike() {
        return usuLike;
    }

    public void setUsuLike(String usuLike) {
        this.usuLike = usuLike;
    }
}
