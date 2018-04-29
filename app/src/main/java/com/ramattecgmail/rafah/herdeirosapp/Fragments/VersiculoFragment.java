package com.ramattecgmail.rafah.herdeirosapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Models.Versiculos;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersiculoFragment extends DialogFragment {
    //atrbutos
    private EditText etMensagem, etVersiculo;
    private Button btSalvar;
    private SharedPreferencias user;

    public VersiculoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_versiculo, container, false);
        this.getDialog().setTitle("Versiculos");

        etMensagem = view.findViewById(R.id.et_mensagem);
        etVersiculo = view.findViewById(R.id.et_versiculo);
        btSalvar = view.findViewById(R.id.bt_salvar_versiculo);

        user = new SharedPreferencias(getActivity());

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarGravacao();
            }
        });

        return view;
    }

    private void validarGravacao(){
        //Verificando se os campos foram devidamente preenchidos
        if (etVersiculo.getText().length() > 0 && etMensagem.getText().length() > 0){
            //Salvando na classe modelo
            Versiculos versiculos = new Versiculos();
            versiculos.setDataPostagem(Atalhos.getHoje());
            versiculos.setMensagem(etMensagem.getText().toString());
            versiculos.setVersiculo(etVersiculo.getText().toString());
            versiculos.setUsuPostagem(user.getCHAVE_NOME());
            versiculos.salvarVersiulo();
            Toast.makeText(getActivity(), "Versiculo adicionado com sucesso!", Toast.LENGTH_SHORT).show();
            etMensagem.setText("");
            etVersiculo.setText("");
        } else {
            Toast.makeText(getActivity(), "Você não preencheu todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
        }
    }

}
