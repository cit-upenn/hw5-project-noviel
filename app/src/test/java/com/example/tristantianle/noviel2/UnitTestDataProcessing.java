package com.example.tristantianle.noviel2;

import android.widget.ListView;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTestDataProcessing {
    private ListView listview;

    @Test
    public void titleCheck() throws Exception {
        DataProcessing dp = new DataProcessing(listview);
        // Test case #1
        dp.execute("love");
        dp.get(1000, TimeUnit.MILLISECONDS);
        assertEquals("http://api.themoviedb.org/3/search/movie?query=love&api_key=001da01ae11dcca44743bde0ad8301cd", dp.getURL());
    }

}