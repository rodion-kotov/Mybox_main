package com.example.mybox;

import android.app.Activity;
import android.app.AlertDialog;

import android.view.LayoutInflater;


public class Loading   {
    Activity activity;
    AlertDialog alertDialog;

    public Loading(Activity activity) {
        this.activity = activity;
    }
    void start(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.YDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading,null));
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();

    }
    void stop(){
        alertDialog.dismiss();
    }
}
