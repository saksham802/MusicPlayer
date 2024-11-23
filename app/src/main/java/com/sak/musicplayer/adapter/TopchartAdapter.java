package com.sak.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.musicplayer.R;
import com.sak.musicplayer.listner.SelectListener;
import com.sak.musicplayer.data.Topcharts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopchartAdapter extends RecyclerView.Adapter<TopchartAdapter.MyViewHolder> {
    Context context;
    SelectListener selectListener;

    public TopchartAdapter(Context context, ArrayList<Topcharts> list,SelectListener selectListener) {
        this.context = context;
        this.list = list;
        this.selectListener=selectListener;
    }

    ArrayList<Topcharts> list;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.topchart_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Topcharts  topcharts=list.get(position);
        holder.textView.setText(topcharts.getName());
        Picasso.get().load(topcharts.getCoverUrl()).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.onItemClicked(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.cover_image);
            textView=itemView.findViewById(R.id.topchart_name);
            cardView=itemView.findViewById(R.id.topchart);
        }
    }
}
