package com.example.supriyaanand.weatherforecast;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ResultActivity extends AppCompatActivity {

    public JSONObject serverResponse;
    String state;
    String city;
    String unit;
    Spanned tempUnit;
    String windSpeedUnit;
    String visibilityUnit;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String curConDesc;
    Uri imgUrl;
    public final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_result);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        displayResults();
    }

    public void displayResults(){
        Intent intent = getIntent();
        try{
            serverResponse = new JSONObject(intent.getStringExtra("com.example.supriyaanand.weatherforecast.RESULT"));
            state = intent.getStringExtra("state");
            city = intent.getStringExtra("city");
            city = city.substring(0,1).toUpperCase() + city.substring(1).toLowerCase();
            unit = intent.getStringExtra("tempUnit");
            Log.d("ErrorWow", unit);
            if (unit.equals("celsius")) {
                tempUnit = Html.fromHtml("<sup>C</sup>");
                windSpeedUnit = "m/s";
                visibilityUnit = "km";
            } else {
                tempUnit = Html.fromHtml("<sup>F</sup>");
                windSpeedUnit = "mph";
                visibilityUnit = "mi";
            }
            System.out.println(serverResponse);
            JSONObject currently = serverResponse.getJSONObject("currently");
            JSONObject daily = serverResponse.getJSONObject("daily");
            JSONArray dailyData = daily.getJSONArray("data");
            int lowTemp = dailyData.getJSONObject(0).getInt("temperatureMin");
            int highTemp = dailyData.getJSONObject(0).getInt("temperatureMax");
            String curCon = currently.getString("summary");
            String imageIcon = currently.getString("icon");
            int curTemp = currently.getInt("temperature");
            curConDesc = curCon + ", " + Integer.toString(curTemp) + (char) 0x00B0 + tempUnit;
            ImageView curConImage = (ImageView) findViewById(R.id.curConImage);
            switch (imageIcon){
                case "clear-day": curConImage.setImageResource(R.drawable.clear);
                                    imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/clear.png");
                                    break;
                case "clear-night": curConImage.setImageResource(R.drawable.clear_night);
                                    imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/clear_night.png");
                                    break;
                case "rain": curConImage.setImageResource(R.drawable.rain);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/rain.png");
                                break;
                case "snow": curConImage.setImageResource(R.drawable.snow);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/snow.png");
                                break;
                case "sleet": curConImage.setImageResource(R.drawable.sleet);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/sleet.png");
                                break;
                case "wind": curConImage.setImageResource(R.drawable.wind);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/wind.png");
                                break;
                case "fog": curConImage.setImageResource(R.drawable.fog);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/fog.png");
                                break;
                case "cloudy": curConImage.setImageResource(R.drawable.cloudy);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/cloudy.png");
                                break;
                case "partly-cloudy-day": curConImage.setImageResource(R.drawable.cloud_day);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/cloud_day.png");
                                break;
                case "partly-cloudy-night": curConImage.setImageResource(R.drawable.cloud_night);
                                imgUrl = Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/cloud_night.png");
                                break;
            }
            curCon = curCon + " in " + city + ", " + state;
            TextView t = new TextView(this);
            t = (TextView)findViewById(R.id.curConString);
            t.setText(curCon);
            t = (TextView)findViewById(R.id.curTempString);
            t.setText(String.valueOf(curTemp) + (char) 0x00B0 + tempUnit);
            t = (TextView)findViewById(R.id.highLowTemp);
            t.setText("L:" + String.valueOf(lowTemp) + (char) 0x00B0 + " | " + "H:" + String.valueOf(highTemp) + (char) 0x00B0);
            t = (TextView)findViewById(R.id.precip);
            String precipitation_text;
            Integer precipitation = currently.getInt("precipIntensity");
            if (precipitation >= 0 && precipitation < 0.002) {
                precipitation_text = "None";
            } else if (precipitation >= 0.002 && precipitation < 0.017) {
                precipitation_text = "Very Light";
            } else if (precipitation >= 0.017 && precipitation < 0.1) {
                precipitation_text = "Light";
            } else if (precipitation >= 0.1 && precipitation < 0.4) {
                precipitation_text = "Moderate";
            } else {
                precipitation_text = "Heavy";
            }
            t.setText(precipitation_text);
            t = (TextView)findViewById(R.id.rain);
            t.setText((currently.getDouble("cloudCover") * 100) + " %");
            t = (TextView)findViewById(R.id.dewPoint);
            t.setText(String.valueOf(currently.getInt("dewPoint")) + (char) 0x00B0 + tempUnit);
            t = (TextView)findViewById(R.id.windSpeed);
            t.setText(currently.getString("windSpeed") + " " + windSpeedUnit);
            t = (TextView)findViewById(R.id.humidity);
            t.setText((currently.getDouble("humidity") * 100) + " %");
            t = (TextView)findViewById(R.id.visibility);
            t.setText(currently.getString("visibility") + " " + visibilityUnit);
            t = (TextView)findViewById(R.id.sunRiseVal);
            DateFormat sdf = new SimpleDateFormat("hh:mm a");
            sdf.setTimeZone(TimeZone.getTimeZone(serverResponse.getString("timezone")));
            String time1 = sdf.format(new Date(serverResponse.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getInt("sunriseTime") * 1000L));
            t.setText(time1);
            t = (TextView)findViewById(R.id.sunSetVal);
            String time2 = sdf.format(new Date(serverResponse.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getInt("sunsetTime") * 1000L));
            t.setText(time2);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void onMoreDetailsClicked(View view) {
        Intent intent = new Intent(com.example.supriyaanand.weatherforecast.ResultActivity.this, DetailsActivity.class);
        intent.putExtra("serverResponse", serverResponse.toString());
        intent.putExtra("state", state);
        intent.putExtra("city", city);
        intent.putExtra("unit", unit);
        startActivity(intent);
    }

    public void onShareToFB(View view){
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onCancel() {
                Toast.makeText(com.example.supriyaanand.weatherforecast.ResultActivity.this, "Post was not shared on Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(com.example.supriyaanand.weatherforecast.ResultActivity.this, "Oops, something went wrong!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Sharer.Result result) {
                if (result.getPostId() != null) {
                    Toast.makeText(com.example.supriyaanand.weatherforecast.ResultActivity.this, "Successfully shared on Facebook", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(com.example.supriyaanand.weatherforecast.ResultActivity.this, "Post was not shared on Facebook", Toast.LENGTH_SHORT).show();
                }
            }
        }, REQUEST_CODE);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Current Weather in " + city + ", " + state)
                    .setContentDescription(curConDesc)
                    .setImageUrl(imgUrl)
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onViewMapClicked(View view){
        Intent intent = new Intent(com.example.supriyaanand.weatherforecast.ResultActivity.this, MapActivity.class);
        try {
            intent.putExtra("lat", serverResponse.getString("latitude"));
            intent.putExtra("lon", serverResponse.getString("longitude"));
            startActivity(intent);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
