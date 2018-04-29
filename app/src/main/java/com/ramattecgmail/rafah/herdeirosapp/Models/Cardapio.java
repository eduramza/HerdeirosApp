package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.google.firebase.database.DatabaseReference;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafah on 03/10/2017.
 */

public class Cardapio {
    //ATRIBUTOS
    private String refeicao;
    private String ingredientes;
    private String data;
    private String ordem;

    public Cardapio(){

    }

    public void salvarCardapio(){

        //Pegando o ano atual
        Date ano = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy");
        String anoRetiro = format.format(ano);

        String day = data.substring(0,2);

        //Criptografando o nó da refeição
        String cripto = CriptografiaBase64.criptografarCardapio(refeicao);

        //pegando o ano atual para salvar no nó retiro
        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
        reference.child("RETIRO")
                .child(anoRetiro)
                .child("CARDAPIO")
                .child(day)
                .child(cripto)
                .setValue(this);

    }

    public String getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(String refeicao) {
        this.refeicao = refeicao;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

}
