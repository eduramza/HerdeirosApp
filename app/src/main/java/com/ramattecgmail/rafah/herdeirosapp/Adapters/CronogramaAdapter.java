package com.ramattecgmail.rafah.herdeirosapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramattecgmail.rafah.herdeirosapp.Models.Cronograma;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;

/**
 * Created by rafah on 15/10/2017.
 */

public class CronogramaAdapter extends ArrayAdapter<Cronograma> {
    //atributos
    private ArrayList<Cronograma> arrayList;
    private Context context;

    public CronogramaAdapter(@NonNull Context c, @NonNull ArrayList<Cronograma> objects) {
        super(c, 0, objects);
        this.arrayList = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //Validando e criando a lista
        if (arrayList != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montando a view a partir do XML
            view = inflater.inflate(R.layout.lista_cronograma, parent, false);

            //Recuperando os elementos para exibição
            TextView inicio = view.findViewById(R.id.tv_hora_inicio);
            TextView fim = view.findViewById(R.id.tv_hora_fim);
            TextView atividade = view.findViewById(R.id.tv_atividade);

            Cronograma crono =  arrayList.get(position);
            inicio.setText(crono.getHoraInicio());
            fim.setText(crono.getHoraFim());
            atividade.setText(crono.getAtividade());

            switch (crono.getCategoria()){
                case "Espiritual":
                    inicio.setTextColor(Color.parseColor("#F88189"));
                    fim.setTextColor(Color.parseColor("#F88189"));
                    atividade.setTextColor(Color.parseColor("#F88189"));
                    break;
                case "Gincana":
                    inicio.setTextColor(Color.parseColor("#55FA4F"));
                    fim.setTextColor(Color.parseColor("#55FA4F"));
                    atividade.setTextColor(Color.parseColor("#55FA4F"));
                    break;
                case "Lazer":
                    inicio.setTextColor(Color.parseColor("#81B5F8"));
                    fim.setTextColor(Color.parseColor("#81B5F8"));
                    atividade.setTextColor(Color.parseColor("#81B5F8"));
                    break;
            }

        }

        return view;
    }
}
