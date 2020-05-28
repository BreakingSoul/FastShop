package com.vlasovs.fastshop.app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.adapters.MiniItemAdapter;
import com.vlasovs.fastshop.app.background.MiniItemResponse;
import com.vlasovs.fastshop.app.background.MiniItemsTask;
import com.vlasovs.fastshop.app.classes.MiniItem;
import com.vlasovs.fastshop.app.classes.User;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements MiniItemResponse, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView loggedUserTW;
    private Button butReg, butLog, butSignOut;
    private DrawerLayout drawer;
    private User user;
    private RecyclerView recyclerView, recyclerViewBottom;
    private ArrayList<MiniItem> miniItems, miniItemsBottom;
    private MiniItemAdapter miniItemAdapter, miniItemAdapterBottom;
    private CardView laptopCat, monitorCat, phoneCat, tabletCat, watchCat, accessoryCat;
    private int refreshCounter = 0;
    private int recieveCounter = 0;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SAVED_USER_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        butReg = findViewById(R.id.butReg);
        butLog = findViewById(R.id.butLog);
        butSignOut = findViewById(R.id.butSignOut);
        loggedUserTW = findViewById(R.id.textUser);
        drawer = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recycler_view_mini_items);
        recyclerViewBottom = findViewById(R.id.recycler_view_mini_items_bottom);
        laptopCat = findViewById(R.id.laptopCategory);
        monitorCat = findViewById(R.id.monitorCategory);
        phoneCat = findViewById(R.id.phoneCategory);
        tabletCat = findViewById(R.id.tabletCategory);
        watchCat = findViewById(R.id.watchCategory);
        accessoryCat = findViewById(R.id.accessoryCategory);

        laptopCat.setOnClickListener(this);
        monitorCat.setOnClickListener(this);
        phoneCat.setOnClickListener(this);
        tabletCat.setOnClickListener(this);
        watchCat.setOnClickListener(this);
        accessoryCat.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManagerBottom = new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.HORIZONTAL,false);

        miniItems = new ArrayList<>();
        miniItemsBottom = new ArrayList<>();
        miniItemAdapter = new MiniItemAdapter(HomeActivity.this, miniItems);
        miniItemAdapterBottom = new MiniItemAdapter(HomeActivity.this, miniItemsBottom);

        addMiniItems();

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBottom.setLayoutManager(linearLayoutManagerBottom);
        recyclerViewBottom.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(miniItemAdapter);
        recyclerViewBottom.setAdapter(miniItemAdapterBottom);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        butReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistration();
            }
        });

        butLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogIn();
            }
        });

        butSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });

        loadUser();

    }

    private void addMiniItems(){

        MiniItemsTask mIT = new MiniItemsTask();
        mIT.delegate = this;
        mIT.execute(1);

        MiniItemsTask mITBottom = new MiniItemsTask();
        mITBottom.delegate = this;
        mITBottom.execute(2);

    }

    private void openConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Are you really want to log out?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    loggedUserTW.setText("Sign in please :D");
                    user.setID(-1);
                    saveUser();
                    butSignOut.setVisibility(View.GONE);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SAVED_USER_ID, user.getID());

        editor.apply();

    }

    private void loadUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        user = new User(sharedPreferences.getInt(SAVED_USER_ID, -1));

        if (user.getID() != -1) {
            butSignOut.setVisibility(View.VISIBLE);
            loggedUserTW.setText("Logged in user: " + user.getID());
        } else {
            butSignOut.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            if (resultCode == RESULT_OK){
                int userID = data.getIntExtra("fetchedID", 0);
                user = new User(userID);
                loggedUserTW.setText("Logged in user: " + user.getID());
                butSignOut.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCounter++;
        if (refreshCounter == 3) {
            miniItems.clear();
            miniItemsBottom.clear();
            addMiniItems();
            refreshCounter = 0;
        }
    }

    private void openLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    private void openRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(ArrayList<MiniItem> itemList) {

        if (recieveCounter == 0) {
            miniItems.addAll(itemList);
            miniItemAdapter.notifyDataSetChanged();
            recieveCounter++;
        } else {
            miniItemsBottom.addAll(itemList);
            miniItemAdapterBottom.notifyDataSetChanged();
            recieveCounter = 0;
        }
    }

    @Override
    public void onClick(View view) {
        //for buttons set separately sorry, here only categories
        switch (view.getId()){
            case R.id.laptopCategory:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.monitorCategory:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.phoneCategory:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tabletCategory:
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.watchCategory:
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accessoryCategory:
                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

    //    private void addItems() {
//        miniItems.add(new MiniItem(R.mipmap.ic_launcher, "iPhone 5", 3.5f, 100, 129));
//        miniItems.add(new MiniItem(R.mipmap.ic_launcher, "iPhone 6", 4.3f, 228.34f, 49));
//        miniItems.add(new MiniItem(R.mipmap.ic_launcher, "iPhone 7", 4.2f, 149.99f, 166));
//        miniItems.add(new MiniItem(R.mipmap.ic_launcher, "iPhone 8", 5.0f, 780, 12));
//        miniItems.add(new MiniItem(R.mipmap.ic_launcher, "iPhone X", 3.7f, 90, 16));
//    }


