package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.google.firebase.database.DatabaseReference;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rafah on 06/10/2017.
 */

public class Hinos {
    //ATRIBUTOS
    private String numero;
    private String titulo;
    private String cantor;
    private String letra;
    private String url;

    public Hinos(){

    }

    public void salvarHinos(){
        //Gerando chave criptografada
        String cripto = CriptografiaBase64.criptografarHino(numero);

        //Salvando os dados no firebase
        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
        reference.child("MOCIDADE")
                .child("HINOS")
                .child(cripto)
                .setValue(this);
    }

    public void atualizarHinos(){
        //Gerando chave criptografada
        String cripto = CriptografiaBase64.criptografarHino(numero);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference()
                .child("MOCIDADE")
                .child("HINOS")
                .child(cripto);

        //CRIANDO O HASHMAP QUE ARMAZENA OS VALORES
        Map<String, Object> map = new HashMap<>();
        map.put("numero", numero);
        map.put("titulo", titulo);
        map.put("letra", letra);
        map.put("cantor", cantor);
        map.put("url", url);

        reference.updateChildren(map);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCantor() {
        return cantor;
    }

    public void setCantor(String cantor) {
        this.cantor = cantor;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
