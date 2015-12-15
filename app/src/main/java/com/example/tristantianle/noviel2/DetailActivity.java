package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * This class controls the activity_detail view and create a fragment DetailFragment
 */
public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private String message;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        // get the message and
        message = intent.getStringExtra(Intent.EXTRA_TEXT);
        position = intent.getIntExtra("pos",0);
        // Create the fragment instance
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public String getStringData() {
        return message;
    }

    public int getIntData() {
        return position;
    }
}
