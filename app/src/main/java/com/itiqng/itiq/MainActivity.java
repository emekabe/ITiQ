package com.itiqng.itiq;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean FAILED = false; // To indicate whether the page loaded or not

    ProgressBar progressBar;

    Button refreshButton;

    WebView itiqSite;

    private TextView loadingText;
    private TextView loadPercentageText;

    TextView failedText;
    ImageView refreshButtonFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refresh_button);
        progressBar = findViewById(R.id.progress_circular);
        itiqSite = findViewById(R.id.itiq_webview);
        loadingText = findViewById(R.id.loading_text);
        loadPercentageText = findViewById(R.id.load_percentage_text);


        failedText = findViewById(R.id.failed_text);
        refreshButtonFailed = findViewById(R.id.refresh_button_failed);

        itiqSite.setVisibility(View.INVISIBLE);

        refreshButton.setVisibility(View.GONE);

        setLoadingVisibilities();

        renderWebPage("https://itiqng.com", itiqSite);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itiqSite.reload();
            }
        });

        refreshButtonFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itiqSite.reload();
//                renderWebPage("https://itiqng.com", itiqSite);
                setLoadingVisibilities();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void renderWebPage(String urlToRender, final WebView webViewToDoStuff){
        webViewToDoStuff.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                itiqSite.setVisibility(View.VISIBLE);
                webViewToDoStuff.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url){

                if (!FAILED){
                    webViewToDoStuff.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    loadingText.setVisibility(View.INVISIBLE);
                    loadPercentageText.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    Toast.makeText(MainActivity.this, "Welcome to ITiQ", Toast.LENGTH_LONG).show();

                    /*TODO: After the user successfully enters the webpage
                    *
                    * Make itiq image, loading x% text invisible
                    * Change the background color to white
                    * Change the spinner color to yellow (former background color)
                    * Change the refresh image to a yellow (former background color) one*/
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                                  WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Do something
                setFailedVisibilities();
                webViewToDoStuff.setVisibility(View.GONE);
                FAILED = true;
            }
        });

        webViewToDoStuff.setWebChromeClient(new WebChromeClient(){
            @SuppressLint("SetTextI18n")
            public void onProgressChanged(WebView view, int newProgress){

                progressBar.setProgress(newProgress);
                loadPercentageText.setText(newProgress + "%");

//                if (newProgress == 100){
////                    Toast.makeText(MainActivity.this, "Welcome to ITiQ", Toast.LENGTH_LONG).show();
////                    progressBar.setVisibility(View.GONE);
////                    webViewToDoStuff.setVisibility(View.VISIBLE);
//                }
                if (newProgress < 100) {
                    FAILED = false; //resetting this variable so that when it loads again, it will show the webview
                }
            }


        });

        webViewToDoStuff.getSettings().setJavaScriptEnabled(true);
        webViewToDoStuff.loadUrl(urlToRender);
    }

    //For navigating backwards
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK) && itiqSite.canGoBack()){
            itiqSite.goBack();
            return true;
        }

        startActivity(new Intent(this, ExitActivity.class));
        finish();
        return true;

//        return super.onKeyDown(keyCode, event);
    }

    private void setLoadingVisibilities(){
        failedText.setVisibility(View.INVISIBLE);
        refreshButtonFailed.setVisibility(View.INVISIBLE);

        loadingText.setVisibility(View.VISIBLE);
        loadPercentageText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setFailedVisibilities(){
        failedText.setVisibility(View.VISIBLE);
        refreshButtonFailed.setVisibility(View.VISIBLE);

        loadingText.setVisibility(View.INVISIBLE);
        loadPercentageText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
