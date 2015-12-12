package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
//    public final static String EXTRA_MESSAGE = "com.example.tristantianle.noviel.MESSAGE";

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendTitle(View view) {
        count =1;
        Intent intent = new Intent(this, SearchList.class);
        EditText editText = (EditText) findViewById(R.id.title_search);
        String message = editText.getText().toString();
        intent.putExtra("titleKey", message);
        intent.putExtra("count", count);
        startActivity(intent);
    }

    public void sendAuthor(View view) {
        count = 2;
        Intent intent = new Intent(this, SearchList.class);
        EditText editText = (EditText) findViewById(R.id.author_search);
        String message = editText.getText().toString();
        intent.putExtra("authorKey", message);
        intent.putExtra("count",count);
        startActivity(intent);
    }
}
