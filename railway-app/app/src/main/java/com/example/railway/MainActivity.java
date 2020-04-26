package com.example.railway;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    JSONObject jsonobj;
    String cookie, username, password;
    String url = "http://192.168.43.26:8000/";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText muserid = findViewById(R.id.userId);
        final EditText mpassword = findViewById(R.id.password);
        Button mloginbtn = findViewById(R.id.loginbutton);
        Button mregisterbtn = findViewById(R.id.registerbutton);
        sharedpreferences = getSharedPreferences("shared", MODE_PRIVATE);

        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = muserid.getText().toString();
                password = mpassword.getText().toString();
                jsonobj = new JSONObject();
                try {
                    jsonobj.put("userId", username);
                    jsonobj.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new BackgroundLogin().execute();
            }
        });

    }
    @SuppressLint("StaticFieldLeak")
    private class BackgroundLogin extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... x){
            Boolean a;
            try{
                a = sendPost();
            }catch(IOException e){
                a = false;
            }catch(JSONException e){
                a = false;
            }
            System.out.println(a);
            return a;
        }

        @Override
        protected  void onPostExecute(Boolean a){

            if(a){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("url", url + "api/");
                intent.putExtra("msg","logging");
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }


        protected Boolean sendPost() throws IOException, JSONException {
            URL obj = new URL(url + "/AndriodLog");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            con.setDoOutput(true);

            OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
            os.write(jsonobj.toString());
            os.flush();
            os.close();

            StringBuffer response;
            cookie = con.getHeaderField("Set-Cookie");
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()
            ));

            String inputline = "";
            response = new StringBuffer();

            while((inputline = in.readLine()) != null){
                response.append(inputline);
            }
            System.out.println(response);
            in.close();
            JSONObject ob = new JSONObject(response.toString());
            System.out.println(ob.getString("status"));
            if (ob.has("status")){
                if(ob.getString("status").equals("success")){
                    System.out.println("HERE");
                    SharedPreferences.Editor share = sharedpreferences.edit();
                    share.putString("userdetails", ob.getString("userdetails"));
                    share.putString("cookie",cookie);
                    share.putString("username", username);
                    share.commit();
                    return true;
                }else{
                    return false;
                }
            }
            con.disconnect();

            return false;

        }

    }
}

