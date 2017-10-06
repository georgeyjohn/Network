package com.ip.barcodescanner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ip.barcodescanner.R;
import com.ip.barcodescanner.entity.ResponseBarCodeLayout;
import com.ip.barcodescanner.utils.http.CheckConnectivity;
import com.ip.barcodescanner.utils.persistance.Keys;
import com.ip.barcodescanner.utils.persistance.PersistanceManager;

/*
this is the barcode info display dialog
 */
public class BarCodeInfoDialog {

    private Dialog dialog;
    private Context context;
    private String serialNo = null, additionalItemId = null, gTin = null;
    private UI ui;
    private ResponseBarCodeLayout responseBarCodeLayout;
    private String barcodeData;

    public BarCodeInfoDialog(Context cxt, String barcodeData) {

        this.context = cxt;
        this.barcodeData = barcodeData;

        dialog = new Dialog(cxt);

        // get the barcode layout
        responseBarCodeLayout = (ResponseBarCodeLayout) new PersistanceManager(cxt).getValue(Keys._response_bar_code_layout, ResponseBarCodeLayout.class);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_barcode_info);

        ui = new UI();

        initElements();

        // --------------------------------
        // code to set the dialog to the bottom and set width
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // --------------------------------

        dialog.setCancelable(true);

        dialog.show();

        // start the bar code processing
        processBarcodeInfo();

    }

    private void initElements() {
        ui.text_view = (TextView) dialog.findViewById(R.id.text_view);
        ui.button_validate = (Button) dialog.findViewById(R.id.button_validate);

        ui.button_validate.setVisibility(View.GONE);
    }

    private class UI {
        TextView text_view;
        Button button_validate;
    }

    /*
    method to process the barcode data
     */
    private void processBarcodeInfo() {

        // get the barcode data
        try {
            barcodeData = barcodeData.substring(1);
            System.err.println("barcode data :" + barcodeData);
        } catch (Exception ex) {
            // if there is any error then show error msg
            System.err.println(ex.getLocalizedMessage());
            ui.text_view.setText(Html.fromHtml("<font color=\"red\">Invalid Data Matrix !</font>"));
            return;
        }

        StringBuilder sb = new StringBuilder();

        int startIndex = 0, endIndex = 0;

        try {
            // for each layout definition
            for (ResponseBarCodeLayout.Details d : responseBarCodeLayout.getDetails()) {

                // get start index
                startIndex = barcodeData.indexOf(d.getGS1Code(), startIndex) + d.getGS1Code().length();
                endIndex = 0;

                // if the delimiter conatins the term variable
                if (d.getDelimiter().contentEquals("Variable")) {
                    // find the index of the special character from the starting index
                    endIndex = barcodeData.indexOf("\u001D", startIndex);
                    if (startIndex > endIndex) {
                        endIndex = barcodeData.length() - 1;
                    }
                } else {
                    // calculate the end index
                    endIndex = startIndex + Integer.parseInt(d.getDataMaxLen());
                }

                // checking for miss calculation
                if (startIndex > endIndex) {
                    System.err.println("startIndex > endIndex....");
                    // continue the search
                    continue;
                }

                sb.append(d.getGS1Description()).append(" : ");

                System.err.println("start : " + startIndex);
                System.err.println("End : " + endIndex);
                System.err.println("len : " + barcodeData.length());

                sb.append("<b>").append(barcodeData.substring(startIndex, endIndex)).append("</b><br><br>");

                // filter the codes based on the GS1 codes
                if (d.getGS1Code().contentEquals("21")) {
                    serialNo = barcodeData.substring(startIndex, endIndex);
                }
                if (d.getGS1Code().contentEquals("240")) {
                    additionalItemId = barcodeData.substring(startIndex);
                    System.err.println("Additional item id length : " + additionalItemId.length());
                }
                if (d.getGS1Code().contentEquals("01")) {
                    gTin = barcodeData.substring(startIndex, endIndex);
                }
            }

            String s = sb.toString();

            try {
                s = s.replace(additionalItemId.substring(0, additionalItemId.length() - 1), additionalItemId);
            } catch (Exception ex) {
                System.err.println(ex.getLocalizedMessage());
            }

            // display the result
            ui.text_view.setText(Html.fromHtml(s));

            // enable the validate button
            ui.button_validate.setVisibility(View.VISIBLE);
            ui.button_validate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go for validation dialog
                    if (CheckConnectivity.check(context)) {
                        new ValidityDialog(context, serialNo, additionalItemId, gTin);
                    }
                }
            });


        } catch (Exception ex) {
            // on error show error msg
            System.err.println(ex.getLocalizedMessage());
            ui.text_view.setText(Html.fromHtml("<font color=\"red\">Invalid Data Matrix !</font>"));
            ui.button_validate.setVisibility(View.GONE);
        }
    }

}
