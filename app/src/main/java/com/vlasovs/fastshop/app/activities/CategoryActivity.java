package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vlasovs.fastshop.R;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView laptop, monitor, phone, tablet, watch, headphone, keyboard, speaker, micro, camera, tv, hardDrive, mouse, pc, gaming;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userid", -1);

        initializeViews();

    }

    private void initializeViews() {

        laptop = findViewById(R.id.catLaptop);
        monitor = findViewById(R.id.catMonitor);
        phone = findViewById(R.id.catPhone);
        tablet = findViewById(R.id.catTablet);
        watch = findViewById(R.id.catWatch);
        headphone = findViewById(R.id.catHeadphone);
        keyboard = findViewById(R.id.catKeyboard);
        speaker = findViewById(R.id.catSpeaker);
        micro = findViewById(R.id.catMicrophone);
        camera = findViewById(R.id.catCamera);
        tv = findViewById(R.id.catTV);
        hardDrive = findViewById(R.id.catHardDrive);
        mouse = findViewById(R.id.catMouse);
        pc = findViewById(R.id.catPC);
        gaming = findViewById(R.id.catGaming);

        laptop.setOnClickListener(this);
        monitor.setOnClickListener(this);
        phone.setOnClickListener(this);
        tablet.setOnClickListener(this);
        watch.setOnClickListener(this);
        headphone.setOnClickListener(this);
        keyboard.setOnClickListener(this);
        speaker.setOnClickListener(this);
        micro.setOnClickListener(this);
        camera.setOnClickListener(this);
        tv.setOnClickListener(this);
        hardDrive.setOnClickListener(this);
        mouse.setOnClickListener(this);
        pc.setOnClickListener(this);
        gaming.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        intent.putExtra("infoType", "category");
        intent.putExtra("accountid", userID);

        String category = null;

        switch (view.getId()){
            case R.id.catLaptop:
                category = "Laptop";
                break;
            case R.id.catMonitor:
                category = "Monitor";
                break;
            case R.id.catPhone:
                category = "Phone";
                break;
            case R.id.catTablet:
                category = "Tablet";
                break;
            case R.id.catWatch:
                category = "Watch";
                break;
            case R.id.catHeadphone:
                category = "Headphone";
                break;
            case R.id.catKeyboard:
                category = "Keyboard";
                break;
            case R.id.catSpeaker:
                category = "Speaker";
                break;
            case R.id.catMicrophone:
                category = "Microphone";
                break;
            case R.id.catCamera:
                category = "Camera";
                break;
            case R.id.catTV:
                category = "TV";
                break;
            case R.id.catHardDrive:
                category = "Hard Drive";
                break;
            case R.id.catMouse:
                category = "Mouse";
                break;
            case R.id.catPC:
                category = "PC";
                break;
            case R.id.catGaming:
                category = "Gaming";
                break;
        }
        intent.putExtra("infoName", "" + category);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
