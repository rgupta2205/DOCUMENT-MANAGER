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

public class UpdatePassword extends AppCompatActivity {

    private EditText pass,repass;
    private Button update;
    private String email;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        progressDialog=new ProgressDialog(UpdatePassword.this);
        email=getIntent().getExtras().get("key").toString();
        pass=(EditText)findViewById(R.id.update_pass);
        repass=(EditText)findViewById(R.id.update_again_pass);

        update=(Button)findViewById(R.id.update_pass_btn);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x=pass.getText().toString();
                String y=repass.getText().toString();
                if(x.equals("") || y.equals(""))
                {
                    Toast.makeText(UpdatePassword.this,"One of the field is empty...",Toast.LENGTH_LONG).show();
                }
                if(!x.equals(y))
                {
                    Toast.makeText(UpdatePassword.this,"Both password are not matched...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    chanePassword();
                }
            }
        });

    }

    private void chanePassword() {
        final String x=email.trim();
        final String y=pass.getText().toString().trim();

        progressDialog.setMessage("Processing user");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/mail/mail.php?update_pass="+x+"&pass="+y, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {
                        Log.d("meet",jsonObject.toString());
                        String value=jsonObject.getString("kamkaj");
                        value=value.trim();
                        //Log.d("meet",value);
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("status"),Toast.LENGTH_LONG).show();
                        if(value.equals("done"))
                            sendUserToHomeActivity();
                        else
                            sendUserToUpadateActivity();
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

                Toast.makeText(UpdatePassword.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(UpdatePassword.this);
        requestQueue.add(stringRequest);
    }

    private void sendUserToUpadateActivity() {
        Intent x=new Intent(UpdatePassword.this,UpdatePassword.class);
        startActivity(x);
    }

    private void sendUserToHomeActivity() {
        Toast.makeText(UpdatePassword.this,"Password Update Successfully...",Toast.LENGTH_LONG).show();
        Intent x=new Intent(UpdatePassword.this,MainActivity.class);
        startActivity(x);
    }

}