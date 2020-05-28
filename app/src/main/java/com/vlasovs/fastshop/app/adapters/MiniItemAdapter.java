package com.vlasovs.fastshop.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.classes.MiniItem;

import java.util.ArrayList;
import java.util.Locale;

public class MiniItemAdapter extends RecyclerView.Adapter<MiniItemAdapter.ViewHolder> {

    ArrayList<MiniItem> miniItems;
    Context context;

    public MiniItemAdapter(Context context, ArrayList<MiniItem> miniItems) {
        this.miniItems = miniItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mini, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MiniItemAdapter.ViewHolder holder, int position) {
     //   holder.imageView.setImageResource(miniItems.get(position).getPicture());
        holder.textReviews.setText("(" + String.format(Locale.getDefault(), "%d", miniItems.get(position).getReviews()) + ")");
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f", miniItems.get(position).getPrice())+"€");
        holder.ratingBar.setRating(miniItems.get(position).getRating());
        holder.textName.setText(miniItems.get(position).getName());
        Glide.with(context).load(miniItems.get(position).getPictureURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return miniItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView miniItemCard;
        ImageView imageView;
        TextView textName;
        RatingBar ratingBar;
        TextView textPrice;
        TextView textReviews;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            miniItemCard = itemView.findViewById(R.id.mini_item_card);
            imageView = itemView.findViewById(R.id.mini_item_picture);
            textName = itemView.findViewById(R.id.mini_item_name);
            ratingBar = itemView.findViewById(R.id.mini_item_rating);
            textPrice = itemView.findViewById(R.id.mini_item_price);
            textReviews = itemView.findViewById(R.id.mini_item_review_amount);
        }
    }
}
