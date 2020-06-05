package com.vlasovs.fastshop.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.adapters.SupportMessageAdapter;
import com.vlasovs.fastshop.app.background.GetMessagesResponse;
import com.vlasovs.fastshop.app.background.LoadMessagesTask;
import com.vlasovs.fastshop.app.background.SendDataResponse;
import com.vlasovs.fastshop.app.background.SendMessageTask;
import com.vlasovs.fastshop.app.classes.LoadingDialog;
import com.vlasovs.fastshop.app.classes.Message;

import java.util.ArrayList;

public class SupportActivity extends AppCompatActivity implements GetMessagesResponse, SendDataResponse {

    private RecyclerView messageRecycler;
    private RecyclerView.LayoutManager manager;
    private SupportMessageAdapter messageAdapter;
    private ArrayList<Message> messages;
    private TextInputLayout messageInput;
    private Button sendMessageBut;

    private int userID;
    private LoadingDialog lD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userid", -1);

        initializeViews();

        loadMessages();

        sendMessageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidMessage()){
                    hideKeyboard(SupportActivity.this);
                    sendMessage();
                }
            }

        });
    }

    private void initializeViews() {
        messageRecycler = findViewById(R.id.recyclerMessages);
        messageInput = findViewById(R.id.messageInput);
        sendMessageBut = findViewById(R.id.buttonSendMessage);

        messages = new ArrayList<>();
        messageAdapter = new SupportMessageAdapter(this, messages);

        manager = new LinearLayoutManager(this);
        messageRecycler.setLayoutManager(manager);
        messageRecycler.setAdapter(messageAdapter);
    }

    private boolean isValidMessage(){
        String messageText = messageInput.getEditText().getText().toString().trim();
        if (messageText.isEmpty()){
            Toast.makeText(this, "Message can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (messageText.length() > 700) {
            Toast.makeText(this, "Message is too long", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loadMessages() {
        LoadMessagesTask lMT = new LoadMessagesTask();
        lMT.delegate = this;
        lD = new LoadingDialog(this);
        lD.startLoadingDialog();
        lMT.execute(userID);
    }

    @Override
    public void messageDownloadFinish(ArrayList<Message> receivedMessages) {
        messages.addAll(receivedMessages);
        messageAdapter.notifyDataSetChanged();
        lD.dismissLoadingDialog();
    }

    private void sendMessage(){
        SendMessageTask sMT = new SendMessageTask();
        sMT.delegate = this;
        Message newMessage = new Message(userID, (short) 1, messageInput.getEditText().getText().toString().trim());
        messages.add(newMessage);
        lD = new LoadingDialog(this);
        lD.startLoadingDialog();
        sMT.execute(newMessage);
    }

    @Override
    public void operationStatus(boolean status) {
        lD.dismissLoadingDialog();
        if (status){
            messageInput.getEditText().setText("");
            messageAdapter.notifyDataSetChanged();
            showSuccessDialog();
        } else {
            messages.remove(messages.size() - 1);
            messageAdapter.notifyDataSetChanged();
            showFailedDialog();
        }
    }

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Message sent successfully");
        builder.setMessage("You will receive the answer from our support in 24 hours.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFailedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Message wasn't sent successfully");
        builder.setMessage("Something went wrong. Please try again later.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
