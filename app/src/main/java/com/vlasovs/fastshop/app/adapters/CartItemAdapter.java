package com.vlasovs.fastshop.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.background.RemoveCartItemTask;
import com.vlasovs.fastshop.app.classes.CartItem;

import java.util.ArrayList;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private ArrayList<CartItem> cartItems;
    private Context context;
    public AdapterHandler adapterhandler;

    public CartItemAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, final int position) {

        holder.textAmount.setText("Amount: " + String.format(Locale.getDefault(), "%d", cartItems.get(position).getAmount()));
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f", cartItems.get(position).getPrice()) + "€");
        holder.textName.setText(cartItems.get(position).getName());
        Glide.with(context).load(cartItems.get(position).getPictureURL()).into(holder.imageView);

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RemoveCartItemTask rCIT = new RemoveCartItemTask();
                rCIT.execute(cartItems.get(position));

                cartItems.remove(position);
                notifyDataSetChanged();

                float newPrice = 0;
                for (CartItem c : cartItems){
                    newPrice += c.getPrice();
                }

                if (adapterhandler != null) {
                    adapterhandler.updatePrice(String.format(Locale.getDefault(), "%.2f", newPrice));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView searchItemCard;
        ImageView imageView;
        TextView textName, textPrice, textAmount;
        Button removeItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            searchItemCard = itemView.findViewById(R.id.cartItemCard);
            imageView = itemView.findViewById(R.id.cartItemImage);
            textName = itemView.findViewById(R.id.cartItemName);
            textPrice = itemView.findViewById(R.id.cartItemPrice);
            textAmount = itemView.findViewById(R.id.cartItemAmount);
            removeItem = itemView.findViewById(R.id.cartItemRemove);
        }
    }

    public abstract static class AdapterHandler{
        public void updatePrice(String newPrice) {}
    }
}
