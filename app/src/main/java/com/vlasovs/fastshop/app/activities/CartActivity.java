package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.adapters.CartItemAdapter;
import com.vlasovs.fastshop.app.background.GetCartItemsTask;
import com.vlasovs.fastshop.app.background.GetCartResponse;
import com.vlasovs.fastshop.app.classes.CartItem;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements GetCartResponse {

    private RecyclerView cartRecyclerView;
    private ArrayList<CartItem> cartItems;
    private CartItemAdapter cartAdapter;
    private Button purchaseButton;
    private TextView totalPriceTextView;
    LoadingDialog lD;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userid", -1);

        initializeViews();
        setupAdapter();
        getCart();
    }

    private void initializeViews() {
        cartRecyclerView = findViewById(R.id.cart_recycler);
        purchaseButton = findViewById(R.id.buttonPurchase);
        totalPriceTextView = findViewById(R.id.textViewTotalPrice);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(cartRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        cartRecyclerView.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this,
                LinearLayoutManager.VERTICAL,false);

        cartRecyclerView.setLayoutManager(linearLayoutManager);
        cartRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupAdapter() {
        cartItems = new ArrayList<>();
        cartAdapter = new CartItemAdapter(this, cartItems);

        cartAdapter.adapterhandler = new CartItemAdapter.AdapterHandler() {
            @Override
            public void updatePrice(String newPrice) {
                totalPriceTextView.setText("Total: " + newPrice + "€");
            }
        };

        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void getCart() {
        GetCartItemsTask gCIT = new GetCartItemsTask();
        gCIT.delegate = this;
        lD = new LoadingDialog(this);
        lD.startLoadingDialog();
        gCIT.execute(userId);
    }

    @Override
    public void processFinish(ArrayList<CartItem> itemList) {
        cartItems.addAll(itemList);
        cartAdapter.notifyDataSetChanged();

        float totalPrice = 0;
        for (CartItem c : cartItems){
            totalPrice += c.getPrice();
        }
        totalPriceTextView.setText("Total: " + String.format(Locale.getDefault(), "%.2f", totalPrice) + "€");
        lD.dismissLoadingDialog();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
