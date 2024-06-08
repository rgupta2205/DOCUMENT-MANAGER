package com.example.map_innovative;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView signup,forget;
    private Button btn;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        progressDialog=new ProgressDialog(MainActivity.this);

        email=(EditText)findViewById(R.id.log_in_email);
        password=(EditText)findViewById(R.id.log_in_password);
        signup=(TextView) findViewById(R.id.sign_up);
        forget=(TextView)findViewById(R.id.forget_password);

        btn=(Button)findViewById(R.id.log_in_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String x=email.getText().toString().trim();
                String y=password.getText().toString().trim();
                if(x.equals("") || y.equals(""))
                {
                    Toast.makeText(MainActivity.this,"One of the field is empty...",Toast.LENGTH_LONG).show();
                }
                if(!x.matches(emailPattern))
                {
                    Toast.makeText(MainActivity.this,"Wrong email pattern...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    checkuser();
                    //sendUserToHomeActivity();
                    //Toast.makeText(MainActivity.this,"right email pattern...",Toast.LENGTH_LONG).show();
                }

               // Toast.makeText(MainActivity.this,"Wrong email pattern...",Toast.LENGTH_LONG).show();

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserTosignUpActivity();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToforgetActivity();
            }
        });
    }

    private void checkuser() {
        final String x=email.getText().toString().trim();
        final String y=password.getText().toString().trim();

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
                            sendUserToHomeActivity("laaaaa");
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

                Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendUserToHomeActivity(String xy) {
        Log.d("meet",xy);
        Intent x=new Intent(MainActivity.this,Home.class);
        x.putExtra("key",email.getText().toString().trim());
        startActivity(x);
    }

    private void sendUserToforgetActivity() {
        Intent x=new Intent(MainActivity.this,ForgetPassword.class);
        startActivity(x);
    }

    private void sendUserTosignUpActivity() {
        Intent x=new Intent(MainActivity.this,SignUp.class);
        startActivity(x);
    }

    private void sendUserToMainActivity() {
        Toast.makeText(MainActivity.this,"EMAIL OR PASSWORD IS WRONG....",Toast.LENGTH_LONG).show();
        Intent x=new Intent(MainActivity.this,MainActivity.class);
        startActivity(x);
    }
}