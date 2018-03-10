package com.example.johanaanesen.imt3673_lab02;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class AutoUpdateFeed extends IntentService {

    public AutoUpdateFeed() {
        super("AutoUpdateFeed");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //System.out.println("updating..");
        // Get prefs
        String url = intent.getStringExtra("url");
        String limit = intent.getStringExtra("max_items");

        // Perform a fetch of the feed
        FetchFeedTask task = new FetchFeedTask(new FetchCallback());
        task.execute(url, limit);
    }

    /**
     * Callback from FetchFeedTask
     */
    public class FetchCallback implements Task {
        @Override
        public void onSuccess(List<RssFeedModel> res) {
            // Save the feed to cache
            try{
                InternalStorage.writeObject(AutoUpdateFeed.this, "asd", res);
            }catch (IOException e) {
                Log.e("fuck", e.getMessage());
            }
        }

        @Override
        public void onError(Exception err) {
            Log.e("BackgroundFetchService", Log.getStackTraceString(err));
        }
    }

}
