package com.ramattecgmail.rafah.herdeirosapp.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ramattecgmail.rafah.herdeirosapp.Models.Hinos;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarHinoFragment extends DialogFragment {
    //ATRIBUTOS
    private EditText etNumero, etHino, etCantor, etURL;
    private Button btSalvar;
    private TextInputLayout tiNum, tiHino, tiCantor, tiUrl;

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

                        //Salvando hino
                        Hinos hinos = new Hinos();
                        hinos.setNumero(etNumero.getText().toString());
                        hinos.setTitulo(etHino.getText().toString());
                        hinos.setCantor(etCantor.getText().toString());
                        hinos.setUrl(etURL.getText().toString());
                        hinos.salvarHinos();

                        dismiss();

                    }

                }
            }
        });

        return view;
    }

}
