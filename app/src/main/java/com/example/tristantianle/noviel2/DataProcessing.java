package com.example.tristantianle.noviel2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TristanTianle on 12/11/2015.
 */
public class DataProcessing extends AsyncTask<String, Void, String[]> {
    private final String LOG_TAG = DataProcessing.class.getSimpleName();
    private JSONObject bookJson;
    private ArrayAdapter<String> mForecastAdapter;
//    private ArrayList<String> weekForecast= new ArrayList<>();
    public URL url;
    private Context myContext;
    private ListView listview;
    public AsyncResponse delegate = null;

    public String getURL() {
        Log.d(LOG_TAG, "getURL: " + url);
        return url.toString();
    }
    public DataProcessing(Context context,ListView listview){
        this.myContext = context;
        this.listview = listview;
    }

    private String[] getBookDataFromJson(String titleJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String RESULTS = "results";
        final String TITLE = "title";
        final String AUTHOR = "author";

        bookJson = new JSONObject(titleJsonStr);
        int numResults = bookJson.getInt("num_results");
        JSONArray bookTitleArray = bookJson.getJSONArray(RESULTS);
        if (numResults > 20) {
            numResults = 20;
        }
        String[] resultStrs = new String[numResults];
        for (int i=0;i<numResults;i++) {
            resultStrs[i] = bookTitleArray.getJSONObject(i).getString(TITLE) + "\nBy ";
            resultStrs[i] += bookTitleArray.getJSONObject(i).getString(AUTHOR);
        }
        return resultStrs;

    }
    @Override
    protected String[] doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String titleJsonStr = null;


        try {
            final String NYTIMES_BASE_URL = "http://api.nytimes.com/svc/books/v2/lists/best-sellers/history.json?";
            final String TITLE_PARAM = "title";
            final String APPKEY_PARAM = "api-key";

            Uri builtUri = Uri.parse(NYTIMES_BASE_URL).buildUpon()
                    .appendQueryParameter(TITLE_PARAM, params[0])
                    .appendQueryParameter(APPKEY_PARAM, "de340fc0244bbb1503a07c665b9903dc:15:65808863")
                    .build();

            url = new URL(builtUri.toString());

            // print out the query in debug mode
            Log.d(LOG_TAG, "url =" + url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            titleJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getBookDataFromJson(titleJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        delegate.processFinish(result);

        if (result != null) {
            mForecastAdapter = new ArrayAdapter<String>(
                    SearchList.getContext(), // The current context (this activity)
                    R.layout.list_item_result, // The name of the layout ID.
                    R.id.list_item_forecast_textview, // The ID of the textview to populate.
                    result);

            listview.setAdapter(mForecastAdapter);
        }

    }
}
