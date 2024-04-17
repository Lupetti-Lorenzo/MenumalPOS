package com.menumal.menumalPOS;

import androidx.appcompat.app.AppCompatActivity;


import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webView.addJavascriptInterface(new JSInterface(this, printer), "Android");

//        webView.setWebViewClient(new WebViewClient() {
//         @Override
//         public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//             super.onReceivedError(view, request, error);
//             Log.e("ERROR","ERRORAZZOOOOOOOOOOO");
//         }
//
////         @Override
////         public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////             Log.e("LOADURL", request.toString());
////             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                 view.loadUrl(request.getUrl().toString());
////             }
////             return true;
////         }
//
//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//             Log.e("SSL", error.toString());
//            handler.proceed(); // Ignora gli errori SSL
//        }
//        });

        webView.loadUrl("http://192.168.1.95:5000/login/eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay11M3F0MkBtZW51bWFsLTgzNzU5LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwic3ViIjoiZmlyZWJhc2UtYWRtaW5zZGstdTNxdDJAbWVudW1hbC04Mzc1OS5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHl0b29sa2l0Lmdvb2dsZWFwaXMuY29tL2dvb2dsZS5pZGVudGl0eS5pZGVudGl0eXRvb2xraXQudjEuSWRlbnRpdHlUb29sa2l0IiwiaWF0IjoxNzEzMjgzMTc0LCJleHAiOjE3MTMyODY3NzQsInVpZCI6Im1lbWFsXzY1ZjliZWVmMDA5Yzc3Ljk4NjUxNDgzIiwiY2xhaW1zIjp7ImN1c3RvbVRva2VuIjp0cnVlLCJtZW51IjoidGVzdCJ9fQ.CpzxgeFJCRNJ_THlnG4c8DI9NxuiOlBZ8TvynLTMW-zgiNqabO6FSpvpX6ncDmdvG3kM1F2iAqpUFkAuzeClv0RLHnzgvYB4jNJw0TclYraA80UMTKJJMD_pIpNsmBhR868gVzE1EnoWx7pAlJ0QUvxrwNqYHxtH9wmIKFW-K2D-tY_KvX5a0o-Fpzz4dPcwT3QT0ZqPnO3zC8pNW41Zl8v1MowYiIeM75dgWMxNaYOvxPebvBssr5RRMhdMcAHXYK1gyVpMAOeL0N5jpWjU-MXxVIhky2K7ol-mUVyUHM441jIQy22eboEJcSxQxcy--dyzFrvZ0sJZxjSaHqNNbg");
    }
}
