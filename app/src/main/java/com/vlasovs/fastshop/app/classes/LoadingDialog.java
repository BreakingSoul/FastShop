package com.vlasovs.fastshop.app.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.vlasovs.fastshop.R;

public class LoadingDialog extends AppCompatDialogFragment {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myAct){
        activity = myAct;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissLoadingDialog(){
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
