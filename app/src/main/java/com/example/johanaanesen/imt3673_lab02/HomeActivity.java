package com.example.johanaanesen.imt3673_lab02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static String URL = ""; //default
    private static int MAX_ITEMS = 20; //default
    private static int REFRESH_RATE = 10*60;
    private static String KEY = "asd";
    private static String TAG = "tag_asd";

    private List<RssFeedModel> rssItemList;
    private List<RssFeedModel> tempItemList;
    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //get preferences
        getPrefs();

        //set listView
        listView = (ListView)findViewById(R.id.ListeID);

        rssItemList = getCachedFeed();

        if (rssItemList == null){
            //get rss feed if none is cached from earlier
            new FetchFeedTask().execute((Void) null);
        }else{
            setListAdapter();
            Toast.makeText(HomeActivity.this,
                    "Welcome back :)",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //reload preferences
        getPrefs();
    }

    public void getPrefs(){
        // Get spinner shared state from Preferences
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final int itemSpinner = shared.getInt("item-spinner", -1);
        final int refreshSpinner = shared.getInt("refresh-spinner", -1);
        String userURL = shared.getString("URL", "https://www.vg.no/rss/feed");
        URL = userURL;
        switch (itemSpinner){
            case 0: MAX_ITEMS = 10;
                break;
            case 1: MAX_ITEMS = 20;
                break;
            case 2: MAX_ITEMS = 50;
                break;
            case 3: MAX_ITEMS = 100;
                break;
            default: MAX_ITEMS = 20;
                break;
        }
        switch (refreshSpinner){
            case 0: REFRESH_RATE = 10*60; //10 min in seconds // 600
                break;
            case 1: REFRESH_RATE = 60*60; //1 hours in seconds // 3600
                break;
            case 2: REFRESH_RATE = 24*60*60; //1 day in seconds // 86400
                break;
            default: REFRESH_RATE = 10*60; //default 10 min
                break;
        }
    }

    public void goToPrefs(View view){
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    public void refreshRSS(View view){
        Toast.makeText(HomeActivity.this,
                "updating..",
                Toast.LENGTH_LONG).show();

        new FetchFeedTask().execute((Void) null);
    }


    public List<RssFeedModel> getCachedFeed(){
        try {
            return (List<RssFeedModel>) InternalStorage.readObject(this, KEY);
        }catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public void writeCachedFeed(List<RssFeedModel> list){
        try {
            // Save the list of entries to internal storage
            InternalStorage.writeObject(this, KEY, list);
        }catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * parseFeed not my work, although I've heavily modified the code
     * original: https://github.com/obaro/SimpleRSSReader/blob/master/app/src/main/java/com/sample/foo/simplerssreader/MainActivity.java - see parseFeed function
     * @param inputStream
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if(isItem) {
                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    }
                }

                if (title != null && link != null) {
                    RssFeedModel item = new RssFeedModel(title, link);
                    items.add(item);

                    //reset
                    title = null;
                    link = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    /**
     * FetchFeedTask not 100% my work, although heavily modified
     * original: https://github.com/obaro/SimpleRSSReader/blob/master/app/src/main/java/com/sample/foo/simplerssreader/MainActivity.java
     */
    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
           urlLink = URL;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "https://" + urlLink;

                URL url = new URL(urlLink+ "?limit=" + MAX_ITEMS);
                InputStream inputStream = url.openConnection().getInputStream();
                tempItemList = parseFeed(inputStream);
                return true;
            } catch (IOException e) { //error handling
                Log.e("HomeActivity", "Error", e);
            } catch (XmlPullParserException e) {
                Log.e("HomeActivity", "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

                writeCachedFeed(tempItemList);
                rssItemList = getCachedFeed();
                setListAdapter();

                Toast.makeText(HomeActivity.this,
                        "Successfully updated.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HomeActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setListAdapter(){
        //set list adapter / fill listView
        listAdapter = new ListAdapter(HomeActivity.this,
                R.layout.rss_item_layout,
                rssItemList);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                RssFeedModel entry= (RssFeedModel) parent.getAdapter().getItem(position);
                Intent intent = new Intent(HomeActivity.this, ContentActivity.class);
                String message = entry.link;
                intent.putExtra("content_url", message);
                startActivity(intent);
            }
        });
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
