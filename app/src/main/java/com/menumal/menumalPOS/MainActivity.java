package com.menumal.menumalPOS;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ahmedelsayed.sunmiprinterapp.R;
import com.ahmedelsayed.sunmiprinterutill.PrintMe;

// per pubblicare app andare su sunmi developers e piazzare l'apk
public class MainActivity extends AppCompatActivity {

    private PrintMe printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printer =  new PrintMe(this);
        webViewSetup();
    }

    private void webViewSetup() {
        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.addJavascriptInterface(new JSInterface(this, printer), "Android");
        webView.loadUrl("https://dev.menumal.it/areaprivata/ordini.php");
    }
}
