package com.vlasovs.fastshop.app.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.activities.HomeActivity;

public class PurchaseDialog {

    private Activity activity;
    private AlertDialog dialog;

    public PurchaseDialog(Activity myAct){
        activity = myAct;
    }

    public void startPurchaseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.purchase_dialog, null));
        builder.setCancelable(false);

        builder.setPositiveButton("Purchase item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, HomeActivity.class);
                activity.startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setTitle("Please provide following information: ");
        dialog = builder.create();
        dialog.show();
    }

    public void dismissPurchaseDialog(){
        dialog.dismiss();
    }

 /*   @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.purchase_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }*/

}
