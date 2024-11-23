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

import com.sak.musicplayer.R;
import com.sak.musicplayer.listner.SelectListnerGolden;
import com.sak.musicplayer.data.GoldenEra;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GoldenEraAdapter extends RecyclerView.Adapter<GoldenEraAdapter.ViewHolder> {
    public GoldenEraAdapter(Context context, ArrayList<GoldenEra> list, SelectListnerGolden listnerGolden) {
        this.context = context;
        this.list = list;
        this.listnerGolden = listnerGolden;
    }

    private Context context;
   private ArrayList<GoldenEra> list;
   private SelectListnerGolden listnerGolden;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.goldenera,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoldenEra goldenEra=list.get(position);
        holder.name.setText(goldenEra.getName());
        Picasso.get().load(goldenEra.getCoverUrl())
                .into(holder.cover);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listnerGolden.onItemClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView cover;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.golden_era_name);
            cover=itemView.findViewById(R.id.golden_era_cover_image);
            layout=itemView.findViewById(R.id.goldenclick);

        }
    }
}
