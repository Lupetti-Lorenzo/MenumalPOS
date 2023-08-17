package com.menumal.menumalPOS;

import android.content.Context;

import android.view.View;

import android.webkit.JavascriptInterface;


import com.ahmedelsayed.sunmiprinterutill.PrintMe;

import org.json.JSONException;

public class JSInterface {
    Context mContext;
    PrintMe printer;

    /**
     * Instantiate the interface and set the context
     */
    JSInterface(Context c, PrintMe p) {
        mContext = c;
        printer = p;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void stampa(String json) throws JSONException {
        Comanda.mContext = mContext;
        View layout = Comanda.creaComanda(json);
        // stampo la view
        printer.sendViewToPrinter(layout);
    }


}
