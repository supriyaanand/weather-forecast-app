package com.example.supriyaanand.weatherforecast;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FragmentTab2 extends Fragment {

    String city;
    String state;
    String unit;
    String tempUnit;
    JSONObject serverResponse;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab2, container, false);
        Bundle bundle = getArguments();
        try {
            serverResponse = new JSONObject(bundle.getString("serverResponse"));
            state = bundle.getString("state");
            city = bundle.getString("city");
            city = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
            unit = bundle.getString("unit");
            if(unit.equals("celsius")){
                tempUnit = "C";
            }
            else{
                tempUnit = "F";
            }
            System.out.println(serverResponse);
            JSONObject daily = serverResponse.getJSONObject("daily");
            JSONArray dailyData = daily.getJSONArray("data");

            TableLayout ll = null;
            int color = 0;

            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 1:
                        ll = (TableLayout) rootView.findViewById(R.id.day1Table);
                        color = getResources().getColor(R.color.lightBlue);
                        break;
                    case 2:
                        ll = (TableLayout) rootView.findViewById(R.id.day2Table);
                        color = getResources().getColor(R.color.lightPink);
                        break;
                    case 3:
                        ll = (TableLayout) rootView.findViewById(R.id.day3Table);
                        color = getResources().getColor(R.color.lightYellow);
                        break;
                    case 4:
                        ll = (TableLayout) rootView.findViewById(R.id.day4Table);
                        color = getResources().getColor(R.color.lightGreen);
                        break;
                    case 5:
                        ll = (TableLayout) rootView.findViewById(R.id.day5Table);
                        color = getResources().getColor(R.color.lightPurple);
                        break;
                    case 6:
                        ll = (TableLayout) rootView.findViewById(R.id.day6Table);
                        color = getResources().getColor(R.color.lightOrange);
                        break;
                    case 7:
                        ll = (TableLayout) rootView.findViewById(R.id.day7Table);
                        color = getResources().getColor(R.color.lightRed);
                        break;
                }
                TableRow row = new TableRow(getActivity());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                TextView time = new TextView(getActivity());
                DateFormat sdf = new SimpleDateFormat("EEE, MMM dd");
                sdf.setTimeZone(TimeZone.getTimeZone(serverResponse.getString("timezone")));
                String time1 = sdf.format(new Date(dailyData.getJSONObject(i).getInt("time") * 1000L));
                time.setText(time1);
                time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                time.setTypeface(null, Typeface.BOLD);
                String imageIcon = dailyData.getJSONObject(i).getString("icon");
                ImageView conImage = new ImageView(getActivity());
                switch (imageIcon) {
                    case "clear-day":
                        conImage.setImageResource(R.drawable.clear);
                        break;
                    case "clear-night":
                        conImage.setImageResource(R.drawable.clear_night);
                        break;
                    case "rain":
                        conImage.setImageResource(R.drawable.rain);
                        break;
                    case "snow":
                        conImage.setImageResource(R.drawable.snow);
                        break;
                    case "sleet":
                        conImage.setImageResource(R.drawable.sleet);
                        break;
                    case "wind":
                        conImage.setImageResource(R.drawable.wind);
                        break;
                    case "fog":
                        conImage.setImageResource(R.drawable.fog);
                        break;
                    case "cloudy":
                        conImage.setImageResource(R.drawable.cloudy);
                        break;
                    case "partly-cloudy-day":
                        conImage.setImageResource(R.drawable.cloud_day);
                        break;
                    case "partly-cloudy-night":
                        conImage.setImageResource(R.drawable.cloud_night);
                        break;
                }

                row.addView(time);
                TableRow.LayoutParams timeParams = (TableRow.LayoutParams)time.getLayoutParams();
                timeParams.span = 2;
                time.setLayoutParams(timeParams);

                RelativeLayout rl = new RelativeLayout(getActivity());
                RelativeLayout.LayoutParams lpr = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpr.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lpr.height = 100;
                lpr.width = 100;
                lpr.setMargins(0,0,30,0);
                rl.addView(conImage, lpr);

                row.addView(rl);
//                TableRow.LayoutParams imgParams = (TableRow.LayoutParams)conImage.getLayoutParams();
//                imgParams.height = 100;
//                imgParams.width = 100;
//                imgParams.gravity = Gravity.END;
//                imgParams.span = 1;
//                conImage.setLayoutParams(imgParams);

                row.setPadding(10, 10, 0, 30);
                row.setBackgroundColor(color);
                ll.addView(row);

                TableRow row2 = new TableRow(getActivity());
                row2.setLayoutParams(lp);

                TextView temp = new TextView(getActivity());
                String tempString = "Min: " + Integer.toString(dailyData.getJSONObject(i).getInt("temperatureMin")) + (char) 0x00B0 + tempUnit;
                tempString += " | Max: " + Integer.toString(dailyData.getJSONObject(i).getInt("temperatureMax")) + (char) 0x00B0 + tempUnit;
                temp.setText(tempString);
                temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                row2.addView(temp);
                TableRow.LayoutParams temp1Params = (TableRow.LayoutParams)temp.getLayoutParams();
                temp1Params.span = 3;
                temp.setLayoutParams(temp1Params);

                row2.setPadding(10, 10, 0, 30);
                row2.setBackgroundColor(color);
                ll.addView(row2);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return rootView;
    }

}