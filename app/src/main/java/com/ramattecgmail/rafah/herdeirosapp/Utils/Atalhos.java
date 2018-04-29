package com.ramattecgmail.rafah.herdeirosapp.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ramattecgmail.rafah.herdeirosapp.Activitys.CalendarioActivity;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Models.Galeria;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafah on 15/10/2017.
 */

public final class Atalhos {
    public static final String DEEP_LINK = "https://rkfn7.app.goo.gl/ZjWo";
    public static final String YOUTUBE_KEY = "AIzaSyBnDSOQYv3G-r8Nz55VyLEw2AQo9nysVW4";
    public static final String VAGALUME_KEY = "1ffb35c8a44963c599d4ace6f7ae52fc";

    public Atalhos(){

    }

    public static String getAno(){
        //Pegando o ano atual
        Date ano = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy");
        String anoRetiro = format.format(ano);

        return anoRetiro;
    }

    public static String getHoje(){
        //Pegando data atual
        Date ano = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String anoRetiro = format.format(ano);

        return anoRetiro;
    }

    public static Boolean verificarLider(Context context){
        SharedPreferencias user = new SharedPreferencias(context);
        if (user.getCHAVE_NIVEL().equals("Lider")){
            return true;
        } else {
            return false;
        }
    }

    public static Boolean verificarRegente(Context context){
        SharedPreferencias user = new SharedPreferencias(context);
        if (user.getCHAVE_NIVEL().equals("Lider") || user.getCHAVE_NIVEL().equals("Regente")){
           return true;
        } else {
            return false;
        }
    }

    public static void acessoNegado(Context context){
        //CONFIGURAÇÃO DO ALERT DIALOG DENTRO DO EVENTO DE CLICK
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Acesso negado");
        alert.setMessage("Desculpe, você não tem permissão para adicionar essas informações");
        alert.setCancelable(false);

        alert.setNeutralButton("Entendi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //apenas fecha a caixa de dialogo
            }
        });
        //materializando o alert
        alert.create();
        alert.show();
    }

    public static void compartilhaDialog(final String evento, final Context context){
        //CONFIGURAÇÃO DO ALERT DIALOG DENTRO DO EVENTO DE CLICK
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Compartilhar Evento");
        alert.setMessage("Gostaria de compartilhar este Evento agora?");
        alert.setCancelable(false);

        alert.setPositiveButton("Compartilhar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent sendIntent = new Intent();
                String msg = evento + "\n não perca! -"+DEEP_LINK;
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);

            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //materializando o alert
        alert.create();
        alert.show();
    }

    //Validando se o celular esta conectado para salvar dados no firebase
    public static boolean verificarConexao(Context context){
        ConnectivityManager connectividade = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectividade.getActiveNetworkInfo() != null
                && connectividade.getActiveNetworkInfo().isAvailable()
                && connectividade.getActiveNetworkInfo().isConnected()){
            //Celular conectado
            return true;
        }
        return false;
    }
}
