package com.itiqng.itiq;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    Button refreshButton;

    WebView itiqSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refresh_button);

        progressBar = findViewById(R.id.progress_circular);
        itiqSite = findViewById(R.id.itiq_webview);
        itiqSite.setVisibility(View.INVISIBLE);

        refreshButton.setVisibility(View.GONE);

        renderWebPage("https://itiqng.com", itiqSite);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itiqSite.reload();
            }
        });

    }

    protected void renderWebPage(String urlToRender, final WebView webViewToDoStuff){
        webViewToDoStuff.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                itiqSite.setVisibility(View.VISIBLE);
                webViewToDoStuff.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                webViewToDoStuff.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        webViewToDoStuff.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress){

                progressBar.setProgress(newProgress);

                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                    webViewToDoStuff.setVisibility(View.VISIBLE);
                }
            }
        });

        webViewToDoStuff.getSettings().setJavaScriptEnabled(true);
        webViewToDoStuff.loadUrl(urlToRender);
    }

    //When I show a page with an hyperlink
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK) && itiqSite.canGoBack()){
            itiqSite.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
