package com.ramattecgmail.rafah.herdeirosapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramattecgmail.rafah.herdeirosapp.Models.Cardapio;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;

/**
 * Created by rafah on 04/10/2017.
 */

public class CardapioAdapter extends ArrayAdapter<Cardapio> {
    private ArrayList<Cardapio> arrayList;
    private Context context;

    public CardapioAdapter(@NonNull Context c, @NonNull ArrayList<Cardapio> objects) {
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
            view = inflater.inflate(R.layout.lista_cardapio, parent, false);

            //Recuperando os componentes para exibição
            TextView refeicao = view.findViewById(R.id.tv_refeicao_lista);
            TextView ingredientes = view.findViewById(R.id.tv_ingredientes_lista);

            //Alterando as fontes
            Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/Mauritian Vibration.ttf");
            Typeface fontI = Typeface.createFromAsset(context.getAssets(), "fonts/Hello Stranger.otf");
            refeicao.setTypeface(fonts);
            ingredientes.setTypeface(fontI);

            Cardapio cardapio = arrayList.get(position);
            refeicao.setText("**" + cardapio.getRefeicao() + "**");
            ingredientes.setText(cardapio.getIngredientes());

        }
        return view;
    }
}
