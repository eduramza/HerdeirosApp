package com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts;


import android.annotation.SuppressLint;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramattecgmail.rafah.herdeirosapp.Activitys.CalendarioActivity;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.CriptografiaBase64;
import com.ramattecgmail.rafah.herdeirosapp.Models.Eventos;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventoDialogFragment extends DialogFragment implements View.OnClickListener{

    //ATRIBUTOS
    private EditText etDataInicio, etDataFim, etDescricao, etTitulo;
    private Button btCancelar, btAdicionar;
    private TextInputLayout tiEvento, tiInicio, tiFim, tiInformacao;

    public EventoDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_evento, container, false);
        this.getDialog().setTitle("Adicionar Evento");
        //Recebendo a data enviada pela activity
        String dataSelecionada = getArguments().getString("DATA SELECIONADA");

        etDataInicio =  view.findViewById(R.id.et_datainicio_evento);
        etDataFim = view.findViewById(R.id.et_datafim_evento);
        btCancelar = view.findViewById(R.id.bt_cancelar_dialog);
        btAdicionar =  view.findViewById(R.id.bt_adicionar_dialog);
        etDescricao = view.findViewById(R.id.et_descricao_dialog);
        etTitulo = view.findViewById(R.id.et_titulo_evento);
        tiEvento = view.findViewById(R.id.ti_titulo_evento);
        tiFim = view.findViewById(R.id.ti_datafim_evento);
        tiInformacao = view.findViewById(R.id.ti_descricao_dialog);
        tiInicio = view.findViewById(R.id.ti_datainicio_evento);

        //********************** RECUPERANDO DATA E HORA ATUAL ********************/
        Date dataAtual = Calendar.getInstance().getTime();

        //Inicializa o simple date format passando o construtor
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Utilizando o metodo para formatar
        String today = dateFormat.format(dataAtual);
        /*************** FIM DA RECUPERAÇÃO DA DATA E DA HORA ********************/

        etDataInicio.setText(dataSelecionada);
        etDataFim.setText(today);

        btAdicionar.setOnClickListener(this);
        btCancelar.setOnClickListener(this);

        return view;
    }

    /************************** METODOS ******************************/
    private void salvarEvento(){
        //Verificando se os campos foram devidamente preenchidos
        if (etTitulo.getText().length() < 5){
            tiEvento.setError("Informe o nome do evento");
        } else if (etDataFim.getText().length() < 12){
            tiFim.setError("Data não preenchida ou invalida");
        } else if (etDataInicio.getText().length() < 12){
            tiInicio.setError("Data não preenchida ou invalida");
        } else if (etDescricao.getText().length() < 10){
            tiInformacao.setError("É preciso de mais dados!");
        } else {
            //Transferindo os dados para o Evento
            String cripto = CriptografiaBase64.criptografarVersiculo(etDataInicio.getText().toString());
            Eventos evento = new Eventos();
            evento.setDataInicio(etDataInicio.getText().toString());
            evento.setDataFim(etDataFim.getText().toString());
            evento.setDescricao(etDescricao.getText().toString());
            evento.setTitulo(etTitulo.getText().toString());
            evento.setId(cripto);
            evento.salvarEvento();

            Toast.makeText(getActivity(), "Evento salvo com sucesso!", Toast.LENGTH_SHORT).show();
            this.dismiss();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_adicionar_dialog:
                salvarEvento();
                break;

            case R.id.bt_cancelar_dialog:
                this.dismiss();
                break;
        }
    }
}
