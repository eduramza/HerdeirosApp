package com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ramattecgmail.rafah.herdeirosapp.Configs.JSONParser;
import com.ramattecgmail.rafah.herdeirosapp.Models.Hinos;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarHinoFragment extends DialogFragment {
    //ATRIBUTOS
    private EditText etNumero, etHino, etCantor, etURL;
    private Button btSalvar;
    private TextInputLayout tiNum, tiHino, tiCantor, tiUrl;
    private ProgressBar pbHino;

    public AdicionarHinoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar_hino, container, false);

        //INSTANCIANDO OS COMPONENTES
        etNumero = view.findViewById(R.id.et_num_hino);
        etHino = view.findViewById(R.id.et_titulo_hino);
        etCantor = view.findViewById(R.id.et_cantor_hino);
        etURL = view.findViewById(R.id.et_url_video);
        btSalvar = view.findViewById(R.id.bt_salvar);
        tiNum = view.findViewById(R.id.ti_num_hino);
        tiHino = view.findViewById(R.id.ti_titulo_hino);
        tiCantor = view.findViewById(R.id.ti_cantor_hino);
        tiUrl = view.findViewById(R.id.ti_url_video);
        pbHino = view.findViewById(R.id.pb_hino);

        //EVENTO DE CLICK NO BOTÃO
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validando se os campos obrigatórios foram preenchidos
                if (etURL.getText().length() < 5 || etCantor.getText().length() < 3 || etHino.getText().length() < 3
                        || etNumero.getText().length() < 1){
                    if (etURL.getText().length() < 5){
                        tiUrl.setError("Informe a URL do video Youtube");
                    } else if(etCantor.getText().length() < 3){
                        tiCantor.setError("Informe um cantor Válido");
                    } else if (etHino.getText().length() < 3){
                        tiHino.setError("Informe um Hino Válido");
                    } else if (etNumero.getText().length() < 1){
                        tiNum.setError("Você precisa inserir o numero do Hino");
                    }
                } else {
                    //Verifica conexão com a internet primeiro
                    if (Atalhos.verificarConexao(getActivity()) == true){

                        //Baixando a letra do hino e salvando direto no firebase
                        String uri = Uri.parse("http://api.vagalume.com.br/search.php")
                                .buildUpon()
                                .appendQueryParameter("mus", etHino.getText().toString())
                                .appendQueryParameter("art", etCantor.getText().toString())
                                .appendQueryParameter("apikey", Atalhos.VAGALUME_KEY)
                                .build().toString();

                        if (uri != null){
                            new vagalumeAsyncTask().execute(uri);
                        }
                    }
                }
            }
        });

        return view;
    }

    /*************************** CLASS PARA DOWNLOAD DA LETRA DO HINO ***********************/
    private class vagalumeAsyncTask extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            return jsonParser.getJSONFromUrl(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try{
                JSONObject letra = (JSONObject) jsonObject.getJSONArray("mus").get(0);

                //SALVANDO OS DADOS NO FIREBASE
                Hinos hinos = new Hinos();
                hinos.setUrl(etURL.getText().toString());
                hinos.setCantor(etCantor.getText().toString());
                hinos.setLetra(letra.getString("text"));
                hinos.setTitulo(etHino.getText().toString());
                hinos.setNumero(etNumero.getText().toString());
                hinos.salvarHinos();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            exibirProgress(false);
            dismiss();
        }
    }


    private void exibirProgress(boolean exibir) {
        pbHino.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
