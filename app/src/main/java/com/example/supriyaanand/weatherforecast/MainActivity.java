package com.example.supriyaanand.weatherforecast;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public String[] states;
    public Spinner spinner;
    public String temperature_unit;
    public String street_address;
    public String state;
    public String city;
    public JSONObject parsedResponse;
    public final static String EXTRA_TEXT = "com.example.supriyaanand.weatherforecast.RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        states = getResources().getStringArray(R.array.states_list);
        spinner = (Spinner) findViewById(R.id.state_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, states);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int pos = spinner.getSelectedItemPosition();
                state = states[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //set default value for temperature unit
        temperature_unit = "fahrenheit";

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.celsius:
                if (checked)
                    temperature_unit = "celsius";
                break;
            case R.id.fahrenheit:
                if (checked)
                    temperature_unit = "fahrenheit";
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ServerResponse extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url[0]);
            System.out.println(url[0]);
            HttpResponse response = null;
            String result = null;
            try {
                response = client.execute(request);

                Log.d("Response of GET request", response.toString());
                HttpEntity entity = response.getEntity();
                // If the response does not enclose an entity, there is no need
                // to worry about connection release

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    result = convertStreamToString(instream);
                    instream.close();
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                parsedResponse = new JSONObject(result);
                Intent intent = new Intent(com.example.supriyaanand.weatherforecast.MainActivity.this, ResultActivity.class);
                intent.putExtra(EXTRA_TEXT, parsedResponse.toString());
                intent.putExtra("state", state);
                intent.putExtra("city", city);
                intent.putExtra("tempUnit", temperature_unit);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void search(View view) {
        String url;
        String domain_url = "http://weathertrust-env.elasticbeanstalk.com/forecast.php?";

        EditText address = (EditText) findViewById(R.id.street_address);
        street_address = address.getText().toString();
        if(street_address.length() == 0){
            TextView errorText = (TextView) findViewById(R.id.errorText);
            errorText.setText("Please enter a Street.");
            return;
        }
        EditText city_box = (EditText) findViewById(R.id.city);
        city = city_box.getText().toString();
        if(city.length() == 0){
            TextView errorText = (TextView) findViewById(R.id.errorText);
            errorText.setText("Please enter a City.");
            return;
        }
        if(spinner.getSelectedItemPosition() == 0){
            TextView errorText = (TextView) findViewById(R.id.errorText);
            errorText.setText("Please select a State.");
            return;
        }
        TextView errorText = (TextView) findViewById(R.id.errorText);
        errorText.setText("");
        url = domain_url + "&address=" + URLEncoder.encode(street_address) + "&city=" + URLEncoder.encode(city) + "&state=" + URLEncoder.encode(state) + "&degree=" + URLEncoder.encode(temperature_unit);
        new ServerResponse().execute(url);
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void loadUrl(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://forecast.io"));
        startActivity(intent);
    }

    public void launchAboutActivity(View view){
        Intent intent = new Intent(com.example.supriyaanand.weatherforecast.MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void clear(View view){
        EditText address = (EditText) findViewById(R.id.street_address);
        address.setText("");
        EditText city = (EditText) findViewById(R.id.city);
        city.setText("");
        spinner.setSelection(0);
        RadioButton tempSignC = (RadioButton) findViewById(R.id.celsius);
        tempSignC.setChecked(false);
        RadioButton tempSignF = (RadioButton) findViewById(R.id.fahrenheit);
        tempSignF.setChecked(true);
        TextView errorText = (TextView) findViewById(R.id.errorText);
        errorText.setText("");
    }
}
