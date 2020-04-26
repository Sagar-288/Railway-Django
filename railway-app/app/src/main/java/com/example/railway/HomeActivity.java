package com.example.railway;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    String url = "http://192.168.43.26:8000/";
    String username;
    JSONObject obj;
    SharedPreferences sharedPreferences;
    String cookie;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        intent = getIntent();
        System.out.println("Welcome");
        TextView usrname = findViewById(R.id.textView10);
        TextView firstName = findViewById(R.id.textView11);
        TextView lastName = findViewById(R.id.textView12);
        TextView aadharNo = findViewById(R.id.textView13);
        TextView dob = findViewById(R.id.textView14);
        TextView email = findViewById(R.id.textView15);
        TextView phone = findViewById(R.id.textView16);
        TextView country = findViewById(R.id.textView17);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String s = sharedPreferences.getString("userdetails", "");
        try {
            obj = new JSONObject(s);
            username = sharedPreferences.getString("username", "");
            usrname.setText(username);
            firstName.setText(obj.getString("firstName"));
            lastName.setText(obj.getString("lastName"));
            aadharNo.setText(obj.getString("aadharNo"));
            dob.setText(obj.getString("dateofbirth"));
            email.setText(obj.getString("email"));
            phone.setText(obj.getString("telephone"));
            country.setText(obj.getString("country"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button bookedj = findViewById(R.id.bookedj);
        Button newj = findViewById(R.id.newj);
        Button logoutbtn = findViewById(R.id.logoutbutton);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HomeActivity.BackgroundLogout().execute();
            }
        });
        newj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), NewBookingActivity.class);
                startActivity(intent1);
            }
        });
        bookedj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HomeActivity.BackgroundDetails().execute();

            }
        });

    }

//    public void OnSaveInstanceState(Bundle savedInstanceState){
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putString("userdetails", obj.toString());
//        savedInstanceState.putString("username", username);
//
//    }

    @SuppressLint("StaticFieldLeak")
    private class BackgroundDetails extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... x) {
            Boolean a;
            try {
                a = sendPostBook();
            } catch (IOException e) {
                a = false;
            } catch (JSONException e) {
                a = false;
            }
            return a;
        }

        @Override
        protected void onPostExecute(Boolean a) {

            if (a) {
                Intent intent = new Intent(getApplicationContext(), ViewBookingActivity.class);
                startActivity(intent);
            }
        }


        protected Boolean sendPostBook() throws IOException, JSONException {
            URL obj = new URL(url + "/AndriodHome");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            cookie = sharedPreferences.getString("cookie", "");
            conn.setRequestProperty("Cookie", cookie);
            conn.setDoOutput(true);

            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
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
            conn.disconnect();
            if (ob.has("status")){
                if(ob.getString("status").equals("success")){
                    System.out.println("HERE");
                    SharedPreferences.Editor share = sharedPreferences.edit();
                    JSONObject ob1 = new JSONObject(ob.getString("bookings"));
                    share.putString("bookingdetails", ob1.toString());
                    share.commit();
                    return true;
                }else{
                    return false;
                }
            }
            return false;

        }
    }




@SuppressLint("StaticFieldLeak")
    private class BackgroundLogout extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... x){
            Boolean a;
            try{
                a = sendPostLogout();
            }catch(IOException e){
                a = false;
            }catch(JSONException e){
                a = false;
            }
            return a;
        }

        @Override
        protected  void onPostExecute(Boolean a){

            if(a){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }


        protected Boolean sendPostLogout() throws IOException, JSONException {
            URL obj = new URL(url + "/AndriodLogout");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);

            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.flush();
            os.close();

            conn.connect();


            return true;

        }

    }

}
