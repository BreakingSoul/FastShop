package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.adapters.WishItemAdapter;
import com.vlasovs.fastshop.app.background.GetWishItemsTask;
import com.vlasovs.fastshop.app.background.ItemResponse;
import com.vlasovs.fastshop.app.classes.Item;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

import java.util.ArrayList;

public class WishActivity extends AppCompatActivity implements ItemResponse {

    private RecyclerView wishRecyclerView;
    private WishItemAdapter wishAdapter;
    private ArrayList<Item> wishItems;

    private int userID;
    private LoadingDialog lD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userid", -1);

        wishRecyclerView = findViewById(R.id.wish_recycler);
        wishItems = new ArrayList<>();
        wishAdapter = new WishItemAdapter(this, wishItems, userID);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        wishRecyclerView.setLayoutManager(manager);
        wishRecyclerView.setAdapter(wishAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(wishRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        wishRecyclerView.addItemDecoration(dividerItemDecoration);

        getWishItems();
    }

    private void getWishItems() {
        GetWishItemsTask gWIT = new GetWishItemsTask();
        gWIT.delegate = this;
        lD = new LoadingDialog(this);
        lD.startLoadingDialog();
        gWIT.execute(userID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void processFinish(ArrayList<Item> itemList) {
        wishItems.addAll(itemList);
        wishAdapter.notifyDataSetChanged();
        lD.dismissLoadingDialog();
    }
}
