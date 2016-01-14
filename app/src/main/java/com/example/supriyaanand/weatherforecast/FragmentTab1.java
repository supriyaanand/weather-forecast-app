package com.example.supriyaanand.weatherforecast;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.ImageView;
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

public class FragmentTab1 extends Fragment {

    String city;
    String state;
    String unit;
    String tempUnit;
    JSONObject serverResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab1, container, false);
        int numOfHours = 24;
        populateHoursTable(rootView, numOfHours);

        return rootView;
    }

    private void populateHoursTable(View rootView, int numOfHours) {
        if(numOfHours > 24){
            TableLayout table = (TableLayout) rootView.findViewById(R.id.hoursTable);
            table.removeAllViews();
        }
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
            JSONObject hourly = serverResponse.getJSONObject("hourly");
            JSONArray hourlyData = hourly.getJSONArray("data");

            TableLayout ll = (TableLayout) rootView.findViewById(R.id.hoursTable);

            TableRow row = new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 1);
            row.setLayoutParams(lp);
            TextView time = new TextView(getActivity());
            time.setText("Time");
            time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            TextView summary = new TextView(getActivity());
            summary.setText("Summary");
            summary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            TextView temp = new TextView(getActivity());
            temp.setText("Temp" +"(" + (char) 0x00B0 + tempUnit + ")    ");
            temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            row.addView(time);
            TableRow.LayoutParams params = (TableRow.LayoutParams)time.getLayoutParams();
            params.span = 3;
            time.setLayoutParams(params);

            row.addView(summary);
            TableRow.LayoutParams sumParams = (TableRow.LayoutParams)summary.getLayoutParams();
            sumParams.span = 3;
            summary.setLayoutParams(sumParams);

            row.addView(temp);
            TableRow.LayoutParams tempParams = (TableRow.LayoutParams)temp.getLayoutParams();
            tempParams.span = 3;
            tempParams.gravity = Gravity.RIGHT;
            temp.setLayoutParams(tempParams);
            row.setPadding(0, 15, 0, 15);
            int lightBlue = getResources().getColor(R.color.lightBlue);
            row.setBackgroundColor(lightBlue);
            ll.addView(row);
            TableRow row2 = new TableRow(getActivity());
            row2.setPadding(0, 15, 0, 15);
            ll.addView(row2);

            for (int i = 0; i < numOfHours; i++) {

                row = new TableRow(getActivity());
                row.setLayoutParams(lp);
                if(((i+1) % 2) != 0){
                    row.setBackgroundColor(Color.LTGRAY);
                }
                time = new TextView(getActivity());
                DateFormat sdf = new SimpleDateFormat("hh:mm a");
                sdf.setTimeZone(TimeZone.getTimeZone(serverResponse.getString("timezone")));
                String time1 = sdf.format(new Date(hourlyData.getJSONObject(i).getInt("time") * 1000L));
                time.setText(time1);
                time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                String imageIcon = hourlyData.getJSONObject(i).getString("icon");
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
                timeParams.span = 3;
                time.setLayoutParams(timeParams);

                row.addView(conImage);
                TableRow.LayoutParams imgParams = (TableRow.LayoutParams)conImage.getLayoutParams();
                imgParams.span = 3;
                imgParams.height = 100;
                imgParams.width = 100;
                conImage.setLayoutParams(imgParams);

                temp = new TextView(getActivity());
                temp.setText(Integer.toString(hourlyData.getJSONObject(i).getInt("temperature")) + "    ");
                temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                row.addView(temp);
                TableRow.LayoutParams temp1Params = (TableRow.LayoutParams)temp.getLayoutParams();
                temp1Params.span = 3;
                temp1Params.gravity = Gravity.END;
                temp.setLayoutParams(temp1Params);

                row.setPadding(0, 15, 0, 15);
                ll.addView(row);
            }

            if(numOfHours < 48) {
                TableRow plusRow = new TableRow(getActivity());
                plusRow.setLayoutParams(lp);
                ImageView plus = new ImageView(getActivity());
                plus.setImageResource(R.drawable.plus);
                plus.setOnClickListener(ClickListener);
                plusRow.addView(plus);
                TableRow.LayoutParams plusParams = (TableRow.LayoutParams) plus.getLayoutParams();
                plusParams.span = 9;
                plusParams.gravity = Gravity.CENTER;
                plusParams.width = 100;
                plusParams.height = 100;
                plusRow.setId(1000 + 24);
                plus.setLayoutParams(plusParams);
                plusRow.setPadding(0, 15, 0, 15);
                ll.addView(plusRow);
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            populateHoursTable(v.getRootView(), 48);
        }
    };
}
