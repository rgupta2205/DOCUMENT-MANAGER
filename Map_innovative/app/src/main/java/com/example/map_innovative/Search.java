package com.example.map_innovative;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private RecyclerView searchlist;
    private EditText search_input;
    private ImageButton search_button;
    private String a[],b[],c[];
    private String full_email,email;
    private int len=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {


            setContentView(R.layout.activity_search);
            full_email=getIntent().getExtras().getString("key").toString();
            //full_email = "meet0fatel@gmail.com";
            email=full_email.substring(0,full_email.lastIndexOf('@'));
            search_input = (EditText) findViewById(R.id.search_box_input);
            search_button = (ImageButton) findViewById(R.id.search_button);

            searchlist = (RecyclerView) findViewById(R.id.search_result_list);
            searchlist.setLayoutManager(new LinearLayoutManager(Search.this));
            a="".split(" ");
            b="".split(" ");
            c="".split(" ");
            //String b[]="x x".split(" ");
            //String c[]="x x".split(" ");

            //Recycle_Adapter xyx = new Recycle_Adapter(a, b, c,);

            //searchlist.setAdapter(xyx);

            TextWatcher sipahi = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Toast.makeText(Search.this, s, Toast.LENGTH_SHORT).show();
                    searchXamppDatabase(s.toString());
                }
            };
            search_input.addTextChangedListener(sipahi);
        }
        catch (Exception e)
        {
            Log.d("meet",e.toString());
        }
    }

    private void searchXamppDatabase(String reg) {
        final String ax=full_email;
        final String bx=reg;
        try {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.154/file_uploader/main.php?lebhai_kar_bhegu=" + bx + "&email=" + ax, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        try {
                            //Log.d("meet",jsonObject.toString());
                            String v = jsonObject.getString("type");
                            String n = jsonObject.getString("name");
                            String m = jsonObject.getString("uq");
                            //Toast.makeText(getApplicationContext(),jsonObject.getString("type"),Toast.LENGTH_LONG).show();
                            lestmakeArray(v, n, m);
                            //updateTotalPostByMe(Integer.parseInt("0"+value));
                        } catch (Exception e) {
                            Log.d("meet", e.toString());
                        }
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("answer"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //progressDialog.dismiss();
                    Log.d("meet", error.toString());

                    Toast.makeText(Search.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });


            RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
            requestQueue.add(stringRequest);
        }
        catch (Exception e)
        {
            Log.d("meet",e.toString());
        }
    }

    private void lestmakeArray(String v, String n, String m) {
        a=n.trim().split(" ");
        b=v.trim().split(" ");
        c=m.trim().split(" ");

        ArrayList<String> ax=new ArrayList<>();
        ArrayList<String> bx=new ArrayList<>();
        ArrayList<String> cx=new ArrayList<>();



        if(a[0].equals(""))
        {
            Recycle_Adapter xyxy = new Recycle_Adapter(new ArrayList<>(),new ArrayList<>(), new ArrayList<>(),email,Search.this);
            Toast.makeText(Search.this,"0 item is matching with your query", Toast.LENGTH_LONG).show();
            searchlist.setAdapter(xyxy);
        }
        else
        {
            for(int i=0;i<a.length;i++)
            {
                ax.add(a[i]);
                bx.add(b[i]);
                cx.add(c[i]);
            }

            Recycle_Adapter xyxy = new Recycle_Adapter(ax, bx, cx,email,Search.this);
            Toast.makeText(Search.this,ax.size()+" item is matching with your query", Toast.LENGTH_LONG).show();
            searchlist.setAdapter(xyxy);
        }


    }
}