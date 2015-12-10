package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    String message;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
        Intent intent = getIntent();
        message = intent.getStringExtra("json");
        position = intent.getIntExtra("pos",0);

    }
    public String getStringData() {
        return message;
    }
    public int getIntData() {
        return position;
    }
}
