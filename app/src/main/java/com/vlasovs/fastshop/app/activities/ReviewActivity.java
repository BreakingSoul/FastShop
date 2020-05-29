package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.classes.Item;

public class ReviewActivity extends AppCompatActivity {

    private Item item;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");
        userId = intent.getIntExtra("userid", -1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
