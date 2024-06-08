package com.example.map_innovative;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {

    private EditText email,pass;
    private Button sub_btn, verify_btn;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forget_password);
            getSupportActionBar().hide();
            progressDialog = new ProgressDialog(ForgetPassword.this);

            email = (EditText) findViewById(R.id.forget_email);
            pass = (EditText) findViewById(R.id.confirmation);

            sub_btn = (Button) findViewById(R.id.submin_forget_btn);
            verify_btn = (Button) findViewById(R.id.verify_btn);

            pass.setVisibility(View.GONE);
            verify_btn.setVisibility(View.GONE);

            sub_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mail_the_user();
                        Toast.makeText(ForgetPassword.this, "MAIL IS SENT TO YOU PLEASE VERIFY...", Toast.LENGTH_LONG).show();
                        pass.setVisibility(View.VISIBLE);
                        verify_btn.setVisibility(View.VISIBLE);

                        email.setVisibility(View.GONE);
                        sub_btn.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.d("meet", e.toString());
                    }
                }
            });


            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkuser();
                }
            });
        }
        catch (Exception e)
        {
            Log.d("meet",e.toString());
        }
    }
    private void checkuser() {
        final String x=email.getText().toString().trim();
        final String y=pass.getText().toString().trim();

        if(y.equals("") || x.equals(""))
            return;

        progressDialog.setMessage("Processing user");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/Python_to_xampp/main.php?email="+x+"&pass="+y, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {
                        Log.d("meet",jsonObject.toString());
                        String value=jsonObject.getString("count");
                        if(Integer.parseInt(value)==1)
                            sendUserToUpdatePassword();
                        else
                            sendUserToMainActivity();
                    }
                    catch (Exception e)
                    {
                        Log.d("meet",e.toString());
                    }
                    //Toast.makeText(getApplicationContext(),jsonObject.getString("answer"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("meet",error.toString());

                Toast.makeText(ForgetPassword.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);
    }
    private void mail_the_user() {
        final String x=email.getText().toString().trim();
        progressDialog.setMessage("Processing user");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/mail/mail.php?change_password="+x, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {

                        String value=jsonObject.getString("status").toString();
                        Log.d("meet",value);
                        if(!value.equals("done"))
                            sendUserToMainActivity();

                    }
                    catch (Exception e)
                    {
                        Log.d("meet",e.toString());
                    }
                    //Toast.makeText(getApplicationContext(),jsonObject.getString("answer"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("meet",error.toString());

                Toast.makeText(ForgetPassword.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);

    }

    private void sendUserToUpdatePassword() {
        Intent x=new Intent(ForgetPassword.this,UpdatePassword.class);
        x.putExtra("key", email.getText().toString().trim());
        startActivity(x);
    }

    private void sendUserToMainActivity() {
        Toast.makeText(ForgetPassword.this,"EMAIL OR PASSWORD IS WRONG....",Toast.LENGTH_LONG).show();
        Intent x=new Intent(ForgetPassword.this,MainActivity.class);
        startActivity(x);
    }
}