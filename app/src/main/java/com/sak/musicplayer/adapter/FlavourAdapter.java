package com.sak.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.musicplayer.listner.SelectListnterFlavour;
import com.sak.musicplayer.data.Flavours;
import com.sak.musicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlavourAdapter extends RecyclerView.Adapter<FlavourAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Flavours> list;
    private SelectListnterFlavour selectListnterFlavour;

    // Constructor to initialize context and list
    public FlavourAdapter(Context context, ArrayList<Flavours> flavoursArrayList,SelectListnterFlavour listnterFlavour) {
        this.context = context;
        this.list = flavoursArrayList; // Assign the passed list to the adapter’s internal list
        this.selectListnterFlavour=listnterFlavour;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(context).inflate(R.layout.flavourcat_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get the current item from the list
        Flavours flavours = list.get(position);

        holder.textView.setText(flavours.getName());

        // Use Picasso to load images efficiently with placeholder and error handling
        Picasso.get()
                .load(flavours.getCoverUrl())
                .into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListnterFlavour.onItemClicked(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return 0 if list is null to prevent NullPointerException
        return (list != null) ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.flavour_name);
            imageView = itemView.findViewById(R.id.flavour_cover_image);
            linearLayout=itemView.findViewById(R.id.flavourclick);
        }
    }
}
