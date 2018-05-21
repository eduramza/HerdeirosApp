package com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ramattecgmail.rafah.herdeirosapp.Models.Cronograma;
import com.ramattecgmail.rafah.herdeirosapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCronogramaFragment extends DialogFragment {
    //ATRIBUTOS
    private EditText etHoraInicio, etHoraTermino, etAtividade;
    private TextInputLayout tiInicio, tiFim, tiAtividade;
    private Spinner spinner;
    private Button btSalvar;

    private String[] categoria = {"Espiritual", "Gincana", "Lazer"};

    public AddCronogramaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cronograma, container, false);
        this.getDialog().setTitle("Add Cronograma do Retiro");

        //INSTANCIANDO OS COMPONENTES
        etHoraInicio = view.findViewById(R.id.et_hora_inicio);
        etHoraTermino = view.findViewById(R.id.et_hora_fim);
        etAtividade = view.findViewById(R.id.et_atividade);
        btSalvar = view.findViewById(R.id.bt_salvar_cronograma);
        spinner = view.findViewById(R.id.sp_categoria);
        tiAtividade = view.findViewById(R.id.ti_atividade);
        tiInicio = view.findViewById(R.id.ti_hora_inicio);
        tiFim = view.findViewById(R.id.ti_hora_fim);

        //Spinner
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoria);
        spinner.setAdapter(adapter);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarCronograma();
            }
        });

        return view;
    }

    private void salvarCronograma() {
        //VALIDANDO SE OS CAMPOS FORAM PREENCIDOS
        if (etAtividade.getText().length() < 4 || etHoraInicio.getText().length() < 5 || etHoraTermino.getText().length() < 5){
            if (etAtividade.getText().length() < 4){
                tiAtividade.setError("Este campo precisa ser preenchido");
            } else if (etHoraInicio.getText().length() < 5){
                tiInicio.setError("Hora inválida ou não informada");
            } else if (etHoraTermino.getText().length() < 5){
                tiFim.setError("Hora inválida ou não informada");
            }
        } else {
            Cronograma cronograma = new Cronograma();
            cronograma.setAtividade(etAtividade.getText().toString());
            cronograma.setCategoria(spinner.getSelectedItem().toString());
            cronograma.setHoraFim(etHoraTermino.getText().toString());
            cronograma.setHoraInicio(etHoraInicio.getText().toString());
            cronograma.setDia(getArguments().getString("dia"));
            cronograma.salvarCronograma();
            dismiss();
        }
    }

}
