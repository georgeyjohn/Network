package com.ip.barcodescanner.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ip.barcodescanner.R;

;

public class CustomMessageDialog {

    private AlertDialog alertDialog;

    public CustomMessageDialog(final Context context, String title, String msg,
                               final boolean closeParent) {
        create(context, title, msg, closeParent);
    }

    public CustomMessageDialog(final Context context, String title, String msg) {
        create(context, title, msg, false);
    }

    @SuppressWarnings("deprecation")
    private void create(final Context context, String title, String msg,
                        final boolean closeParent) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (closeParent) {
                    ((Activity) context).finish();
                }

                dialog.dismiss();
            }
        });
        // Set the Icon for the Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);
    }

    public void show() {
        alertDialog.show();
    }
}
