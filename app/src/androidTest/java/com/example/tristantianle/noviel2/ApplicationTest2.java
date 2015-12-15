package com.example.tristantianle.noviel2;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * This Test tests the default value of edit text is empty
 */
public class ApplicationTest2 extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity ma;
    private EditText title;
    private EditText author;
    private EditText publisher;

    public ApplicationTest2() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ma = this.getActivity();
        title = (EditText) ma.findViewById(R.id.title_search);
        author = (EditText) ma.findViewById(R.id.author_search);
        publisher = (EditText) ma.findViewById(R.id.publisher_search);
    }

    public void testPreconditions() {
        assertNotNull(title);
        assertNotNull(author);
        assertNotNull(publisher);
    }

    public void testText() {
        // simulate user action to input some value into EditText:
        final EditText title = (EditText) ma.findViewById(R.id.title_search);
        final EditText author = (EditText) ma.findViewById(R.id.author_search);
        final EditText publisher = (EditText) ma.findViewById(R.id.author_search);
        ma.runOnUiThread(new Runnable() {
            public void run() {
                title.requestFocus();
                title.setText("harry");
                author.setText("j k rowling");
                publisher.setText("penguin");
            }
        });

        // Check if the EditText is properly set:
        assertEquals("harry", title.getText().toString());
        assertEquals("j k rowling", author.getText().toString());
        assertEquals("penguin", author.getText().toString());
    }
}