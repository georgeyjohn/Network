package com.ip.barcodescanner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ip.barcodescanner.R;
import com.ip.barcodescanner.entity.PostLogin;
import com.ip.barcodescanner.entity.PostProductInformation;
import com.ip.barcodescanner.entity.ResponseLogin;
import com.ip.barcodescanner.entity.ResponseProductInformation;
import com.ip.barcodescanner.utils.CustomeAsyncTask;
import com.ip.barcodescanner.utils.JSONHelper;
import com.ip.barcodescanner.utils.http.CheckConnectivity;
import com.ip.barcodescanner.utils.http.HttpManager;
import com.ip.barcodescanner.utils.persistance.Keys;
import com.ip.barcodescanner.utils.persistance.PersistanceManager;

import java.util.ArrayList;

public class ValidityDialog {

    private Dialog dialog;
    private Context context;
    private UI ui;

    private String serialNo, additionalItemId, gTin;

    public ValidityDialog(Context cxt, String serialNo, String additionalItemId, String gTin) {

        // set values
        this.context = cxt;
        this.serialNo = serialNo;
        this.additionalItemId = additionalItemId;
        this.gTin = gTin;

        dialog = new Dialog(cxt);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_validity);

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

        processProductInfo();

    }

    private void initElements() {
        ui.imageView = (ImageView) dialog.findViewById(R.id.imageView);
        ui.button_more_info = (Button) dialog.findViewById(R.id.button_more_info);
    }

    private class UI {
        ImageView imageView;
        Button button_more_info;
    }


    private void processProductInfo() {

        try {

            if (serialNo != null && additionalItemId != null) {
                new CustomeAsyncTask(context) {

                    PersistanceManager pm;
                    String response;

                    @Override
                    public void executeInBackground() {

                        try {
                            pm = new PersistanceManager(context);
                            ResponseLogin responseLogin = (ResponseLogin) pm.getValue(Keys._response_login, ResponseLogin.class);
                            PostLogin postLogin = (PostLogin) pm.getValue(Keys._post_login, PostLogin.class);

                            // declare a product info object
                            PostProductInformation postProductInformation = new PostProductInformation();

                            Location location = (Location) pm.getValue(Keys._geolocation, Location.class);

                            postProductInformation.setAdditionalIdentificationNumber(additionalItemId);
                            postProductInformation.setGTIN(gTin);
                            postProductInformation.setLatitude(location == null ? 0 : location.getLatitude());
                            postProductInformation.setLongitude(location == null ? 0 : location.getLongitude());
                            postProductInformation.setSerialNumber(serialNo);
                            postProductInformation.setUserEmailAddress(postLogin.getUsername());

                            // make the http post call with the object info
                            if (CheckConnectivity.check(context)) {
                                response = HttpManager.post(context, "getproductinformation", JSONHelper.Serialize(postProductInformation), "Bearer  " + responseLogin.getSessionId());
                            }
                            System.err.println(response);
                        } catch (Exception ex) {
                            System.err.println(ex.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void postExecute() {

                        ui.imageView.setVisibility(View.VISIBLE);

                        try {
                            // parse the result
                            final ResponseProductInformation[] pa = (ResponseProductInformation[]) JSONHelper.Deserialize(response, ResponseProductInformation[].class);

                            ui.button_more_info.setVisibility(View.GONE);

                            if (pa != null) {
                                // from all the results
                                for (ResponseProductInformation x : pa) {
                                    // if there is "Genuine" key
                                    if (x.getKey().contentEquals("Genuine") && x.getValue().contentEquals("Y")) {
                                        pm.setValue(Keys._product_info, response);
                                        // display the genuine logo
                                        ui.imageView.setImageResource(R.drawable.genuine);
                                        // enable more info button
                                        ui.button_more_info.setVisibility(View.VISIBLE);
                                        ui.button_more_info.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                ArrayList<ResponseProductInformation> al = new ArrayList<ResponseProductInformation>();
                                                for (ResponseProductInformation x : pa) {
                                                    if (!x.getKey().contentEquals("Genuine")) {
                                                        al.add(x);
                                                    }
                                                }
                                                ResponseProductInformation[] z = al.toArray(new ResponseProductInformation[al.size()]);

                                                new ProductInfoDialog(context, JSONHelper.Serialize(z));
                                            }
                                        });

                                        if (pa.length < 2) {
                                            ui.button_more_info.setVisibility(View.GONE);
                                        }

                                        return;
                                    }
                                }

                            }

                        } catch (Exception ex) {
                            System.err.println(ex.getLocalizedMessage());
                            // by default the info is error info so no need to do any thing in case of exception
                            Toast.makeText(context, "Please try after some time", Toast.LENGTH_SHORT).show();
                            ui.button_more_info.setVisibility(View.GONE);
                        }

                    }
                }.execute();
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            Toast.makeText(context, "Please try after some time", Toast.LENGTH_SHORT).show();
            // by default the info is error info so no need to do any thing in case of exception
        }
    }
}
