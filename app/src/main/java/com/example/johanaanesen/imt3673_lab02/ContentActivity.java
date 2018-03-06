package com.example.johanaanesen.imt3673_lab02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class ContentActivity extends AppCompatActivity {

    private String URL = "vg.no/rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        URL = shared.getString("URL", "http://www.vg.no/RSS");

        WebView w1=(WebView)findViewById(R.id.webView);

        w1.loadUrl(URL);
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
