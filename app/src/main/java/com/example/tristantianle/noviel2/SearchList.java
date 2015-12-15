package com.example.tristantianle.noviel2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchList extends AppCompatActivity implements AsyncResponse {
    private String message;
    private String url;
    private ArrayAdapter<String> resultAdapter;
    private ListView listview;
    private DataProcessing dp;
    private AuthorProcessing dp1;
    private PublisherProcessing dp2;
    private static Context myContext;
    private String[] result;
    private int getCount;
    private final String LOG_TAG = SearchList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        myContext = this;
        listview = (ListView) findViewById(R.id.listview_forecast);
        Intent intent = getIntent();
        getCount = intent.getIntExtra("count", 0);
        // if count is 1, call the DataProcessing method to handle book title search
        if (getCount == 1){
            dp = new DataProcessing(listview);
            dp.delegate = this;
            message = intent.getStringExtra("titleKey");
            // String message is book title
            dp.execute(message);
        }
        // if count is 2, call the AuthorProcessing method to handle book author search
        if (getCount == 2){
            dp1 = new AuthorProcessing(listview);
            dp1.delegate = this;
            message = intent.getStringExtra("authorKey");
            // String message is book author
            dp1.execute(message);
        }
        // if count is 3, call the PublisherProcessing method to handle book publisher search
        if (getCount == 3){
            dp2 = new PublisherProcessing(listview);
            dp2.delegate = this;
            message = intent.getStringExtra("publisherKey");
            // String message is book publisher
            dp2.execute(message);
        }
        listview.setAdapter(resultAdapter);
        clickList();
    }

    /**
     * This method allows the background to acquire the context of SearchList
     * @return context of the view
     */
    public static Context getContext() {
        return myContext;
    }

    /**
     * This defines a method to start the detail activity once an item in the list is clicked
     */
    public void clickList() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(SearchList.this, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, url);
                intent.putExtra("pos",position);
                Log.d(LOG_TAG, "Pass in url: " + url);
                startActivity(intent);
            }
        });
    }

    /**
     * This method processes the values passed back from background
     * It guarantees the list view has been refreshed
     *
     * @param output
     */
    public void processFinish(String[] output){
        result = output;
        resultAdapter = new ArrayAdapter<String>(
                SearchList.getContext(),          // The current context (this activity)
                R.layout.list_item_result,        // The name of the layout ID.
                R.id.list_item_forecast_textview, // The ID of the textview to populate.
                result);
        listview.setAdapter(resultAdapter);
        if (getCount == 1){
            url = dp.getURL();
        }
        if (getCount == 2){
            url = dp1.getURL();
        }
        if (getCount == 3){
            url = dp2.getURL();
        }
    }
}
