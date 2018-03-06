package com.example.johanaanesen.imt3673_lab02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private static int MAX_ITEMS = 20; //default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get spinner shared state from Preferences
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final int userChoice = shared.getInt("spinner-choice", -1);
        switch (userChoice){
            case 0: MAX_ITEMS = 10;
            case 1: MAX_ITEMS = 20;
            case 2: MAX_ITEMS = 50;
            case 3: MAX_ITEMS = 100;
        }


    }



    public void goToPrefs(View view){
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivityForResult(intent, 1);
    }

    public void refreshRSS(View view){
        //do refresh thingy!
    }

    @Override
    public void onBackPressed(){ // https://stackoverflow.com/a/42615612 answer i've used :)
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
