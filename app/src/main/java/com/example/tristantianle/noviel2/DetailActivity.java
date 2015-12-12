package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    String message;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        message = intent.getStringExtra(Intent.EXTRA_TEXT);
        position = intent.getIntExtra("pos",0);

        Log.d(LOG_TAG, "onCreate: " + message +"\n"+ position);

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
