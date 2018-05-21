package com.ramattecgmail.rafah.herdeirosapp.Configs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

/**
 * Created by rafah on 02/10/2017.
 */

public class    SharedPreferencias {
    //ATRIBUTOS
    private Context context;
    private android.content.SharedPreferences preferences;
    private final String ARQUIVO = "HERDEIROS.PREFERENCES";
    private final int MODE = 0;
    private android.content.SharedPreferences.Editor editor;

    private final String CHAVE_ID = "id";
    private final String CHAVE_GRUPO = "grupo";
    private final String CHAVE_NIVEL = "nivel";
    private final String CHAVE_NOME = "nome";
    private final String CHAVE_EMAIL = "email";
    private final String CHAVE_APELIDO = "apelido";

    //para salvar o hino selecionado
    private final String CHAVE_NUM = "numero";
    private final String CHAVE_TITULO = "titulo";
    private final String CHAVE_CANTOR = "cantor";
    private final String CHAVE_URL = "url";

    public SharedPreferencias(Context c){
        context = c;
        preferences = context.getSharedPreferences(ARQUIVO, MODE);
        editor = preferences.edit();
    }

    //SALVANDO DADOS DO USUÁRIO
    public void salvarUsuarioPreferences(String id, String nome, String grupo, String nivel, String email, String apelido){
        editor.putString(CHAVE_ID, id);
        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_GRUPO, grupo);
        editor.putString(CHAVE_NIVEL, nivel);
        editor.putString(CHAVE_EMAIL, email);
        editor.putString(CHAVE_APELIDO, apelido);
        editor.commit();
    }

    public void salvarHinoPreferences(String numero, String titulo, String cantor, String url){
        editor.putString(CHAVE_NUM, numero);
        editor.putString(CHAVE_TITULO, titulo);
        editor.putString(CHAVE_CANTOR, cantor);
        editor.putString(CHAVE_URL, url);
        editor.commit();
    }

    //RECUPERANDO OS DADOS DO USUÁRIO
    public String getCHAVE_NOME(){
        return preferences.getString(CHAVE_NOME, null);
    }

    public String getCHAVE_APELIDO(){
        return preferences.getString(CHAVE_APELIDO, null);
    }

    public String getCHAVE_ID(){
        return preferences.getString(CHAVE_ID, null);
    }

    public String getCHAVE_NIVEL(){
        return preferences.getString(CHAVE_NIVEL, null);
    }

    public String getCHAVE_NUM() {
        return preferences.getString(CHAVE_NUM, null);
    }

    public String getCHAVE_TITULO() {
        return preferences.getString(CHAVE_TITULO, null);
    }

    public String getCHAVE_CANTOR() {
        return preferences.getString(CHAVE_CANTOR, null);
    }

    public String getCHAVE_URL() {
        return preferences.getString(CHAVE_URL, null);
    }

    public String getCHAVE_EMAIL() {
        return preferences.getString(CHAVE_EMAIL, null);
    }

}
