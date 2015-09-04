package com.example.potato.hcl;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpEntity;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



public class Blah extends ActionBarActivity implements View.OnClickListener {
    private Button btnSearch;
    private EditText Name;
    HttpClient httpClient = new DefaultHttpClient();
    //private TextView msg, NameOut, DateOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.potato.hcl.R.layout.activity_blah);
        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        btnSearch = (Button) findViewById(com.example.potato.hcl.R.id.search);
        btnSearch.setOnClickListener(this);
        Name = (EditText) findViewById(com.example.potato.hcl.R.id.name);
        Typeface myTypeface= Typeface.createFromAsset(getAssets(), "Aaargh.ttf");
        TextView myTextview = (TextView)findViewById(com.example.potato.hcl.R.id.Title);
        myTextview.setTypeface(myTypeface);


        // makeGetRequest();
    }
     /*   if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.potato.hcl.R.menu.menu_blah, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.potato.hcl.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        final EditText edit = (EditText) findViewById(com.example.potato.hcl.R.id.name);
        Editable value = edit.getText();
        String stringed = value.toString();
        String changed = stringed.replaceAll(" ", "%20");
        String result = makeGetRequest(changed);

        //Create a pop-up dialog to display the result
        AlertDialog.Builder results = new AlertDialog.Builder(this);
        results.setTitle("Search Results For: " + stringed);
        CharSequence message = result;
        results.setMessage(message);
        results.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing, close dialog
            }

        });
        results.show();

        return;

    }

    private String makeGetRequest(String value) {

        String url = "http://hinch9.hinchfamily.com/~pjhinch/cgi-bin/MySQL_Connect";
        HttpClient client = new DefaultHttpClient();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", value));
        // replace with your url

        HttpResponse response;
        try {

            String paramString = URLEncodedUtils.format(params, "UTF-8");
            url += "?" + value;
            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();


            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            is.close();
            String json = sb.toString();
            System.out.println(json);
            return json;

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result" + e.toString());
        }
        return "Deepest condolences: There appear to be no entries by that name.";
    }
}