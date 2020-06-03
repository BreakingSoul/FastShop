package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.background.AddWishItemTask;
import com.vlasovs.fastshop.app.background.ItemResponse;
import com.vlasovs.fastshop.app.background.OnListChangeResponse;
import com.vlasovs.fastshop.app.background.AddCartItemTask;
import com.vlasovs.fastshop.app.background.ItemDescriptionResponse;
import com.vlasovs.fastshop.app.background.ItemDescriptionTask;
import com.vlasovs.fastshop.app.background.OnWishListAddedResponse;
import com.vlasovs.fastshop.app.classes.CartItem;
import com.vlasovs.fastshop.app.classes.Item;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

import java.util.ArrayList;
import java.util.Locale;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener, ItemDescriptionResponse,
                                                                OnListChangeResponse, OnWishListAddedResponse {

    private Item item;
    private ImageView image;
    private Button addFav, addCart, incBut, decBut, showReviews;
    private TextView itemName, itemDesc, itemCost;
    private EditText editCount;
    private RatingBar rating;

  //  private PurchaseDialog pD;
    private LoadingDialog lD;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");
        userId = intent.getIntExtra("userid", -1);

        initializeViews();

        editCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int amount;
                if (editable.length() == 1 && editable.toString().equals("0")) {
                    editCount.setText("1");
                }
                if (!editable.toString().equals("")) {
                    amount = Integer.parseInt(editCount.getText().toString());
                    if (amount <= 0) {
                        editCount.setText(String.valueOf(1));
                        amount = 1;
                    }
                    float total = amount * item.getPrice();
                    itemCost.setText(String.format(Locale.getDefault(), "%.2f", total) + "€");
                }
            }
        });

        setInfoInViews();

    }

    private void initializeViews(){
        image = findViewById(R.id.itemActImage);
        addFav = findViewById(R.id.itemActFavBut);
        addCart = findViewById(R.id.itemActCartBut);
        incBut = findViewById(R.id.buttonCounterInc);
        decBut = findViewById(R.id.buttonCounterDec);
        showReviews = findViewById(R.id.itemActReviewBut);
        itemName = findViewById(R.id.itemActName);
        itemDesc= findViewById(R.id.itemActDescription);
        itemCost = findViewById(R.id.itemActPrice);
        editCount = findViewById(R.id.editCounter);
        rating = findViewById(R.id.itemActRating);

        addFav.setOnClickListener(this);
        addCart.setOnClickListener(this);
        incBut.setOnClickListener(this);
        decBut.setOnClickListener(this);
        showReviews.setOnClickListener(this);
    }

    private void setInfoInViews() {

        Glide.with(getApplicationContext()).load(item.getPictureURL()).into(image);
        itemName.setText(item.getName());
        itemCost.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice())+"€");
        showReviews.setText(showReviews.getText() + " (" + String.format(Locale.getDefault(), "%d", item.getReviews()) + ")");
        rating.setRating(item.getRating());

        ItemDescriptionTask iDT = new ItemDescriptionTask();
        iDT.delegate = this;
        iDT.execute(item.getId());

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        int currentAmount;
        Intent intent;
        switch (view.getId()){

            case R.id.itemActFavBut:
                if(userId != -1) {
                    AddWishItemTask aWIT = new AddWishItemTask();
                    aWIT.delegate = this;
                    lD = new LoadingDialog(this);
                    lD.startLoadingDialog();
                    aWIT.execute(userId, item.getId());
                } else {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.itemActCartBut:
                if(userId != -1) {
                    CartItem cartItem = new CartItem(item.getId(), userId, item.getPictureURL(), item.getName(),
                            Float.parseFloat(editCount.getText().toString()) * item.getPrice(),
                            Integer.parseInt(editCount.getText().toString()));

                    AddCartItemTask aCIT = new AddCartItemTask();
                    aCIT.delegate = this;
                    lD = new LoadingDialog(this);
                    lD.startLoadingDialog();
                    aCIT.execute(cartItem);
                } else {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.buttonCounterInc:
                if (editCount.getText().toString().equals("")){
                editCount.setText("0");
            } else {
                    currentAmount = Integer.parseInt(editCount.getText().toString()) + 1;
                    editCount.setText(String.valueOf(currentAmount));
                }
                break;

            case R.id.buttonCounterDec:
                if (editCount.getText().toString().equals("")){
                    editCount.setText("0");
                }
                currentAmount = Integer.parseInt(editCount.getText().toString());
                if (!(currentAmount <= 1)) {
                    editCount.setText(String.valueOf(currentAmount - 1));
                }
                break;

            case R.id.itemActReviewBut:
                intent = new Intent(ItemActivity.this, ReviewActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("userid", userId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void processDescriptionFinish(String description) {
        itemDesc.setText(description);
    }

    @Override
    public void listRefresh() {
        lD.dismissLoadingDialog();
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("userid", userId);
        startActivity(intent);
    }

    @Override
    public void wishItemAdded() {
        lD.dismissLoadingDialog();
        Toast.makeText(this, "Item added to wish list", Toast.LENGTH_LONG).show();
    }
}
