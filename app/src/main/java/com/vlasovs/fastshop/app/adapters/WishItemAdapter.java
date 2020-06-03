package com.vlasovs.fastshop.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.activities.ItemActivity;
import com.vlasovs.fastshop.app.activities.SearchActivity;
import com.vlasovs.fastshop.app.activities.WishActivity;
import com.vlasovs.fastshop.app.background.OnListChangeResponse;
import com.vlasovs.fastshop.app.background.RemoveWishItemTask;
import com.vlasovs.fastshop.app.classes.Item;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

import java.util.ArrayList;
import java.util.Locale;

public class WishItemAdapter extends RecyclerView.Adapter<WishItemAdapter.ViewHolder> implements OnListChangeResponse {

    private ArrayList<Item> wishItems;
    private Context context;
    private int userID;
    private LoadingDialog lD;

    public WishItemAdapter(Context context, ArrayList<Item> wishItems, int userID) {
        this.wishItems = wishItems;
        this.context = context;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishItemAdapter.ViewHolder holder, final int position) {

        holder.textReviews.setText("Reviews: " + String.format(Locale.getDefault(), "%d", wishItems.get(position).getReviews()));
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f", wishItems.get(position).getPrice())+"€");
        holder.ratingBar.setRating(wishItems.get(position).getRating());
        holder.textName.setText(wishItems.get(position).getName());
        Glide.with(context).load(wishItems.get(position).getPictureURL()).into(holder.imageView);

        holder.wishItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("item", wishItems.get(position));
                intent.putExtra("userid", userID);
                context.startActivity(intent);
            }
        });

        holder.removeItemBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveWishItemTask rWIT = new RemoveWishItemTask();
                rWIT.delegate = WishItemAdapter.this;
                lD = new LoadingDialog((Activity)context);
                lD.startLoadingDialog();
                rWIT.execute(wishItems.get(position).getId(), userID);

                wishItems.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishItems.size();
    }

    @Override
    public void listRefresh() {
        lD.dismissLoadingDialog();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView wishItemCard;
        ImageView imageView;
        TextView textName, textPrice, textReviews;
        RatingBar ratingBar;
        Button removeItemBut;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            wishItemCard = itemView.findViewById(R.id.wishItemCard);
            imageView = itemView.findViewById(R.id.wishItemImage);
            textName = itemView.findViewById(R.id.wishItemName);
            ratingBar = itemView.findViewById(R.id.wishItemRating);
            textPrice = itemView.findViewById(R.id.wishItemPrice);
            textReviews = itemView.findViewById(R.id.wishItemReviews);
            removeItemBut = itemView.findViewById(R.id.wishItemRemove);

        }
    }
}
