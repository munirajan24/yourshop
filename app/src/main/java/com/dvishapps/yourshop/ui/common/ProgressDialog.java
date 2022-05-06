package com.dvishapps.yourshop.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dvishapps.yourshop.R;

public class ProgressDialog {
    private Activity activity;
    private AlertDialog dialog;
    private String message="";

    public ProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public ProgressDialog(Activity activity, String message) {
        this.activity = activity;
        this.message = message;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        TextView txtMessage = view.findViewById(R.id.txt_message);
        if (message.length()>0){
            txtMessage.setText(message);
        }
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        if (dialog!=null) {
            dialog.dismiss();
        }
    }
}
