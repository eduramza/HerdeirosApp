package com.ramattecgmail.rafah.herdeirosapp.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramattecgmail.rafah.herdeirosapp.Models.Hinos;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;

/**
 * Created by rafah on 06/10/2017.
 */

public class HinosAdapter extends ArrayAdapter<Hinos> {
    //ATRIBUTOS
    Context context;
    private ArrayList<Hinos> arrayList;
    private ImageView heart;

    public HinosAdapter(@NonNull Context c, ArrayList<Hinos> objects) {
        super(c, 0, objects);
        this.context = c;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //Validando e criando a lista de hinos
        if (arrayList != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //montando a view a partir do XML
            view = inflater.inflate(R.layout.lista_hinos, parent, false);

            //Recuperando os elementos para exibição
            TextView tvNumero = view.findViewById(R.id.tv_num_hino);
            TextView tvTitulo = view.findViewById(R.id.tv_titulo_hino);
            TextView tvCantor = view.findViewById(R.id.tv_cantor_hino);
            heart = view.findViewById(R.id.img_heart);

            Hinos hinos = arrayList.get(position);
            tvNumero.setText(hinos.getNumero());
            tvCantor.setText(hinos.getCantor());
            tvTitulo.setText(hinos.getTitulo());

            //efeitos com o click de curtir
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //trocando as cores do coração - ATENÇÃO !!!! ADICIONAR O ANIMATION AQUI!!!
                    if (heart.getDrawable().equals(R.drawable.ic_favorite_border)){
                        heart.setImageResource(R.drawable.ic_favorite_border);
                    } else {
                        heart.setImageResource(R.drawable.ic_favorite_border);
                    }

                }
            });

        }
        return view;
    }
}
