package com.peseca.browser;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText url_edittext;
    ImageButton settings_acilis_buton;
    ImageButton search_btn_main;
    WebView webView_main;
    ProgressBar progressbar_main;
    RelativeLayout error_page;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SEARCH_ENGINE = null;
    private String search_engine;
    private boolean is_error_occured = false;
    private String url_webview;
    public boolean isCameFromExternalApp = false;
    public String arama_sonuc;
    public boolean isNewerVersionAvailable;
    public boolean isCleaned = false;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isCleaned= true;

        // findViewByIds Are Here
        url_edittext = findViewById(R.id.url_edittext);
        settings_acilis_buton = findViewById(R.id.settings_acilis_buton);
        search_btn_main = findViewById(R.id.search_btn_main);
        webView_main = findViewById(R.id.webview_main);
        progressbar_main = findViewById(R.id.progress_bar);
        error_page = findViewById(R.id.error_webview);

        // Some settings for webview
        webView_main.getSettings().setJavaScriptEnabled(true);
        webView_main.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView_main.getSettings().setSupportMultipleWindows(true);
        webView_main.getSettings().setBuiltInZoomControls(true);



        webView_main.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView_main.setVisibility(View.INVISIBLE);
                error_page.setVisibility(View.VISIBLE);
                is_error_occured = true;
                url_webview = webView_main.getUrl();
                url_edittext.setText(url_webview);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                url_webview = webView_main.getUrl();
                url_edittext.setText(url_webview);
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                url_webview = webView_main.getUrl();
                url_edittext.setText(url_webview);
            }
        });
        webView_main.setWebChromeClient(new MyChrome(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressbar_main.setProgress(newProgress);
            }
        });
        progressbar_main.setMax(100);
        url_edittext.setTextColor(getResources().getColor(R.color.black));

        //Getting Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        search_engine = sharedPreferences.getString(SEARCH_ENGINE, "Google");

        isNewerVersionAvailable = sharedPreferences.getBoolean("isNewerVersionAvailable", false);
        if (isNewerVersionAvailable) {
            goToNewVersionActivity();
        } else {
            isThereANewVersion();
        }

        // Download Listener
        webView_main.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(url));
            request.allowScanningByMediaScanner();
            String file_name = URLUtil.guessFileName(url, null, null);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file_name);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), R.string.downloading, Toast.LENGTH_LONG).show();

        });

        // Pressed Enter Listener
        url_edittext.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                webView_main.setVisibility(View.VISIBLE);
                arama_sonuc = url_edittext.getText().toString();
                makeSearch();
                return true;
            }
            return false;
        });

        // Making search
         search_btn_main.setOnClickListener(view -> {
             arama_sonuc = url_edittext.getText().toString();
             makeSearch();
         });

         // Opening Settings Page
        settings_acilis_buton.setOnClickListener(v -> settings_acilis());
    }

    //Opening External Links In App
    @Override
    protected void onResume() {
        super.onResume();
        Uri url = getIntent().getData();
        if (url != null) {
            webView_main.loadUrl(url.toString());
            webView_main.setVisibility(View.VISIBLE);
            isCameFromExternalApp = true;
        }
    }



    //Settings Part Opening
    public void settings_acilis() {
        Intent Acilis2 = new Intent(this, SettingsActivity.class);
        startActivity(Acilis2);
    }

    //OnBackPressed
    @Override
    public void onBackPressed() {
        if (isCameFromExternalApp) {
            finishAndRemoveTask();
            finish();
        } else {
            if (!isCleaned) {
                if (is_error_occured) {
                    error_page.setVisibility(View.INVISIBLE);
                    webView_main.setVisibility(View.INVISIBLE);
                    is_error_occured = false;
                    url_edittext.getText().clear();
                    isCleaned = true;
                } else {
                    if (webView_main.canGoBack()) {
                        webView_main.goBack();
                    } else {
                        webView_main.setVisibility(View.INVISIBLE);
                        url_edittext.getText().clear();
                        isCleaned = true;
                    }
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView_main.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView_main.restoreState(savedInstanceState);
    }

    //Function for making searches
    public void makeSearch(){
        isCleaned = false;
        arama_sonuc = url_edittext.getText().toString();
        webView_main.setVisibility(View.VISIBLE);
        if (arama_sonuc.contains(".")) {
            if (arama_sonuc.contains(" ")){
                switch (search_engine) {
                    case "Google":
                        webView_main.loadUrl("https://www.google.com/search?q=" + arama_sonuc);
                        break;
                    case "Yandex":
                        webView_main.loadUrl("https://yandex.com.tr/search/?lr=115700&text=" + arama_sonuc);
                        break;
                    case "Yahoo":
                        webView_main.loadUrl("https://search.yahoo.com/search?p=" + arama_sonuc + "&fr=yfp-t&ei=UTF-8&fp=1");
                        break;
                    case "Bing":
                        webView_main.loadUrl("https://www.bing.com/search?q=" + arama_sonuc);
                        break;
                    case "DuckDuckGo":
                        webView_main.loadUrl("https://duckduckgo.com/?q=" + arama_sonuc + "&t=ffab&ia=web");
                        break;
                    default:
                        webView_main.loadUrl("https://www.google.com/search?q=" + arama_sonuc);
                        break;
                }
                error_page.setVisibility(View.INVISIBLE);
            } else {
                if (arama_sonuc.startsWith("http") || arama_sonuc.startsWith("https")) {
                    webView_main.loadUrl(arama_sonuc);
                } else {
                    webView_main.loadUrl("http://" + arama_sonuc);
                    error_page.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            switch (search_engine) {
                case "Google":
                    webView_main.loadUrl("https://www.google.com/search?q=" + arama_sonuc);
                    break;
                case "Yandex":
                    webView_main.loadUrl("https://yandex.com.tr/search/?lr=115700&text=" + arama_sonuc);
                    break;
                case "Yahoo":
                    webView_main.loadUrl("https://search.yahoo.com/search?p=" + arama_sonuc + "&fr=yfp-t&ei=UTF-8&fp=1");
                    break;
                case "Bing":
                    webView_main.loadUrl("https://www.bing.com/search?q=" + arama_sonuc);
                    break;
                case "DuckDuckGo":
                    webView_main.loadUrl("https://duckduckgo.com/?q=" + arama_sonuc + "&t=ffab&ia=web");
                    break;
                default:
                    webView_main.loadUrl("https://www.google.com/search?q=" + arama_sonuc);
                    break;
            }
            error_page.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            makeSearch();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            makeSearch();
        }
    }

    public void isThereANewVersion() {
        String currentMonth = (new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
        String currentYear = (new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentYearAndMonth = Integer.parseInt(currentYear+currentMonth);
        if (currentYearAndMonth > 202208){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isNewerVersionAvailable", true);
            editor.apply();
            goToNewVersionActivity();
        }
    }

    public void goToNewVersionActivity() {
        Intent goToNewVersionActivity = new Intent(this, NewVersionActivity.class);
        startActivity(goToNewVersionActivity);
    }
}

