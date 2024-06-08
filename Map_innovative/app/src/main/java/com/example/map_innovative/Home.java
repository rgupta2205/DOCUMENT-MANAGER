package com.example.map_innovative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    private CircleImageView upload,search;
    private String email, checker, myurl,download_url,file_name,full_email;
    private Uri fileurl;
    private StorageTask uploadTask;
    private RecyclerView uploadlist;
    //private String Checker;
    private ProgressDialog progressDialog;
    private int total_post_by_me=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        uploadlist=(RecyclerView)findViewById(R.id.hopage_all_list);
        uploadlist.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Home.this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        uploadlist.setLayoutManager(linearLayoutManager);

        progressDialog=new ProgressDialog(Home.this);
        try {
            upload = (CircleImageView) findViewById(R.id.upload_document);
            search = (CircleImageView) findViewById(R.id.search_document);
            full_email=getIntent().getExtras().getString("key").toString();
            //email="meet0fatel";
            //full_email="meet0fatel@gmail.com";
            email=full_email.substring(0,full_email.lastIndexOf('@'));
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    countTotalPostByMe();

                    CharSequence options[] = new CharSequence[]
                            {
                                    "Images",
                                    "PDF Files",
                                    "MS Word Files"
                            };
                    //Toast.makeText(Home.this,"dddd",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle("SELECT FILE");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                checker = "images";
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image"), 438);
                            }
                            if (which == 1) {
                                checker = "pdf";
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("application/pdf");
                                startActivityForResult(Intent.createChooser(intent,"Select PDG"), 438);
                            }
                            if (which == 2) {
                                checker = "docx";
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("application/msword");
                                startActivityForResult(Intent.createChooser(intent,"Select DOCX"), 438);
                            }

                        }
                    });
                    builder.show();
                }
            });

            displayfile();

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.d("meet",xy);
                    Intent x=new Intent(Home.this,Search.class);
                    x.putExtra("key",full_email.trim());
                    startActivity(x);
                }
            });

        } catch (Exception e) {
            Log.d("meet", e.toString());
        }


    }

    private void displayfile() {

            DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("meet0fatel");
            FirebaseRecyclerOptions<WareHouse> options = new FirebaseRecyclerOptions.Builder<WareHouse>().setQuery(databaseRef, WareHouse.class).build();

            FirebaseRecyclerAdapter<WareHouse,WareHouseViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<WareHouse, WareHouseViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull WareHouseViewHolder wareHouseViewHolder, int i, @NonNull WareHouse wareHouse) {




                        wareHouseViewHolder.name.setText(wareHouse.getName());
                        wareHouseViewHolder.uniquename.setText(wareHouse.getNode());


                        wareHouseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent khol=new Intent(Intent.ACTION_VIEW,Uri.parse(wareHouse.getUid()));
                                wareHouseViewHolder.itemView.getContext().startActivity(khol);
                                Toast.makeText(Home.this,"Wait We are serving to you",Toast.LENGTH_LONG).show();
                            }
                        });


                        wareHouseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Delete File"
                                        };
                                //Toast.makeText(Home.this,"dddd",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                                builder.setTitle("SELECT OPTION");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            try {
                                                String x=wareHouse.getNode();
                                                x=x.substring(0,x.lastIndexOf("."));
                                                databaseRef.child(x).removeValue();
                                                deleteFilefromXamppAlso(wareHouse.getNode());
                                            }
                                            catch (Exception e)
                                            {
                                                Log.d("meet",e.toString());
                                            }

                                        }
                                    }
                                });
                                builder.show();
                                return false;
                            }
                        });

                        Log.d("meet", wareHouse.getType().trim());

                        String x = wareHouse.getType().trim();
                       if (x.equals("pdf")) {
                            Picasso.get().load(R.drawable.pdf).into(wareHouseViewHolder.type);
                        } else if (x.equals("images")) {
                            Picasso.get().load(R.drawable.image).into(wareHouseViewHolder.type);
                        } else {
                            Picasso.get().load(R.drawable.word).into(wareHouseViewHolder.type);
                        }

                }

                @NonNull
                @Override
                public WareHouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_file_layout,parent,false);
                    WareHouseViewHolder xy=new WareHouseViewHolder(view);

                    return  xy;
                }
            };

            uploadlist.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();


    }

    private void deleteFilefromXamppAlso(String node) {
        final String x=full_email;
        final String y=node;

        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/file_uploader/main.php?kadhi_de="+y+"&email="+x, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {
                        //Log.d("meet",jsonObject.toString());
                        //String value=jsonObject.getString("le_gani_didhu");
                        //updateTotalPostByMe(Integer.parseInt("0"+value));
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

                Toast.makeText(Home.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(Home.this);
        requestQueue.add(stringRequest);
    }

    public static class WareHouseViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView type;
        TextView name,uniquename;

        public WareHouseViewHolder(@NonNull View itemView) {
            super(itemView);
            type=(CircleImageView)itemView.findViewById(R.id.potani_post_icon);
            name=(TextView)itemView.findViewById(R.id.potani_post_name);
            uniquename=(TextView)itemView.findViewById(R.id.potani_unique_name);
        }
    }

    private void countTotalPostByMe() {
        final String x=full_email;
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/file_uploader/main.php?gani_ne_k="+x, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {
                        Log.d("meet",jsonObject.toString());
                        String value=jsonObject.getString("le_gani_didhu");
                        updateTotalPostByMe(Integer.parseInt("0"+value));
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

                Toast.makeText(Home.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(Home.this);
        requestQueue.add(stringRequest);

    }

    private void updateTotalPostByMe(int parseInt) {
        total_post_by_me=parseInt;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.setMessage("Processing user");
        progressDialog.show();
        if (requestCode == 438 && data != null) {
            fileurl = data.getData();
            File x=new File(fileurl.toString());
            String full_name=fileurl.getLastPathSegment().toString();
            String arr[]=full_name.split("/");
            String name=arr[arr.length-1];


            Cursor returnCursor = getContentResolver().query(fileurl, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            name = returnCursor.getString(nameIndex);
            Log.d("meet",name);
            file_name=name;

            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(email);


            if(checker.equals("images"))
            {
                final StorageReference filepath=storageReference.child(total_post_by_me+name);

                filepath.putFile(fileurl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url=uri.toString();
                                    Log.d("meet",download_url);
                                    savingDocumentInfromationToDatabse();
                                }
                            });
                        }
                    }
                });
            }
            else if(checker.equals("pdf"))
            {
                final StorageReference filepath=storageReference.child(total_post_by_me+name);

                filepath.putFile(fileurl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url=uri.toString();
                                    Log.d("meet",download_url);
                                    savingDocumentInfromationToDatabse();
                                }
                            });
                        }
                    }
                });
            }
            else
            {
                final StorageReference filepath=storageReference.child(total_post_by_me+name);

                filepath.putFile(fileurl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url=uri.toString();
                                    Log.d("meet",download_url);
                                    savingDocumentInfromationToDatabse();
                                }
                            });
                        }
                    }
                });
            }

        }
    }

    private void savingDocumentInfromationToDatabse() {
        Log.d("meet","Called ");

        try {
            int index=file_name.lastIndexOf('.');
            String pelanu=file_name.substring(0,index);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(email).child(total_post_by_me+pelanu);
            HashMap postMap = new HashMap();
            postMap.put("uid", download_url);
            postMap.put("node", total_post_by_me+file_name);
            postMap.put("name",pelanu);
            postMap.put("type",checker);
            Log.d("meet"," "+file_name+" "+total_post_by_me);
            databaseReference.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Home.this, "done kam", Toast.LENGTH_LONG).show();
                }
            });
            updateXamppDatabase();
        }
        catch (Exception e)
        {
            Log.d("meet",e.toString());
            progressDialog.dismiss();
        }


    }

    private void updateXamppDatabase() {
        final String a=full_email;
        final String b="\""+checker+"\"";
        final String c="\""+file_name+"\"";
        final String d="\""+total_post_by_me+""+file_name+"\"";
        Log.d("meet","http://192.168.43.154/file_uploader/main.php?file_muki_de="+a+"&location"+download_url+"&name"+c+"&unique"+d);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/file_uploader/main.php?file_muki_de="+a+"&location="+b+"&name="+c+"&unique="+d, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {
                        //Log.d("meet",jsonObject.toString());
                        //String value=jsonObject.getString("le_gani_didhu");
                        //updateTotalPostByMe(Integer.parseInt("0"+value));
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

                Toast.makeText(Home.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(Home.this);
        requestQueue.add(stringRequest);
    }
}