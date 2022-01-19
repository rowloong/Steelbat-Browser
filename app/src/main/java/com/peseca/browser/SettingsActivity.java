package com.peseca.browser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SEARCH_ENGINE = null;
    private String search_engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //Findviewbyids
        Button back_button = findViewById(R.id.back_button);
        Button search_engine_button = findViewById(R.id.search_engine_button);

        //Getting Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        search_engine = sharedPreferences.getString(SEARCH_ENGINE, "Google");

        //Making UI
        search_engine_button.setText(search_engine);

        //On Button Pressed
        search_engine_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_engine_settings_acilis();
            }
        });

        //Going Back To Main Page
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ana_menuye_geridonus();
            }
        });
    }

    //On Back Pressed
    @Override
    public void onBackPressed() {
        Intent geriye_donus = new Intent(this, MainActivity.class);
        startActivity(geriye_donus);
    }

    //Search Engine Page Opening
    public void search_engine_settings_acilis() {
        Intent Acilis1 = new Intent(this, SearchEngineSettingsActivity.class);
        startActivity(Acilis1);
    }

    //Going Back To Main Page
    public void ana_menuye_geridonus() {
        Intent Acilis2 = new Intent(this, MainActivity.class);
        startActivity(Acilis2);
    }
}