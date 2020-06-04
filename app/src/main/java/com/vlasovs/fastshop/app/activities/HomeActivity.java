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
import com.vlasovs.fastshop.app.background.ItemResponse;
import com.vlasovs.fastshop.app.background.MiniItemsTask;
import com.vlasovs.fastshop.app.classes.Item;
import com.vlasovs.fastshop.app.classes.User;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ItemResponse,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button butReg, butLog, headerButton;
    private DrawerLayout drawer;
    private Menu drawerMenu;
    private NavigationView navigationView;
    private View headerView;
    private TextView headerTitle;
    private LinearLayout regLogButtonLayout;
    private RecyclerView recyclerView, recyclerViewBottom;
    private ArrayList<Item> miniItems, miniItemsBottom;
    private MiniItemAdapter miniItemAdapter, miniItemAdapterBottom;
    private CardView laptopCat, monitorCat, phoneCat, tabletCat, watchCat, accessoryCat;
    private int refreshCounter = 0;
    private int receiveCounter = 0;
 //   private boolean isClickedBottomRecycler;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SAVED_USER_ID = "id";

    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        initializeViews();

        butReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistration();
            }
        });

        headerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                openLogIn();
            }
        });

        butLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogIn();
            }
        });

        loadUser();

    }

    private void initializeViews(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        butReg = findViewById(R.id.butReg);
        butLog = findViewById(R.id.butLog);
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
        drawerMenu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(this);
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
                    user.setID(-1);
                    saveUser();
                    showNotLoggedUserViews();
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
            showLoggedUserViews();
        } else {
            headerButton.setVisibility(View.VISIBLE);
            drawerMenu.findItem(R.id.nav_log_out).setVisible(false);
        }

    }

    private void showLoggedUserViews(){
        drawerMenu.findItem(R.id.nav_log_out).setVisible(true);
        regLogButtonLayout.setVisibility(View.GONE);
        headerButton.setVisibility(View.GONE);
    }

    private void showNotLoggedUserViews(){
        headerButton.setVisibility(View.VISIBLE);
        drawerMenu.findItem(R.id.nav_log_out).setVisible(false);
        regLogButtonLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null){
            if (resultCode == RESULT_OK){
                int userID = data.getIntExtra("fetchedID", 0);
                user = new User(userID);
                showLoggedUserViews();
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
        switch (item.getItemId()){

            case R.id.nav_categories:
                openCategories();
                break;
            case R.id.nav_shop_cart:
                if (user.getID() != -1) {
                    openCartActivity();
                } else {
                    openLogIn();
                }
                break;

            case R.id.nav_wish_list:
                if (user.getID() != -1) {
                    openWishActivity();
                } else {
                    openLogIn();
                }
                break;

            case R.id.nav_support:

                if (user.getID() != -1) {
                    openSupportActivity();
                } else {
                    openSupportDialog();
                }
                break;

            case R.id.nav_log_out:
                openConfirmDialog();
                break;
        }
        return true;
    }

    private void openSupportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Customer Service Information");
        builder.setMessage("You can contact us by calling on number +37122822869 or writing on email support@fastshop.com. If you log in the system we will be able to help you via private chat!");

        builder.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openLogIn();
            }
        });

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openSupportActivity() {
        Intent intent = new Intent(HomeActivity.this, SupportActivity.class);
        intent.putExtra("userid", user.getID());
        startActivity(intent);
    }

    private void openCategories() {
        Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
        intent.putExtra("userid", user.getID());
        startActivity(intent);
    }

    private void openWishActivity() {
        Intent intent = new Intent(HomeActivity.this, WishActivity.class);
        intent.putExtra("userid", user.getID());
        startActivity(intent);
    }

    private void openCartActivity() {
        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
        intent.putExtra("userid", user.getID());
        startActivity(intent);
    }

    @Override
    public void processFinish(ArrayList<Item> itemList) {

        if (receiveCounter == 0) {
            miniItems.addAll(itemList);
            miniItemAdapter.notifyDataSetChanged();
            receiveCounter++;
        } else {
            miniItemsBottom.addAll(itemList);
            miniItemAdapterBottom.notifyDataSetChanged();
            receiveCounter = 0;
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
 /*           case R.id.recycler_view_mini_items:
                isClickedBottomRecycler = false;
                break;
            case R.id.recycler_view_mini_items_bottom:
                isClickedBottomRecycler = true;
                break;*/
        }
        intent.putExtra("infoName", "" + category);
        startActivity(intent);
    }
}


