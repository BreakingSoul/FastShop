package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.adapters.SearchItemAdapter;
import com.vlasovs.fastshop.app.background.ItemResponse;
import com.vlasovs.fastshop.app.background.SearchItemsTask;
import com.vlasovs.fastshop.app.classes.Item;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ItemResponse {

    private RecyclerView searchRecyclerView;
    private ArrayList<Item> foundItems;
    private SearchItemAdapter searchAdapter;
    private RecyclerView.LayoutManager manager;
    private TextView twFoundAmount;
    private String searchType, searchName;
    public static int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        searchType = intent.getStringExtra("infoType");
        searchName = intent.getStringExtra("infoName");
        userID = intent.getIntExtra("accountid", -1);

        foundItems = new ArrayList<>();

        searchRecyclerView = findViewById(R.id.search_recycler);
        twFoundAmount = findViewById(R.id.textViewFoundAmount);

        searchAdapter = new SearchItemAdapter(SearchActivity.this, foundItems);

        manager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(manager);
        searchRecyclerView.setAdapter(searchAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(searchRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        searchRecyclerView.addItemDecoration(dividerItemDecoration);

        addFoundItems();

    }

    private void addFoundItems() {
        SearchItemsTask sIT = new SearchItemsTask();
        sIT.delegate = this;
        sIT.execute(searchType, searchName);
    }

    @Override
    public void processFinish(ArrayList<Item> itemList) {
        foundItems.addAll(itemList);
        searchAdapter.notifyDataSetChanged();
        twFoundAmount.setText("Items found: " + foundItems.size());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
