package com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ramattecgmail.rafah.herdeirosapp.Models.Cardapio;
import com.ramattecgmail.rafah.herdeirosapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarCardapioFragment extends DialogFragment {
    //ATRIBUTOS
    String[] refeicoes = {"Café da manhã", "Almoço", "Café da tarde", "Janta", "Outro"};
    String[] dias = {"24/11", "25/11", "26/11"};
    Spinner spRefeicoes, spData;
    EditText etReceita;
    Button btSalvar;


    public AdicionarCardapioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adcionar_cardapio, container, false);

        //INSTANCIANDO OS COMPONENTES
        btSalvar = view.findViewById(R.id.bt_salvar_cardapio);
        spData = view.findViewById(R.id.sp_data);
        spRefeicoes = view.findViewById(R.id.sp_refeicao);
        etReceita = view.findViewById(R.id.et_ingredientes);

        //Configurando os Spinners
        ArrayAdapter adapterRefeicao = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, refeicoes);
        ArrayAdapter adapterDias = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, dias);

        spData.setAdapter(adapterDias);
        spRefeicoes.setAdapter(adapterRefeicao);

        //EVENTO DE CLICK NO BOTÃO
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Configurando a ordem para exibição do cardapio
                String ordem = String.valueOf(spRefeicoes.getSelectedItemPosition());

                //Transferindos os dados para a classe modelo
                if (etReceita.getText().length() > 4){
                    Cardapio cardapio = new Cardapio();
                    cardapio.setData(String.valueOf(spData.getSelectedItem()));
                    cardapio.setRefeicao(String.valueOf(spRefeicoes.getSelectedItem()));
                    cardapio.setIngredientes(etReceita.getText().toString());
                    cardapio.setOrdem(ordem);
                    cardapio.salvarCardapio();
                    Toast.makeText(getActivity(), "Refeição salva com sucesso", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "é preciso informar o que teremos!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

}
