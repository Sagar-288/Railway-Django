package com.example.railway;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
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
import java.net.URL;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {


    JSONObject jsonobj;
    String cookie, username, password;
    String url = "http://192.168.43.26:8000/";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final EditText mfirstName = findViewById(R.id.firstName);
        final EditText mlastName  = findViewById(R.id.lastName);
        final EditText maadharNo = findViewById(R.id.aadharNo);
        final EditText mdob = findViewById(R.id.dob);
        final EditText memail = findViewById(R.id.email);
        final EditText mpasswordReg = findViewById(R.id.passwordReg);
        final EditText mtelephone = findViewById(R.id.telephone);
        final EditText mcountry = findViewById(R.id.country);
        final EditText museridReg = findViewById(R.id.userIdReg);
        Button signupbtn = findViewById(R.id.signupbutton);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonobj = new JSONObject();
                try {
                    jsonobj.put("userId", museridReg.getText().toString());
                    jsonobj.put("firstName", mfirstName.getText().toString());
                    jsonobj.put("lastName", mlastName.getText().toString());
//                    jsonobj.put("dateofbirth", mdob.getText().toString());
                    jsonobj.put("aadharNo", maadharNo.getText().toString());
                    jsonobj.put("email", memail.getText().toString());
                    jsonobj.put("telephone", mtelephone.getText().toString());
                    jsonobj.put("password", mpasswordReg.getText().toString());
                    jsonobj.put("country", mcountry.getText().toString());

//                    jsonobj.put("userId", "h1");
//                    jsonobj.put("firstName", "h1");
//                    jsonobj.put("lastName", "h1");
////                    jsonobj.put("dateofbirth", mdob.getText().toString());
//                    jsonobj.put("aadharNo", "23");
//                    jsonobj.put("email", "h1@h.com");
//                    jsonobj.put("telephone", "23");
//                    jsonobj.put("password","h1");
//                    jsonobj.put("country", "h1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                System.out.println(jsonobj.toString());
                new BackgroundLogin().execute();
            } });
    }

    @SuppressLint("StaticFieldLeak")
    private class BackgroundLogin extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... x){
            Boolean a;
            try{
                a = sendPost();
            }catch(IOException e){
                System.out.println("IO");
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


        protected Boolean sendPost() throws IOException {
            URL obj = new URL(url + "signup");


            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(jsonobj.toString());
            os.flush();
            os.close();

            conn.connect();

           return true;

        }

    }

}
