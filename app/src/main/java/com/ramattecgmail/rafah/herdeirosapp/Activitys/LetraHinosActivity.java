package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;
import com.ramattecgmail.rafah.herdeirosapp.Configs.JSONParser;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Models.Hinos;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;
import com.ramattecgmail.rafah.herdeirosapp.YoutubeLibs.YouTubeFailureRecoveryActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class LetraHinosActivity extends YouTubeFailureRecoveryActivity  {
    //ATRIBUTOS
    private YouTubePlayerView video;
    private TextView tvHino, tvCantor, tvLetra;
    private BottomNavigationView bottomNav;

    //Strings do shared
    private String artista;
    private String musica;
    private String numero;
    private String url;
    private String idHino;

    private SharedPreferencias preferencias;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letra_hinos);

        //INICIALIZANDO VIDEO YOUTUBE
        video = findViewById(R.id.youtube_view);
        video.initialize(Atalhos.YOUTUBE_KEY, this);

        //INSTANCIANDO OS COMPONENTES
        tvCantor =  findViewById(R.id.tv_cantor_hino);
        tvHino =    findViewById(R.id.tv_titulo_hino);
        tvLetra =   findViewById(R.id.tv_letra);
        bottomNav = findViewById(R.id.bn_letras_hino);

        //Recuperando as informações temporarias
        preferencias = new SharedPreferencias(LetraHinosActivity.this);

        artista = preferencias.getCHAVE_CANTOR();
        musica  = preferencias.getCHAVE_TITULO();
        numero  = preferencias.getCHAVE_NUM();
        url     = preferencias.getCHAVE_URL().replace("https://youtu.be/", "");
        tvCantor.setText(artista);
        tvHino.setText(numero + "- "+ musica);

        //Convertendo id do Hino
        idHino = CriptografiaBase64.criptografarHino(numero);

        //Verifica se há conexão com a internet
        //if (!verificaConexao()){
            //se não tiver conexão então tenta recuperar os dados salvos do FIREBASE
            // COMENTADO PARA VERIFICAR PORQUE ESTA DANDO ERRO PARA ALGUNS USUÁRIOS
            reference = ConfiguracaoFirebase.getFirebaseReference()
                    .child("MOCIDADE")
                    .child("HINOS")
                    .child(idHino);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        //Recuperando os dados na classe usuário
                        Hinos hinos = dataSnapshot.getValue(Hinos.class);
                        tvLetra.setText(hinos.getLetra());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Se já estiver salvo no firebase ele não pode tentar a conexão novamente
            /*if (tvLetra.getText().toString().length() != 0) {
                //A String que faz a pesquisa no site do vagalume
                String uri = Uri.parse("http://api.vagalume.com.br/search.php")
                        .buildUpon()
                        .appendQueryParameter("mus", musica)
                        .appendQueryParameter("art", artista)
                        .appendQueryParameter("apikey", Atalhos.VAGALUME_KEY)
                        .build().toString();

                if (uri != null && Atalhos.verificarConexao(this) == true) {
                    new LetraHinosActivity.vagalumeAsyncTask().execute(uri);
                } else {

                }
            }*/

        //******************EVENTOS RELACIONADOS A BOTTOMNAVIGATION*******************/
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mn_voltar:
                        finish();
                        break;
                    case R.id.mn_ouvir_letra:
                        android.view.ViewGroup.LayoutParams layoutParams = video.getLayoutParams();
                        layoutParams.height = 450;
                        video.setLayoutParams(layoutParams);
                        video.setVisibility(View.VISIBLE);
                        break;
                    case R.id.mn_compartilhar_letra:
                        compartilharHino();
                        break;

                }

                return true;
            }
        });
        //******************FIM - EVENTOS RELACIONADOS A BOTTOM NAVIGATION*******************/
    }

    /*private class vagalumeAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            return jParser.getJSONFromUrl(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //acionando a barra de progresso
            exibirProgress(true);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            try {

                JSONObject mus = (JSONObject) json.getJSONArray("mus").get(0);
                tvLetra.setText(mus.getString("text"));

                //SALVANDO A LETRA NO FIREBASE
                Hinos hinos = new Hinos();
                hinos.setUrl(preferencias.getCHAVE_URL());
                hinos.setCantor(preferencias.getCHAVE_CANTOR());
                hinos.setTitulo(preferencias.getCHAVE_TITULO());
                hinos.setNumero(preferencias.getCHAVE_NUM());
                hinos.setLetra(tvLetra.getText().toString());
                hinos.salvarHinos();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            exibirProgress(false);
        }
    }*/

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean warRestored) {
        if (url != null){
            if (!warRestored){
                player.cueVideo(url);
            } else {
                //vazio
            }
        }
    }


    public void compartilharHino(){
        //Compartilhando o novo hino no WhatsApp
        Intent shared = new Intent(Intent.ACTION_SEND);
        shared.setType("text/plain");
        shared.putExtra(Intent.EXTRA_TEXT, "Hino: "+ tvHino.getText()+" ("+tvCantor.getText()+") "
        +"https://youtu.be/"+url+" Venha conferir e louvar conosco! -> HerdeirosApp");
        shared.setPackage("com.whatsapp");
        startActivity(shared);

    }

}
