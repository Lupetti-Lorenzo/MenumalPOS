package com.menumal.menumalPOS;

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

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

// aggiungere riga in basso (spazio ce gia)
// aggiungere costi di servizio sezione dopo prodotti
// aggiungere costi di servizio al totale

// font monospace

// impostazioni:
//      grandezza caratteri titoli e paragrafi

// visturalizzare - se necessario

public class Comanda {
    public static Context mContext;

    public static View creaComanda(String jsonString) throws JSONException {
        // ---------------- ESTRAZIONE DATI E PARSING ---------------------

        JSONObject comanda = new JSONObject(jsonString);
        // estraggo i campi da info
        JSONObject info = comanda.getJSONObject("info");
        String note = info.getString("note");
        String additionalLabel = "";
        try {
            additionalLabel = info.getString("additionalLabel");
        } catch (JSONException e) {
            //
        }
        String table = info.getString("placeholder"); // numero tavolo
        String orderNumber = info.getString("number");     // numero ordine

        // estraggo dal timestamp i secondi e li trasformo in orario HH:MM
        JSONObject timestamp = comanda.getJSONObject("generatedTime");
        int seconds = timestamp.getInt("seconds");
        String orderTime = formatTime(seconds);

        // estraggo i prodotti ordinati
        JSONArray items = comanda.getJSONArray("products");


        // ---------------------- CREAZIONE LAYOUT ------------------------

        // LAYOUT PRINCIPALE SCONTRINO
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(0xFF00FF00);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 16, 10, 16);
        layout.setGravity(Gravity.CENTER);


        // HEADER:  numero dell'ordine #n ---------------- ora
        layout.addView(createHeaderLayout(orderNumber, orderTime));


        // TAVOLO
        TextView textViewtavolo = new TextView(mContext);
        textViewtavolo.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewtavolo.setText("TAVOLO " +  table);
        textViewtavolo.setTypeface(null, Typeface.BOLD);
        textViewtavolo.setTextSize(20);
        textViewtavolo.setGravity(Gravity.CENTER);
        textViewtavolo.setPadding(0, 10, 0, 13);
        layout.addView(textViewtavolo);

        // NOTE
        TextView textViewNote = new TextView(mContext);
        textViewNote.setLayoutParams(new LinearLayout.LayoutParams(
                450,
                ViewGroup.LayoutParams.WRAP_CONTENT
                ));
        textViewNote.setText(note);
        textViewNote.setMaxLines(4); // Set a maximum number of lines
        textViewNote.setTextSize(13);
        textViewNote.setGravity(Gravity.CENTER);
        textViewNote.setPadding(0, 10, 0, 10);
        layout.addView(textViewNote);

        // LISTA ORDINI
        for (int i = 0; i < items.length(); i++) {
            // estraggo i campi dagli ordini
            JSONObject item = items.getJSONObject(i);
            String nome = item.getString("nome");
            String numero = item.getString("quantity");
            String prezzo = item.getString("prezzo");
            // creo il contenitore per l'ordine e lo aggiungo al layout principale
            layout.addView(createOrderLayout(nome, numero));
        }


        // TOTALE
        TextView textViewtotale = new TextView(mContext);
        textViewtotale.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewtotale.setText("Tot " + calculateTotal(items) +  " EUR");
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
        oraView.setText(ora.substring(0, ora.length() - 3));
        oraView.setTypeface(null, Typeface.NORMAL);
        oraView.setTextSize(12);
        oraView.setPadding(0, 8, 0, 8);

        // aggiungo l'header con numero ordine e orario con spazio al centro
        return spaceBetween(countView, oraView);
    }

    private static LinearLayout createOrderLayout(String nome, String numero) {
        // creo il contenitore per l'ordine

        if (nome.equals("Prosciutto e melone")) nome = "piatto lungo a STECCA QQQ BESTIA cheeeee";

        if (nome.length() > 30) {
            // view contentente il nome
            TextView nameView = new TextView(mContext);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(
                    450,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            nameView.setTypeface(null, Typeface.NORMAL);
            nameView.setTextSize(13);
            nameView.setPadding(0, 6, 0, 6);

            // aggiungo nome e numero attaccati
            nameView.setText(nome + "  x" + numero);
            LinearLayout mainLayout = new LinearLayout(mContext);
            mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            mainLayout.addView(nameView);
            return mainLayout;
        }
        else {
            // view contentente il nome
            TextView nameView = new TextView(mContext);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            nameView.setTypeface(null, Typeface.NORMAL);
            nameView.setTextSize(13);
            nameView.setPadding(0, 6, 0, 6);

            // view contentente il nome
            nameView.setText(nome);
            // quantità ordine
            TextView numView = new TextView(mContext);
            numView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            numView.setText(" x"+numero);
            numView.setTypeface(null, Typeface.NORMAL);
            numView.setTextSize(13);
            numView.setPadding(0, 8, 0, 8);
            // aggiungo l'header con numero ordine e orario con spazio al centro
            return spaceBetween(nameView, numView);
        }
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
            float price = Float.parseFloat(item.getString("prezzo").replace(',', '.'));
            int quantity = Integer.parseInt(item.getString("quantity"));
            total += price * quantity;
        }
        return total;
    }

    private static String formatTime(long seconds) {
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        long remainingSeconds = seconds % 60;

        // Format the time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        // Create a Date object with hours, minutes, and remaining seconds
        java.util.Date date = new java.util.Date(hours * 3600 * 1000 + minutes * 60 * 1000 + remainingSeconds * 1000);

        // Return the formatted time
        return dateFormat.format(date);
    }
}
