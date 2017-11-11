package com.example.hossainkabir.calculatorweb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView viewWeb;
    WebSettings webSettings;
    ConnectivityManager conman;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewWeb=(WebView)findViewById(R.id.webView);
        conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);

        if (conman.getActiveNetworkInfo() == null) {
            viewWeb.loadUrl("file:///android_asset/index.html");
        } else {
            viewWeb.loadUrl("file:///android_asset/VIP2.html");
        }

        webSettings = viewWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebViewInterface webAppInterface = new WebViewInterface(this);
        viewWeb.addJavascriptInterface(webAppInterface,"Android");
        viewWeb.getSettings().setBuiltInZoomControls(true);
    }

}
