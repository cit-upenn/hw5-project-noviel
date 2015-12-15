package com.example.tristantianle.noviel2;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * This Test tests the default value of edit text is empty
 */
public class ApplicationTest1 extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity ma;
    private EditText title;
    private EditText author;
    private EditText publisher;

    public ApplicationTest1() {
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
        assertEquals("", title.getText().toString());
        assertEquals("", author.getText().toString());
        assertEquals("", publisher.getText().toString());
    }
}