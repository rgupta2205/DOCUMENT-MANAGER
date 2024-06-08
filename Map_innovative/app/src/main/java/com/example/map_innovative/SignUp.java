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

public class SignUp extends AppCompatActivity {

    private EditText name,email,pass;
    private Button sign_up;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(SignUp.this);

        name=(EditText)findViewById(R.id.sign_up_name);
        email=(EditText)findViewById(R.id.sign_up_email);
        pass=(EditText)findViewById(R.id.sign_up_password);

        sign_up=(Button)findViewById(R.id.sign_up_btn);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String x=email.getText().toString().trim();
                String y=pass.getText().toString().trim();
                String z=name.getText().toString().trim();

                if(x.equals("") || y.equals("") || z.equals(""))
                {
                    Toast.makeText(SignUp.this,"One of the field is empty...",Toast.LENGTH_LONG).show();
                }
                if(!x.matches(emailPattern))
                {
                    Toast.makeText(SignUp.this,"Wrong email pattern...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    addUser(x,y,z);
                    //sendUserToHomeActivity();
                    //Toast.makeText(MainActivity.this,"right email pattern...",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addUser(String x, String y, String z) {
        final String a=x;
        final String b=y;
        final String c=z;

        progressDialog.setMessage("Processing user");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/mail/mail.php?add_me="+c+"&email="+a+"&pass="+b, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {

                        String value=jsonObject.getString("status").toString();
                        Log.d("meet",value);
                        if(value.equals("done"))
                            sendUserToHomeActivity();
                        else
                            sendUserToSignUpActivity();
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

                Toast.makeText(SignUp.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);

    }

    private void sendUserToSignUpActivity() {
        Toast.makeText(SignUp.this,"Something went wrong....",Toast.LENGTH_LONG).show();
        Intent x=new Intent(SignUp.this,SignUp.class);
        startActivity(x);
    }

    private void sendUserToHomeActivity() {

        Intent x=new Intent(SignUp.this,Home.class);
        startActivity(x);
    }
}