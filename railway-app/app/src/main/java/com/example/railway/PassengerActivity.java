package com.example.railway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

public class PassengerActivity extends AppCompatActivity{

    JSONObject data;
    String url = "http://192.168.43.26:8000/";
    SharedPreferences sharedPreferences;
    String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger);

        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);

        final TextView trainname = findViewById(R.id.train_name2);
        final TextView trainnumber = findViewById(R.id.train_number3);
        final TextView traindate = findViewById((R.id.train_date3));
        final Spinner seattype = findViewById(R.id.seatlist);
        final Spinner gender = findViewById(R.id.genderlist);
        final EditText name = findViewById(R.id.name2);
        final EditText age = findViewById(R.id.age2);
        traindate.setText(getIntent().getStringExtra("traindate"));
        JSONObject obj = null;
        try {
            obj = new JSONObject(getIntent().getStringExtra("train"));
            trainname.setText(obj.getString("trainName"));
            trainnumber.setText(obj.getString("trainNo"));

            String[] items1 = new String[]{"AC(" + obj.getString("acFare") + ")", "Sleeper(" + obj.getString("sleeperFare") + ")", "Sitter(" + obj.getString("sittingFare") + ")"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , items1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            seattype.setAdapter(adapter1);

            String[] items2 = new String[]{"Male", "Female"};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , items2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gender.setAdapter(adapter2);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button bookbtn = findViewById(R.id.bookbutton);
        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = new JSONObject();
                try {
                    data.put("passname", name.getText().toString());
                    data.put("passage", age.getText().toString());
                    data.put("senddate", traindate.getText());
                    data.put("sendnum", trainnumber.getText());
                    data.put("type", seattype.getSelectedItem());
                    data.put("gender", gender.getSelectedItem());

                    new BackgroundBook().execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    protected class BackgroundBook extends AsyncTask<Void, Void, Boolean> {

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

            if(a){
                Toast.makeText(getApplicationContext(), "Booking successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }

        }


        protected Boolean sendPost() throws IOException, JSONException {
            URL obj = new URL(url + "/AndriodPassenger");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            cookie = sharedPreferences.getString("cookie", "");
            conn.setRequestProperty("Cookie", cookie);
            conn.setDoOutput(true);

            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(data.toString());
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
                    return true;
                }else{
                    return false;
                }
            }
            return false;

        }
    }
}
