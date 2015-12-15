package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * this class will handle sending data to the activity_main
 */
public class MainActivity extends AppCompatActivity {

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * this method will deal with message sent from the book title search bar
     * @param view the view in the activity_main
     */
    public void sendTitle(View view) {
        // This is an identification letter, 1 represents title search
        count = 1;
        Intent intent = new Intent(this, SearchList.class);
        EditText editText = (EditText) findViewById(R.id.title_search);
        String message = editText.getText().toString();
        // if didn't input anything, don't start the activity
        if (message.isEmpty()) {
            return;
        }
        // Send user input and the identification to SearchList
        intent.putExtra("titleKey", message);
        intent.putExtra("count", count);
        startActivity(intent);
    }

    /**
     * this method will deal with message sent from the book author search bar
     * @param view the view in the activity_main
     */
    public void sendAuthor(View view) {
        // This is an identification letter, 2 represents author search
        count = 2;
        Intent intent = new Intent(this, SearchList.class);
        EditText editText = (EditText) findViewById(R.id.author_search);
        String message = editText.getText().toString();
        if (message.isEmpty()) {
            return;
        }
        // Send user input and the identification to SearchList
        intent.putExtra("authorKey", message);
        intent.putExtra("count",count);
        startActivity(intent);
    }

    /**
     * this method will deal with message sent from the book publisher search bar
     * @param view the view in the activity_main
     */
    public void sendPublisher(View view) {
        // This is an identification letter, 3 represents publisher search
        count = 3;
        Intent intent = new Intent(this, SearchList.class);
        EditText editText = (EditText) findViewById(R.id.publisher_search);
        String message = editText.getText().toString();
        if (message.isEmpty()) {
            return;
        }
        // Send user input and the identification to SearchList
        intent.putExtra("publisherKey", message);
        intent.putExtra("count", count);
        startActivity(intent);
    }
}
