package com.portalmilagresecuras.persistencia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.portalmilagresecuras.R;
import com.portalmilagresecuras.modelo.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.portalmilagresecuras.persistencia.ApiClient.dateFormat;

public class AdaptadorComent extends RecyclerView.Adapter<AdaptadorComent.ViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private OnItemClickListener onItemClickListener;
    private List<Comment> articlesList;
    private LayoutInflater inflater;

    public AdaptadorComent(Context context, List<Comment> articles){
        this.inflater = LayoutInflater.from(context);
        this.articlesList = articles;
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = inflater.inflate(R.layout.ichat, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorComent.ViewHolder holders, int position) {
        final ViewHolder holder = holders;
        Picasso.get().load(articlesList.get(position).getPicture()).into(holder.circleImageView);
        holder.tvUsername.setText(articlesList.get(position).getUsuario_name());
        holder.tvComment.setText(articlesList.get(position).getComentario_coment());
        holder.tvPublished.setText(dateFormat(articlesList.get(position).getComentario_published()));
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
        TextView tvUsername, tvComment, tvPublished;
        CircleImageView circleImageView;


        public ViewHolder(@NonNull final View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            circleImageView = itemView.findViewById(R.id.profileImage);
            tvUsername = itemView.findViewById(R.id.username);
            tvComment = itemView.findViewById(R.id.comentario);
            tvPublished = itemView.findViewById(R.id.published);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null){
                if (currentUser.getPhoneNumber() == articlesList.get(getAdapterPosition()).getUsuario_phone()){

                }
                System.out.println(currentUser.getPhoneNumber() + " " + articlesList.get(getAdapterPosition()).getUsuario_phone());
            }
        }
    }
}
