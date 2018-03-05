package com.example.johanaanesen.imt3673_lab02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }



    public void goToPrefs(View view){
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
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
