package com.sparkapps.blogreader;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;

public class MainListActivity extends ListActivity {



    protected String [] mAndroidNames;
    protected String [] mBlogPostTitles;

    public static final int NUMBER_OF_POSTS = 20;
    public static final String TAG = MainListActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        Resources resources = getResources();
        mAndroidNames = resources.getStringArray(R.array.android_names);

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mAndroidNames);
        setListAdapter(adapter);

        if (isNetworkAvailable()) {
            GetBlogPostTasks getBlogPostTasks = new GetBlogPostTasks();
            getBlogPostTasks.execute();
        }
        else
        {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }




        //Toast.makeText(this,getString(R.string.default_list_nodatamessage),Toast.LENGTH_LONG).show();

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager  = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo!= null && networkInfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;
    }

    private class GetBlogPostTasks extends AsyncTask <Object, Void, String>
    {

        @Override
        protected String doInBackground(Object... params) {
            int responseCode = -1;

            try
            {
                URL blogURL = new URL ("http://blog.teamtreehouse.com/api/get_recent_summary/?count="+NUMBER_OF_POSTS);
                HttpURLConnection connection = (HttpURLConnection) blogURL.openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();
            }
            catch (MalformedURLException  e)
            {
                Log.e(TAG, "Excepption caught: ", e);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Excepption caught: ", e);
            }

            Log.i(TAG,"Code:"+responseCode);
            return "Code: " + responseCode;
        }
    }
}
