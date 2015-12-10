package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends Fragment {
    JSONObject selectedBook;
    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        String message = intent.getStringExtra(intent.EXTRA_TEXT);
        dataProcessing(message);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void dataProcessing(String message) throws JSONException{
        selectedBook = new JSONObject(message);
    }

}
