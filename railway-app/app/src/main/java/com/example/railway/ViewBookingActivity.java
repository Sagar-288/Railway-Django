package com.example.railway;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewBookingActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.viewbooking);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String s = sharedPreferences.getString("bookingdetails", "");

        ArrayList<Journey> journeys = new ArrayList<Journey>();
        try {
            JSONObject obj = new JSONObject(s);
            int count = 1;
            while(obj.has(String.valueOf(count))){
                JSONObject ticket = new JSONObject(obj.getString(String.valueOf(count)));
                journeys.add(new Journey(ticket));
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JourneyAdapter journeyAdapter = new JourneyAdapter(this, journeys);

        ListView listView = (ListView) findViewById(R.id.listjourneys);
        listView.setAdapter(journeyAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
