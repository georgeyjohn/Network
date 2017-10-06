package com.ip.barcodescanner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.ip.barcodescanner.R;
import com.ip.barcodescanner.adapter.ProductInfoAdapter;
import com.ip.barcodescanner.entity.ResponseProductInformation;
import com.ip.barcodescanner.utils.JSONHelper;

/*
this dialog is for the product more info
 */
public class ProductInfoDialog {

    private Dialog dialog;
    private Context context;
    private UI ui;

    public ProductInfoDialog(Context cxt, String data) {

        this.context = cxt;


        dialog = new Dialog(cxt);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_product_info);

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

        processProductInfo(data);

    }

    private void processProductInfo(String data) {
        try {
            // parse data
            ResponseProductInformation[] pa = (ResponseProductInformation[]) JSONHelper.Deserialize(data, ResponseProductInformation[].class);

            // load in list view adapter
            ProductInfoAdapter adapter = new ProductInfoAdapter(context, pa);
            ui.list.setAdapter(adapter);
            ui.error_text.setVisibility(View.GONE);

        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            // by default the info is error info so no need to do any thing in case of exception
            ui.error_text.setVisibility(View.VISIBLE);
        }
    }

    private void initElements() {
        ui.error_text = (TextView) dialog.findViewById(R.id.error_text);
        ui.list = (ListView) dialog.findViewById(R.id.list);
    }

    private class UI {
        ListView list;
        TextView error_text;
    }
}
