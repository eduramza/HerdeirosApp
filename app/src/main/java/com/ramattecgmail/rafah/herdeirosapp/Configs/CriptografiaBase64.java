package com.ramattecgmail.rafah.herdeirosapp.Configs;

import android.util.Base64;

/**
 * Created by rafah on 05/10/2017.
 */

public class CriptografiaBase64 {

    //METODOS DE CODIFICAÇÃO
    public static String criptografarCardapio(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "-cardapio");
    }

    public static String criptografarTelefone(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "-user");
    }

    public static String criptografarHino(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "-hino");
    }

    public static String criptografarData(String data){
        return Base64.encodeToString(data.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "-atualizacao");
    }

    public static String criptografarVersiculo(String verso){
        return Base64.encodeToString(verso.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String criptografarCronograma(String dia){
        return Base64.encodeToString(dia.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "-cronog");
    }

}
