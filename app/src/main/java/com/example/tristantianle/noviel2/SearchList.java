package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SearchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SearchFragment())
                    .commit();
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

    }
}
