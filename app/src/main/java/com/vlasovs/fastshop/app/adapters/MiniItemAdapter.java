package com.vlasovs.fastshop.app.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.vlasovs.fastshop.app.activities.HomeActivity;
import com.vlasovs.fastshop.app.activities.ItemActivity;
import com.vlasovs.fastshop.app.classes.Item;

import java.util.ArrayList;
import java.util.Locale;

public class MiniItemAdapter extends RecyclerView.Adapter<MiniItemAdapter.ViewHolder> {

    private ArrayList<Item> miniItems;
    private Context context;

    public MiniItemAdapter(Context context, ArrayList<Item> miniItems) {
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
    public void onBindViewHolder(@NonNull MiniItemAdapter.ViewHolder holder, final int position) {

        holder.textReviews.setText("(" + String.format(Locale.getDefault(), "%d", miniItems.get(position).getReviews()) + ")");
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f", miniItems.get(position).getPrice())+"€");
        holder.ratingBar.setRating(miniItems.get(position).getRating());
        holder.textName.setText(miniItems.get(position).getName());
        Glide.with(context).load(miniItems.get(position).getPictureURL()).into(holder.imageView);

        holder.miniItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("item", miniItems.get(position));
                intent.putExtra("userid", HomeActivity.user.getID());
                context.startActivity(intent);
            }
        });
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
