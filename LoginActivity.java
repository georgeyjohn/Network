package com.ip.barcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ip.barcodescanner.entity.PostForgotPassword;
import com.ip.barcodescanner.entity.PostLogin;
import com.ip.barcodescanner.entity.ResponseBarCodeLayout;
import com.ip.barcodescanner.entity.ResponseForgotPassword;
import com.ip.barcodescanner.entity.ResponseLogin;
import com.ip.barcodescanner.utils.CustomMessageDialog;
import com.ip.barcodescanner.utils.CustomeAsyncTask;
import com.ip.barcodescanner.utils.JSONHelper;
import com.ip.barcodescanner.utils.MyLocation;
import com.ip.barcodescanner.utils.http.CheckConnectivity;
import com.ip.barcodescanner.utils.http.HttpManager;
import com.ip.barcodescanner.utils.persistance.Keys;
import com.ip.barcodescanner.utils.persistance.PersistanceManager;

/*
this activity handles the user login
 */
public class LoginActivity extends AppCompatActivity {

    // create a UI object
    private UI ui;
    // persistance manager object
    private PersistanceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = new Explode();
            getWindow().setSharedElementEnterTransition(transition);
            getWindow().setSharedElementExitTransition(transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pm = new PersistanceManager(LoginActivity.this);
        ui = new UI();
        initViews();
        // get the current geo location
        //  getGeoLocation();
        if (CheckConnectivity.check(LoginActivity.this)) {
            if (filedValidation()) {
                // go for login
                loginService();
            }
        }


    }


