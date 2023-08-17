package com.ahmedelsayed.sunmiprinterapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comanda {
    public static Context mContext;

    public static View creaComanda(String jsonString) throws JSONException {
        JSONObject comanda = new JSONObject(jsonString);
        // estraggo i campi
        String tavolo = comanda.getString("numeroPosto");
        String note = comanda.getString("note");
        String count = comanda.getString("count");
        String ora = comanda.getString("ora");
        String pagato = comanda.getString("pagato");
        String stato = comanda.getString("stato");
        String tipo = comanda.getString("tipo");
        String campoAggiuntivo = comanda.getString("campoAggiuntivo");
        JSONArray items = comanda.getJSONArray("items");

        // creo il layout principale dello scontrino
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(0xFF00FF00);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 16, 10, 16);
        layout.setGravity(Gravity.CENTER);

        // CREAZIONE HEADER: numero dell'ordine #n ---------------- ora
        layout.addView(createHeaderLayout(count, ora));


        // tavolo
        TextView textViewtavolo = new TextView(mContext);
        textViewtavolo.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewtavolo.setText("TAVOLO " +  tavolo);
        textViewtavolo.setTypeface(null, Typeface.BOLD);
        textViewtavolo.setTextSize(20);
        textViewtavolo.setGravity(Gravity.CENTER);
        textViewtavolo.setPadding(0, 10, 0, 13);
        layout.addView(textViewtavolo);



        // ordini
        for (int i = 0; i < items.length(); i++) {
            // estraggo i campi dagli ordini
            JSONObject item = items.getJSONObject(i);
            String nome = item.getString("nome");
            String numero = item.getString("numero");
            String prezzo = item.getString("prezzo");
            // creo il contenitore per l'ordine e lo aggiungo al layout principale
            layout.addView(createOrderLayout(nome, numero));
        }


        // totale
        TextView textViewtotale = new TextView(mContext);
        textViewtotale.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewtotale.setText("Tot " + calculateTotal(items) + "EUR");
        textViewtotale.setTypeface(null, Typeface.BOLD);
        textViewtotale.setTextSize(20);
        textViewtotale.setGravity(Gravity.CENTER);
        textViewtotale.setPadding(0, 10, 0, 8);
        layout.addView(textViewtotale);

        return layout;
    }

    private static LinearLayout createHeaderLayout(String count, String ora) {
        // view contentente l'ordine
        TextView countView = new TextView(mContext);
        countView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        countView.setText("#"+count);
        countView.setTypeface(null, Typeface.NORMAL);
        countView.setTextSize(12);
        countView.setPadding(0, 8, 0, 8);
        // ora arrivo ordine
        TextView oraView = new TextView(mContext);
        countView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        oraView.setText(ora);
        oraView.setTypeface(null, Typeface.NORMAL);
        oraView.setTextSize(12);
        oraView.setPadding(0, 8, 0, 8);

        // aggiungo l'header con numero ordine e orario con spazio al centro
        return spaceBetween(countView, oraView);
    }

    private static LinearLayout createOrderLayout(String nome, String numero) {
        // creo il contenitore per l'ordine
        // view contentente l'ordine
        TextView nameView = new TextView(mContext);
        nameView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        nameView.setText(nome);
        nameView.setTypeface(null, Typeface.NORMAL);
        nameView.setTextSize(13);
        nameView.setPadding(0, 6, 0, 6);
        // ora arrivo ordine
        TextView numView = new TextView(mContext);
        numView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        numView.setText("x"+numero);
        numView.setTypeface(null, Typeface.NORMAL);
        numView.setTextSize(13);
        numView.setPadding(0, 8, 0, 8);

        // aggiungo l'header con numero ordine e orario con spazio al centro
        return spaceBetween(nameView, numView);
    }


    private static LinearLayout spaceBetween(View v1, View v2) {
        // crea il layout esterno , aggiungo la prima view, lo spazio nel mezzo e infine la 2 view
        LinearLayout mainLayout = new LinearLayout(mContext);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);

        mainLayout.addView(v1);

        // Create some space between the TextViews
        TextView spaceTextView = new TextView(mContext);
        spaceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // This weight pushes the next TextView to the right
        ));
        mainLayout.addView(spaceTextView);

        // Create the second TextView
        mainLayout.addView(v2);

        return mainLayout;
    }

    private static double calculateTotal(JSONArray items) throws JSONException {
        double total = 0;
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            double prezzo = Double.parseDouble(item.getString("prezzo"));
            int numero = Integer.parseInt(item.getString("numero"));
            total += prezzo * numero;
        }
        return total;
    }
}
