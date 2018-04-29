package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;

/**
 * Created by rafah on 30/09/2017.
 */

public class Eventos {

    private String id;
    private String titulo;
    private String dataInicio;
    private String dataFim;
    private String descricao;

    public Eventos(){

    }

    //METODO DE SALVAR
    public void salvarEvento(){
        /******************* ATENÇÃO ******************/
        /**
         * MESMO QUE O USUARIO ESTEJA OFFLINE ELE PODE CRIAR UM EVENTO E QUANDO ELE ESTIVER COM ACESSO NOVAMENTE
         * O EVENTO SERÁ ENVIADO PARA O FIREBASE!!!
         */
         DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
         reference.child("EVENTOS").child(id).setValue(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
