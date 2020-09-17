package com.portalmilagresecuras.persistencia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.portalmilagresecuras.R;
import com.portalmilagresecuras.modelo.Posts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<Posts> articlesList;
    private LayoutInflater inflater;

    public Adaptador(Context context, List<Posts> articles){
        this.inflater = LayoutInflater.from(context);
        this.articlesList = articles;
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = inflater.inflate(R.layout.cards, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ViewHolder holders, int position) {
        final ViewHolder holder = holders;
        holder.title.setText(articlesList.get(position).getTitle());
        Picasso.get().load(articlesList.get(position).getUrlToImage()).into(holder.thumbnail);
        System.out.println(articlesList.get(position).getUrlToImage());
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener onItemClickListener;
        TextView title;
        ImageView thumbnail;


        public ViewHolder(@NonNull final View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            thumbnail = itemView.findViewById(R.id.postImage);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(articlesList.get(getAdapterPosition()).getUrl()));
            v.getContext().startActivity(intent);
        }
    }
}
