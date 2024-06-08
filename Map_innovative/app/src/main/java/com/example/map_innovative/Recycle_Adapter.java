package com.example.map_innovative;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycle_Adapter extends RecyclerView.Adapter<Recycle_Adapter.MViewHolder>
{
    String email;
    Context context;
    ArrayList<String> name,type,unique;
    //name[],type[],unique[]


    public Recycle_Adapter( ArrayList<String> name, ArrayList<String> type, ArrayList<String> unique,String email, Context context) {
        this.email = email;
        this.context = context;
        this.name = name;
        this.type = type;
        this.unique = unique;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.all_file_layout,parent,false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.nam.setText(name.get(position));
        holder.uniquenam.setText(unique.get(position));

        String x = type.get(position);
        if (x.equals("pdf")) {
            Picasso.get().load(R.drawable.pdf).into(holder.typ);
        } else if (x.equals("images")) {
            Picasso.get().load(R.drawable.image).into(holder.typ);
        } else {
            Picasso.get().load(R.drawable.word).into(holder.typ);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,unique.get(position),Toast.LENGTH_LONG).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Delete File",
                                "Download File",
                                "share file"
                        };
                //Toast.makeText(Home.this,"dddd",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("SELECT OPTION");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String chhokoro=unique.get(position);
                            chhokoro=chhokoro.substring(0,chhokoro.lastIndexOf('.'));
                            DatabaseReference userref= FirebaseDatabase.getInstance().getReference().child(email).child(chhokoro);
                            userref.removeValue();
                            deleteFilefromXamppAlso(unique.get(position),position);
                        }
                        if(which==1)
                        {
                            String chhokoro=unique.get(position);
                            String download="";
                            chhokoro=chhokoro.substring(0,chhokoro.lastIndexOf('.'));
                            DatabaseReference userref= FirebaseDatabase.getInstance().getReference().child(email).child(chhokoro);
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Toast.makeText(context,dataSnapshot.child("uid").getValue().toString(),Toast.LENGTH_LONG).show();
                                    downloadfile(dataSnapshot.child("uid").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if(which==2)
                        {
                            String chhokoro=unique.get(position);
                            String download="";
                            chhokoro=chhokoro.substring(0,chhokoro.lastIndexOf('.'));
                            DatabaseReference userref= FirebaseDatabase.getInstance().getReference().child(email).child(chhokoro);
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Toast.makeText(context,dataSnapshot.child("uid").getValue().toString(),Toast.LENGTH_LONG).show();
                                    //downloadfile(dataSnapshot.child("uid").getValue().toString());

                                    Intent intentShare = new Intent(Intent.ACTION_SEND);
                                    intentShare.setType("text/plain");
                                    intentShare.putExtra(Intent.EXTRA_SUBJECT,"My Subject Here ... ");
                                    intentShare.putExtra(Intent.EXTRA_TEXT,""+dataSnapshot.child("uid").getValue().toString());

                                    context.startActivity(Intent.createChooser(intentShare, "Shared the text ..."));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    private void downloadfile(String uid) {
        Intent khol=new Intent(Intent.ACTION_VIEW, Uri.parse(uid));
        context.startActivity(khol);
        Toast.makeText(context,"Wait We are serving to you",Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView typ;
        TextView nam,uniquenam;

        public MViewHolder(@NonNull View itemView) {

            super(itemView);
            typ=(CircleImageView)itemView.findViewById(R.id.potani_post_icon);
            nam=(TextView)itemView.findViewById(R.id.potani_post_name);
            uniquenam=(TextView)itemView.findViewById(R.id.potani_unique_name);
        }
    }





    private void deleteFilefromXamppAlso(String node,int pos) {
        final String x=email+"@gmail.com";
        final String y=node;
        this.notifyItemRemoved(pos);
        name.remove(pos);
        type.remove(pos);
        unique.remove(pos);
        this.notifyItemRangeChanged(pos,name.size());
        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://192.168.43.154/file_uploader/main.php?kadhi_de="+y+"&email="+x, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    try {
                        //Log.d("meet",jsonObject.toString());
                        String value=jsonObject.getString("le_didhu");
                        //updateTotalPostByMe(Integer.parseInt("0"+value));
                        if(value.equals("Thai gyu bhura"))
                        {

                        }

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
                //progressDialog.dismiss();
                Log.d("meet",error.toString());

                Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
}
