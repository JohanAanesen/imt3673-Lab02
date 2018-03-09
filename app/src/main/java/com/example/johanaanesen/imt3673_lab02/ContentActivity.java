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

        //Get contentURL from HomeActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            URL = extras.getString("content_url");
        }

        WebView w1=(WebView)findViewById(R.id.webView);
        w1.getSettings().setJavaScriptEnabled(true);
        w1.getSettings().setBuiltInZoomControls(true);

        w1.loadUrl(URL);
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
