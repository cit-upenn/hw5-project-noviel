package com.example.tristantianle.noviel2;

/**
 * This is a interface to define the recall data from the background thread
 */
public interface AsyncResponse {
    void processFinish(String[] output);
}