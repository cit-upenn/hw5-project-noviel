package com.example.tristantianle.noviel2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

public class SearchList extends AppCompatActivity implements AsyncResponse {
    private String message;
    private String url;
    private ArrayAdapter<String> mForecastAdapter;
    private JSONObject bookJson;
    ListView listview;
    private DataProcessing dp;
    private AuthorProcessing dp1;
    private static Context myContext;
    String[] result;
    private int getCount;

    private final String LOG_TAG = SearchList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        myContext = this;
        listview = (ListView) findViewById(R.id.listview_forecast);
//        setHasOptionsMenu(true);
        Intent intent = getIntent();
        getCount = intent.getIntExtra("count", 0);

        if (getCount == 1){
            dp = new DataProcessing(myContext, listview);
            dp.delegate = this;
            message = intent.getStringExtra("titleKey");

            //        String message = "Harry Potter";

            dp.execute(message);

        }
        if (getCount == 2){
            dp1 = new AuthorProcessing(myContext, listview);
            dp1.delegate = this;

            message = intent.getStringExtra("authorKey");

            //        String message = "Harry Potter";

            dp1.execute(message);

        }
        listview.setAdapter(mForecastAdapter);
        clickList();
    }

    public static Context getContext() {
        return myContext;
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_search_list, menu);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    public void clickList() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String urlStr = url;
                Intent intent = new Intent(SearchList.this, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, url);
                intent.putExtra("pos",position);
                Log.d(LOG_TAG, "Pass in url: " + url);
                startActivity(intent);
            }
        });
    }

    public void processFinish(String[] output){
        result = output;
        mForecastAdapter = new ArrayAdapter<String>(
                SearchList.getContext(), // The current context (this activity)
                R.layout.list_item_result, // The name of the layout ID.
                R.id.list_item_forecast_textview, // The ID of the textview to populate.
                result);
        listview.setAdapter(mForecastAdapter);
        if (getCount == 1){
            url = dp.getURL();
        }
        if (getCount == 2){
            url = dp1.getURL();
        }

//        Log.d(LOG_TAG, "processFinish: " + output[0]);
    }



}
