package com.krabd.klient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
/**
 * Created by Sofon on 29.05.2016.
 */
public class PDFShow extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfshow);
        Intent intent = getIntent();
        String URL1 = intent.getStringExtra("URL");
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL1);
    }
}