    private void initViews() {
        ui.button_login = (Button) findViewById(R.id.button_login);
        ui.button_cancel = (Button) findViewById(R.id.button_cancel);
        ui.password = (EditText) findViewById(R.id.password);
        ui.username = (EditText) findViewById(R.id.username);
        ui.tvRegisterUser = (TextView) findViewById(R.id.tv_register);
        ui.tvForgotPassword = (TextView) findViewById(R.id.tv_forgot);

        // if there is already a user name and password from previous entry ... load them
//        if (pm.getValue(Keys._username) != null) {
//            ui.username.setText(pm.getValue(Keys._username));
//        }
//        if (pm.getValue(Keys._password) != null) {
//            ui.password.setText(pm.getValue(Keys._password));
//        }
        if ((pm.getValue(Keys._username) != null) && (pm.getValue(Keys._password) != null)) {
            if ((pm.getValue(Keys._username).length() != 0) && (pm.getValue(Keys._password).length() != 0)) {
                ui.username.setText(pm.getValue(Keys._username));
                ui.password.setText(pm.getValue(Keys._password));
//                if (CheckConnectivity.check(LoginActivity.this)) {
//                    if (filedValidation()) {
//                        // go for login
//                        loginService();
//                    }
//                }
            }
        }

        ui.button_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // if all the fileds are filled then
                if (CheckConnectivity.check(LoginActivity.this)) {
                    if (filedValidation()) {
                        // go for login
                        loginService();
                    }
                }
            }
        });

        ui.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                ui.username.setText("");
                ui.password.setText("");
            }
        });
        ui.tvRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentRegisterUser = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegisterUser);

            }
        });
        ui.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ui.username.getText().toString().trim().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please Enter E-mail ID", Toast.LENGTH_LONG).show();
                } else {
                    ForgotPassword();
                }
            }
        });
        setActionbar();
    }


    private void ForgotPassword() {
        new CustomeAsyncTask(this) {

            String response;
            PostForgotPassword postForgotPassword;
            ResponseForgotPassword[] responseForgotPassword;

            @Override
            public void preExecute() {
                postForgotPassword = new PostForgotPassword();
                postForgotPassword.setUsername(ui.username.getText().toString());
            }

            @Override
            public void executeInBackground() {

                try {
                    // create a post login object and set values
                    // make the post request with the post data with login url
                    response = HttpManager.post(LoginActivity.this, "ForgotPassword", JSONHelper.Serialize(postForgotPassword));
                    System.err.println(response);

                    // parse the response
                    try {
                        responseForgotPassword = (ResponseForgotPassword[]) JSONHelper.Deserialize(response, ResponseForgotPassword[].class);
                    } catch (Exception ex) {
                        responseForgotPassword = null;
                    }
                } catch (Exception ex) {
                    responseForgotPassword = null;
                }
            }

            @Override
            public void postExecute() {
                if (responseForgotPassword == null || responseForgotPassword.length == 0) {
                    Toast.makeText(LoginActivity.this, "Please try after some time", Toast.LENGTH_SHORT).show();
                } else {
                    if (responseForgotPassword[0].getKey().equals("Error")) {
                        new CustomMessageDialog(LoginActivity.this, "Error", responseForgotPassword[0].getValue()).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "\n" +
                                "Password Reset: Check your email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }.execute();
    }


    private void loginService() {
        new CustomeAsyncTask(this) {

            String usernameString, passwordString, response;
            ResponseLogin responseLogin;
            ResponseBarCodeLayout responseBarCodeLayout;
            PostLogin postLogin;

            @Override
            public void preExecute() {
                // get the user inputted username and password
                usernameString = ui.username.getText().toString();
                passwordString = ui.password.getText().toString();
            }

            @Override
            public void executeInBackground() {

                try {
                    // create a post login object and set values
                    postLogin = new PostLogin();
                    postLogin.setUsername(usernameString);
                    postLogin.setPassword(passwordString);
                    // make the post request with the post data with login url
                    response = HttpManager.post(LoginActivity.this, "login", JSONHelper.Serialize(postLogin));
                    System.err.println(response);

                    // parse the response
                    responseLogin = (ResponseLogin) JSONHelper.Deserialize(response, ResponseLogin.class);

                    // if no response then end
                    if (responseLogin == null || !responseLogin.getResult().contentEquals("200")) {
                        return;
                    }

                    // get the layout from the session id
                    response = HttpManager.get(LoginActivity.this, "getbarcodelayout", "Bearer  " + responseLogin.getSessionId());
                    System.err.println(response);

                    // parse the layout
                    responseBarCodeLayout = (ResponseBarCodeLayout) JSONHelper.Deserialize(response, ResponseBarCodeLayout.class);

                } catch (Exception ex) {
                    responseLogin = null;
                }
            }

            @Override
            public void postExecute() {

                // if there is login response and layout response and login is success then continue
                if (responseLogin != null && responseLogin.getResult().contentEquals("200")
                        && responseBarCodeLayout != null && responseBarCodeLayout.getResult().contentEquals("200")) {

                    // store the values
                    pm.setValue(Keys._response_login, JSONHelper.Serialize(responseLogin));
                    pm.setValue(Keys._response_bar_code_layout, JSONHelper.Serialize(responseBarCodeLayout));
                    pm.setValue(Keys._post_login, JSONHelper.Serialize(postLogin));
                    pm.setValue(Keys._username, usernameString);
                    pm.setValue(Keys._password, passwordString);

                    // start the scanner activity
                    Intent i = new Intent(LoginActivity.this, ScannerActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Please check your E-mail or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    /*
    method to check whether all the fields are filled
     */
    private boolean filedValidation() {
        if (ui.username.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter E-mail ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.password.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    protected void onDestroy() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // set an exit transition
            getWindow().setExitTransition(new Explode());
        }

        super.onDestroy();
    }


    private void setActionbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(null);
        ((TextView) mToolbar.findViewById(R.id.toolbar_title_textview)).setText(getString(R.string.user_login));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationIcon(null);
    }

    class UI {
        EditText username, password;
        Button button_login, button_cancel;
        TextView tvRegisterUser, tvForgotPassword;
    }

    /*
    method to get the current location
     */
    private void getGeoLocation() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Fetching Location...");
        pd.show();

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                // store the location
                pm.setValue(Keys._geolocation, JSONHelper.Serialize(location));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (CheckConnectivity.check(LoginActivity.this)) {
                    if (filedValidation()) {
                        // go for login
                        loginService();
                    }
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }
}
