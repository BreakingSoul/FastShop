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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.adapters.MiniItemAdapter;
import com.vlasovs.fastshop.app.adapters.OnItemCardClickListener;
import com.vlasovs.fastshop.app.background.ItemResponse;
import com.vlasovs.fastshop.app.background.MiniItemsTask;
import com.vlasovs.fastshop.app.classes.Item;
import com.vlasovs.fastshop.app.classes.User;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ItemResponse,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnItemCardClickListener {

    private TextView loggedUserTW;
    private Button butReg, butLog, butSignOut;
    private DrawerLayout drawer;
    private Menu drawerMenu;
    private NavigationView navigationView;
    private View headerView;
    private TextView headerTitle;
    private Button headerButton;
    private LinearLayout regLogButtonLayout;
    private RecyclerView recyclerView, recyclerViewBottom;
    private ArrayList<Item> miniItems, miniItemsBottom;
    private MiniItemAdapter miniItemAdapter, miniItemAdapterBottom;
    private CardView laptopCat, monitorCat, phoneCat, tabletCat, watchCat, accessoryCat;
    private int refreshCounter = 0;
    private int recieveCounter = 0;
    private boolean isClickedBottomRecycler;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SAVED_USER_ID = "id";

    public static User user;

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
        regLogButtonLayout = findViewById(R.id.regButtonsLayout);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer);
        headerView = navigationView.getHeaderView(0);
        headerTitle = headerView.findViewById(R.id.user_name_drawer);
        headerButton = headerView.findViewById(R.id.log_in_drawer);

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
        miniItemAdapter = new MiniItemAdapter(HomeActivity.this, miniItems, this);
        miniItemAdapterBottom = new MiniItemAdapter(HomeActivity.this, miniItemsBottom, this);

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
        drawerMenu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(this);

        butReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistration();
            }
        });

        headerButton.setOnClickListener(new View.OnClickListener() {
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
       //             butSignOut.setVisibility(View.GONE);
                    headerButton.setVisibility(View.VISIBLE);
                    drawerMenu.findItem(R.id.nav_log_out).setVisible(false);
                    regLogButtonLayout.setVisibility(View.VISIBLE);
                    butReg.setVisibility(View.VISIBLE);
                    butLog.setVisibility(View.VISIBLE);
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
   //     String title;

        if (user.getID() != -1) {
     //       butSignOut.setVisibility(View.VISIBLE);
            drawerMenu.findItem(R.id.nav_log_out).setVisible(true);
     //       loggedUserTW.setText("Logged in user: " + user.getID());
            headerButton.setVisibility(View.GONE);
            regLogButtonLayout.setVisibility(View.GONE);
            butReg.setVisibility(View.GONE);
            butLog.setVisibility(View.GONE);
        } else {
   //         butSignOut.setVisibility(View.GONE);
            headerButton.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            if (resultCode == RESULT_OK){
                int userID = data.getIntExtra("fetchedID", 0);
                user = new User(userID);
    //            loggedUserTW.setText("Logged in user: " + user.getID());
    //            butSignOut.setVisibility(View.VISIBLE);
                drawerMenu.findItem(R.id.nav_log_out).setVisible(true);
                butReg.setVisibility(View.GONE);
                butLog.setVisibility(View.GONE);
                regLogButtonLayout.setVisibility(View.GONE);
                headerButton.setVisibility(View.GONE);
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
        switch (item.getItemId()){
            case R.id.nav_log_out:
                openConfirmDialog();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(ArrayList<Item> itemList) {

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
        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        intent.putExtra("infoType", "category");
        intent.putExtra("accountid", user.getID());

        String category = null;

        switch (view.getId()){
            case R.id.laptopCategory:
                category = "Laptop";
                break;
            case R.id.monitorCategory:
                category = "Monitor";
                break;
            case R.id.phoneCategory:
                category = "Phone";
                break;
            case R.id.tabletCategory:
                category = "Tablet";
                break;
            case R.id.watchCategory:
                category = "Watch";
                break;
            case R.id.accessoryCategory:
                category = "Accessory";
                break;
            case R.id.recycler_view_mini_items:
                isClickedBottomRecycler = false;
                break;
            case R.id.recycler_view_mini_items_bottom:
                isClickedBottomRecycler = true;
                break;
        }
        intent.putExtra("infoName", "" + category);
        startActivity(intent);
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(HomeActivity.this, ItemActivity.class);
        if (isClickedBottomRecycler) {
            intent.putExtra("item", miniItemsBottom.get(position));
        } else {
            intent.putExtra("item", miniItems.get(position));
        }
        startActivity(intent);
    }
}


