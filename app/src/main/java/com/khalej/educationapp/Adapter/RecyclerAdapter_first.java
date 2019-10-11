package com.khalej.educationapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.khalej.educationapp.Activity.first_second_category;
import com.khalej.educationapp.R;
import com.khalej.educationapp.model.contact_home_realm;

import java.util.List;


public class RecyclerAdapter_first extends RecyclerView.Adapter<RecyclerAdapter_first.MyViewHolder> {
    Typeface myTypeface;
    private Context context;
    List<contact_home_realm> contactslist;


    public RecyclerAdapter_first(Context context, List<contact_home_realm> contactslist){
        this.contactslist=contactslist;
        this.context=context;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_circle_list,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        myTypeface = Typeface.createFromAsset(context.getAssets(), "Droid.ttf");

        holder.Name.setText(contactslist.get(position).getname());
        holder.Name.setTypeface(myTypeface);
        Glide.with(context).load(contactslist.get(position).getImg()).error(R.drawable.log).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, first_second_category.class);
                intent.putExtra("name",contactslist.get(position).getname());
                intent.putExtra("id",contactslist.get(position).getId());
                context.startActivity(intent);
            }

        });

    }
    @Override
    public int getItemCount() {
        return contactslist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            Name=(TextView)itemView.findViewById(R.id.name);
            image=(ImageView)itemView.findViewById(R.id.photo);

        }
    }}