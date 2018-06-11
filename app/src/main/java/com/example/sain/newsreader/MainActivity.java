package com.example.sain.newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String API_KEY = BuildConfig.ApiKey;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> url = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json = null;

        DownloadTask downloadTask = new DownloadTask();
        try {
            json = downloadTask.execute(String.format(Locale.getDefault(),
                    "http://newsapi.org/v2/top-headlines?country=in&apiKey=%s", API_KEY)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            for (int i = 0; i < jsonArray.length(); i++) {
                title.add(jsonArray.getJSONObject(i).getString("title"));
                url.add(jsonArray.getJSONObject(i).getString("url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, title);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        intent.putExtra("url", url.get(position));
        startActivity(intent);
    }
}
