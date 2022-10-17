package com.example.asm_adnc.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NewsService extends IntentService {
    private AsyncHttpClient client = new SyncHttpClient();
    private final String url = "https://api.stackexchange.com/2.3/questions?page=1&pagesize=5&order=desc&sort=activity&site=stackoverflow";
    public static final String NEWS_SERVICE_EVENT = "NEWS_SERVICE_EVENT";


    public NewsService() {
        super("NewsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            client.get(this, url, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        JSONArray items = response.getJSONArray("items");
                        ArrayList<ItemModel> list = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = (JSONObject) items.get(i);
                            Long creation_date = object.getLong("creation_date");
                            String title = object.getString("title");
                            ItemModel itemModel = new ItemModel(creation_date, title);
                            list.add(itemModel);
                        }
                        // dùng broadcast để gửi dữ liệu về activity
//                        Intent outIntent = new Intent(NEWS_SERVICE_EVENT);
//                        outIntent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
//                        LocalBroadcastManager.getInstance(NewsService.this).sendBroadcast(outIntent);
                        Log.d("TAG", "onSuccess: " + list.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public class ItemModel {
        private Long creation_date;
        private String title;

        public ItemModel(Long creation_date, String title) {
            this.creation_date = creation_date;
            this.title = title;
        }

        public Long getCreation_date() {
            return creation_date;
        }

        public void setCreation_date(Long creation_date) {
            this.creation_date = creation_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "ItemModel{" +
                    "creation_date=" + creation_date +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

//    private void getApi(Intent intent) {
//        if (intent != null) {
//            client.get(this, url, new JsonHttpResponseHandler(){
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                    super.onSuccess(statusCode, headers, response);
//                    try {
//                        Log.d(">>>>>>>>>TAG", "OnSuccess: " + response.toString());
//                        ArrayList<ItemModel> items = new ArrayList<>();
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject object = response.getJSONObject(i);
//                            String creation_date = object.getString("creation_date");
//                            String title = object.getString("title");
//                            ItemModel model = new ItemModel(creation_date, title);
//                            items.add(model);
//                            Log.d(">>>>>>>>TAG", "onSuccess: " + title);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.d(">>>>>>>>TAG", "JSONException: " + e.getMessage());
//                    }
//                }
//            });
//        }
//    }
}
