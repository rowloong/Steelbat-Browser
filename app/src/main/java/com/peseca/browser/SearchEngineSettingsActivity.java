package com.peseca.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class SearchEngineSettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SEARCH_ENGINE = null;
    private String search_engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_engine_settings);

        //Findviewbyids
        Button back_button = findViewById(R.id.back_button);
        Button google_button = findViewById(R.id.google_button);
        Button yahoo_button = findViewById(R.id.yahoo_button);
        Button yandex_button = findViewById(R.id.yandex_button);
        Button bing_button = findViewById(R.id.bing_button);
        Button duckduckgo_button = findViewById(R.id.duckduckgo_button);
        ImageView google_check = findViewById(R.id.google_check);
        ImageView yahoo_check = findViewById(R.id.yahoo_check);
        ImageView yandex_check = findViewById(R.id.yandex_check);
        ImageView bing_check = findViewById(R.id.bing_check);
        ImageView duckduckgo_check = findViewById(R.id.duckduckgo_check);

        //Getting Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        search_engine = sharedPreferences.getString(SEARCH_ENGINE, "Google");

        //Settings for search engine change
        if (true) {
            switch (search_engine) {
                case "Google":
                    google_check.setVisibility(View.VISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "Yahoo":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.VISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "Yandex":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.VISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "Bing":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.VISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "DuckDuckGo":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.VISIBLE);
                    break;
            }
        }

        google_button.setOnClickListener(v -> {
            search_engine = "Google";
            save_engine();
            refresh();
        });

        yahoo_button.setOnClickListener(v -> {
            search_engine = "Yahoo";
            save_engine();
            refresh();
        });

        yandex_button.setOnClickListener(v -> {
            search_engine = "Yandex";
            save_engine();
            refresh();
        });

        duckduckgo_button.setOnClickListener(v -> {
            search_engine = "DuckDuckGo";
            save_engine();
            refresh();
        });

        bing_button.setOnClickListener(v -> {
            search_engine = "Bing";
            save_engine();
            refresh();
        });

        back_button.setOnClickListener(v -> geriye_donus());
    }

    //UI
    @Override
    protected void onStart() {
        super.onStart();
        ImageView google_check = findViewById(R.id.google_check);
        ImageView yahoo_check = findViewById(R.id.yahoo_check);
        ImageView yandex_check = findViewById(R.id.yandex_check);
        ImageView bing_check = findViewById(R.id.bing_check);
        ImageView duckduckgo_check = findViewById(R.id.duckduckgo_check);

        if (true) {
            switch (search_engine) {
                case "Google":
                    google_check.setVisibility(View.VISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "Yahoo":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.VISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "Yandex":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.VISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "Bing":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.VISIBLE);
                    duckduckgo_check.setVisibility(View.INVISIBLE);
                    break;
                case "DuckDuckGo":
                    google_check.setVisibility(View.INVISIBLE);
                    yahoo_check.setVisibility(View.INVISIBLE);
                    yandex_check.setVisibility(View.INVISIBLE);
                    bing_check.setVisibility(View.INVISIBLE);
                    duckduckgo_check.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    //Going Back
    public void geriye_donus() {
        Intent geriye_donus = new Intent(this, SettingsActivity.class);
        startActivity(geriye_donus);
    }

    //Going Back
    @Override
    public void onBackPressed() {
        Intent geriye_donus = new Intent(this, SettingsActivity.class);
        startActivity(geriye_donus);
    }

    //Saving the engine
    public void save_engine() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SEARCH_ENGINE, search_engine);

        editor.apply();
    }

    //Refreshing the page
    public void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}