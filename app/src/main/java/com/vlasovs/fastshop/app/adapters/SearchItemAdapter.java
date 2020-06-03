package com.vlasovs.fastshop.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.activities.ItemActivity;
import com.vlasovs.fastshop.app.activities.SearchActivity;
import com.vlasovs.fastshop.app.classes.Item;

import java.util.ArrayList;
import java.util.Locale;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private ArrayList<Item> searchItems;
    private Context context;
    private int userID;

    public SearchItemAdapter(Context context, ArrayList<Item> searchItems, int userID) {
        this.searchItems = searchItems;
        this.context = context;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemAdapter.ViewHolder holder, final int position) {

        holder.textReviews.setText("Reviews: " + String.format(Locale.getDefault(), "%d", searchItems.get(position).getReviews()));
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f", searchItems.get(position).getPrice())+"€");
        holder.ratingBar.setRating(searchItems.get(position).getRating());
        holder.textName.setText(searchItems.get(position).getName());
        Glide.with(context).load(searchItems.get(position).getPictureURL()).into(holder.imageView);

        holder.searchItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("item", searchItems.get(position));
                intent.putExtra("userid", userID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView searchItemCard;
        ImageView imageView;
        TextView textName;
        RatingBar ratingBar;
        TextView textPrice;
        TextView textReviews;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            searchItemCard = itemView.findViewById(R.id.searchItemCard);
            imageView = itemView.findViewById(R.id.searchItemImage);
            textName = itemView.findViewById(R.id.searchItemName);
            ratingBar = itemView.findViewById(R.id.searchItemRating);
            textPrice = itemView.findViewById(R.id.searchItemPrice);
            textReviews = itemView.findViewById(R.id.searchItemReviews);

        }
    }
}
