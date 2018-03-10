package com.example.johanaanesen.imt3673_lab02;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * FetchFeedTask not 100% my work, although heavily modified
 * original: https://github.com/obaro/SimpleRSSReader/blob/master/app/src/main/java/com/sample/foo/simplerssreader/MainActivity.java
 */
public class FetchFeedTask extends AsyncTask<String, Void, Boolean> {
    private Task callback;
    private Exception error;
    private String urlLink;
    private String MAX_ITEMS;

    private List<RssFeedModel> tempItemList;

    public FetchFeedTask(Task cb) {
        this.callback = cb;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... param) {
        urlLink = param[0];
        MAX_ITEMS = param[1];

        if (TextUtils.isEmpty(urlLink))
            return false;

        try {
            if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "https://" + urlLink;

            URL url = new URL(urlLink+ "?limit=" + MAX_ITEMS);
            InputStream inputStream = url.openConnection().getInputStream();
            tempItemList = HomeActivity.parseFeed(inputStream);
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
            callback.onSuccess(tempItemList);
        } else {
            callback.onError(this.error);
        }
    }
}