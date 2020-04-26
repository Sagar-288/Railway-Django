package com.example.railway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewBookingActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    JSONObject jsonobj, objGlobal;
    String cookie;
    ArrayList<Booking> bookings;
    String url = "http://192.168.43.26:8000/";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newbooking);

        final EditText too = findViewById(R.id.to);
        final EditText from = findViewById(R.id.from);
        final EditText date = findViewById(R.id.date);
        Button search = findViewById(R.id.search);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonobj = new JSONObject();
                try {
                    jsonobj.put("date", date.getText().toString());
                    jsonobj.put("from", from.getText().toString());
                    jsonobj.put("to", too.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new BackgroundSearch().execute();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    protected class BackgroundSearch extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... x) {
            Boolean a;
            try {
                a = sendPost();
            } catch (IOException e) {
                a = false;
            } catch (JSONException e) {
                a = false;
            }
            return a;
        }

        @Override
        protected void onPostExecute(Boolean a) {

            bookings = new ArrayList<Booking>();
            try {
                int count = 1;
                while(objGlobal.has(String.valueOf(count))){
                    JSONObject ticket = new JSONObject(objGlobal.getString(String.valueOf(count)));
                    bookings.add(new Booking(ticket));
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(
                    "1111111111"
            );
            BookingAdapter bookingAdapter = new BookingAdapter(NewBookingActivity.this, bookings);
//
            ListView listView = (ListView) findViewById(R.id.listsearch);
            System.out.println(listView);
            System.out.println(bookingAdapter);
            TextView a1 = findViewById(R.id.temp);
            a1.setText("THERHERHE");
            listView.setAdapter(bookingAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    //Dessert dessert = desserts.get(i);
                    Intent intent1 = new Intent(getApplicationContext(), PassengerActivity.class);
                    JSONObject trainstatus = null;
                    try {
                        trainstatus = bookings.get(i).returnjson();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent1.putExtra("train",trainstatus.toString());
                    try {
                        intent1.putExtra("traindate", jsonobj.getString("date"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent1);
//                    Toast.makeText(getApplicationContext(), booking.trainNo, Toast.LENGTH_SHORT).show();
                }
            });

        }


        protected Boolean sendPost() throws IOException, JSONException {
            URL obj = new URL(url + "/AndriodBook");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            cookie = sharedPreferences.getString("cookie", "");
            conn.setRequestProperty("Cookie", cookie);
            conn.setDoOutput(true);

            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(jsonobj.toString());
            os.flush();
            os.close();

            conn.connect();
            StringBuffer response;
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()
            ));

            String inputline = "";
            response = new StringBuffer();

            while((inputline = in.readLine()) != null){
                response.append(inputline);
            }
            System.out.println(response);
            in.close();
            JSONObject ob = new JSONObject(response.toString());
            System.out.println(ob);
            conn.disconnect();
            if (ob.has("status")){
                if(ob.getString("status").equals("success")){
                    System.out.println("HERE");
//                    SharedPreferences.Editor share = sharedPreferences.edit();
                    objGlobal = new JSONObject(ob.getString("listbook"));
//                    share.putString("bookingdetails", ob1.toString());
//                    share.commit();
                    return true;
                }else{
                    return false;
                }
            }
            return false;

        }
    }



}
