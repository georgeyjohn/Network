package com.ip.barcodescanner;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ip.barcodescanner.dialog.BarCodeInfoDialog;
import com.ip.barcodescanner.utils.CustomeAsyncTask;
import com.ip.barcodescanner.utils.http.CheckConnectivity;
import com.ip.barcodescanner.utils.persistance.Keys;
import com.ip.barcodescanner.utils.persistance.PersistanceManager;

/*
this activity is the base from which we will call the data matrix scanner and process the result
 */
public class ScannerActivity extends AppCompatActivity {

    private PersistanceManager pm;
    private int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = new Explode();
            getWindow().setSharedElementEnterTransition(transition);
            getWindow().setSharedElementExitTransition(transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        pm = new PersistanceManager(ScannerActivity.this);

        // initilize the scan button
        ((ImageView) findViewById(R.id.button_scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomeAsyncTask(ScannerActivity.this) {

                    @Override
                    public void executeInBackground() {
                        if (CheckConnectivity.check(ScannerActivity.this)) {
                            scan();
                        }
                    }
                }.execute();
            }
        });

        setActionbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final Drawable about = getResources().getDrawable(R.drawable.logout);
        about.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        menu.getItem(0).setIcon(about);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            pm.setValue(Keys._response_login, "");
            pm.setValue(Keys._response_bar_code_layout, "");
            pm.setValue(Keys._post_login, "");
            pm.setValue(Keys._username, "");
            pm.setValue(Keys._password, "");
            Intent i = new Intent(ScannerActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private void setActionbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(null);
        ((TextView) mToolbar.findViewById(R.id.toolbar_title_textview)).setText(getString(R.string.scanner));
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //upArrow.setColorFilter(getResources().getColor(R.color.dark_brown), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // Handle Back Navigation
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intLogin = new Intent(ScannerActivity.this, LoginActivity.class);
//                startActivity(intLogin);

        // finish();
        //onBackPressed();
        //         }
        //   });
    }

    @Override
    protected void onDestroy() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // set an exit transition
            getWindow().setExitTransition(new Explode());
        }

        super.onDestroy();
    }

    /*
    method to initiate the scan
     */
    private void scan() {
        try {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX_TYPES);
            // set the scan view info text
            integrator.setPrompt("Scan Data Matrix");
            integrator.setResultDisplayDuration(0);
            //integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.initiateScan();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                // on scan canceled
                Log.d("MainActivity", "Cancelled scan");
            } else {
                // on successfull scan
                Log.d("MainActivity", "Scanned");
                try {
                    // show the barcode info dialog, pass the result data
                    new BarCodeInfoDialog(ScannerActivity.this, result.getContents());
                } catch (Exception ex) {
                    System.err.println(ex.getLocalizedMessage());
                }
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
