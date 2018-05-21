package com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ramattecgmail.rafah.herdeirosapp.Models.Placar;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacarFragment extends DialogFragment{
    //ARTRIBUTOS
    private Button btSalvar;
    private EditText etEmanuel, etGideoes;

    public PlacarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_placar, container, false);
        this.getDialog().setTitle("Placar Retiro");

        btSalvar = view.findViewById(R.id.bt_salvar_placar);
        etEmanuel = view.findViewById(R.id.et_emanuel);
        etGideoes = view.findViewById(R.id.et_gideoes);

        //Pegando a hora
        Date dataAtual = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String today = dateFormat.format(dataAtual);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmanuel.getText().length() < 1 || etGideoes.getText().length() < 1){

                    Toast.makeText(getActivity(), "Você precisa preencher todos os campos", Toast.LENGTH_SHORT).show();

                } else {
                    Placar placar = new Placar();
                    placar.setAtualizacao(today);
                    placar.setPlacarEmanuel(etEmanuel.getText().toString());
                    placar.setPlacarGideoes(etGideoes.getText().toString());
                    placar.salvarPlacar();
                    Toast.makeText(getActivity(), "Placar salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }
}
