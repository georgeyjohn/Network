package com.ip.barcodescanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ip.barcodescanner.entity.ErrorRegister;
import com.ip.barcodescanner.entity.PostRegister;
import com.ip.barcodescanner.entity.ResponseRegister;
import com.ip.barcodescanner.utils.CustomMessageDialog;
import com.ip.barcodescanner.utils.CustomeAsyncTask;
import com.ip.barcodescanner.utils.JSONHelper;
import com.ip.barcodescanner.utils.http.HttpManager;
import com.ip.barcodescanner.utils.persistance.Keys;
import com.ip.barcodescanner.utils.persistance.PersistanceManager;

public class RegisterActivity extends AppCompatActivity {

    private UI ui;
    private PersistanceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ui = new UI();
        intiViews();
        pm = new PersistanceManager(RegisterActivity.this);
        ui.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldValidation()) {
                    RegisterService();
                }
            }
        });
        setActionbar();
    }

    private void RegisterService() {
        new CustomeAsyncTask(this) {

            String response;
            ResponseRegister responseRegister;
            PostRegister postRegister = new PostRegister();
            ErrorRegister[] errorRegister;

            @Override
            public void preExecute() {
                postRegister.setUsername(ui.etUserName.getText().toString());
                postRegister.setPassword(ui.etPassword.getText().toString());
                postRegister.setPassword(ui.etConfirmPassword.getText().toString());
                postRegister.setFirstname(ui.etFirstName.getText().toString());
                postRegister.setLastname(ui.etLastName.getText().toString());
                postRegister.setAddress1(ui.etAddress1.getText().toString());
                postRegister.setAddress2(ui.etAddress2.getText().toString());
                postRegister.setCity(ui.etCity.getText().toString());
                postRegister.setState(ui.etState.getText().toString());
                postRegister.setCountry(ui.etCountry.getText().toString());
                postRegister.setZip(ui.etZip.getText().toString());
                postRegister.setPhone(ui.etPhone.getText().toString());
                postRegister.setFax(ui.etFax.getText().toString());
            }

            @Override
            public void executeInBackground() {

                try {
                    // make the post request with the post data with login url
                    response = HttpManager.post(RegisterActivity.this, "Register", JSONHelper.Serialize(postRegister));
                    System.err.println(response);

                    // parse the response
                    try {
                        responseRegister = (ResponseRegister) JSONHelper.Deserialize(response, ResponseRegister.class);
                        System.err.println(responseRegister);
                    } catch (Exception ex) {
                        responseRegister = null;
                    }

                    if (responseRegister == null) {
                        try {
                            errorRegister = (ErrorRegister[]) JSONHelper.Deserialize(response, ErrorRegister[].class);
                            System.err.println(errorRegister);
                        } catch (Exception ex) {
                            errorRegister = null;
                        }
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void postExecute() {

                if (responseRegister == null) {
                    if (errorRegister == null || errorRegister.length == 0) {
                        Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    } else {
                        new CustomMessageDialog(RegisterActivity.this, "Error", errorRegister[0].getValue()).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_LONG).show();
                    // store the values
                    pm.setValue(Keys._response_login, "");
                    pm.setValue(Keys._response_bar_code_layout, "");
                    pm.setValue(Keys._post_login, "");
                    pm.setValue(Keys._username, "");
                    pm.setValue(Keys._password, "");
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }.execute();
    }

    private void setActionbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setTitle(null);
        ((TextView) mToolbar.findViewById(R.id.toolbar_title_textview)).setText(getString(R.string.register_user_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // Handle Back Navigation
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void intiViews() {
        ui.etUserName = (EditText) findViewById(R.id.et_username);
        ui.etPassword = (EditText) findViewById(R.id.et_password);
        ui.etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        ui.etFirstName = (EditText) findViewById(R.id.et_first_name);
        ui.etLastName = (EditText) findViewById(R.id.et_last_name);
        ui.etAddress1 = (EditText) findViewById(R.id.et_address1);
        ui.etAddress2 = (EditText) findViewById(R.id.et_address2);
        ui.etCity = (EditText) findViewById(R.id.et_city);
        ui.etState = (EditText) findViewById(R.id.et_state);
        ui.etCountry = (EditText) findViewById(R.id.et_country);
        ui.etZip = (EditText) findViewById(R.id.et_zip);
        ui.etPhone = (EditText) findViewById(R.id.et_phone);
        ui.etFax = (EditText) findViewById(R.id.et_fax);
        ui.btnRegister = (Button) findViewById(R.id.btn_register);
        setEditText();
    }

    private void setEditText() {
        ui.etPassword.setTypeface(Typeface.DEFAULT);
        ui.etConfirmPassword.setTypeface(Typeface.DEFAULT);
        ui.etUserName.setHint(setMandatoryField(getResources().getString(R.string.username_email)));
        ui.etPassword.setHint(setMandatoryField(getResources().getString(R.string.password)));
        ui.etConfirmPassword.setHint(setMandatoryField(getResources().getString(R.string.confirm_password)));
        ui.etFirstName.setHint(setMandatoryField(getResources().getString(R.string.first_name)));
        ui.etLastName.setHint(setMandatoryField(getResources().getString(R.string.last_name)));
        ui.etAddress1.setHint(setMandatoryField(getResources().getString(R.string.address1)));
        ui.etCity.setHint(setMandatoryField(getResources().getString(R.string.city)));
        ui.etState.setHint(setMandatoryField(getResources().getString(R.string.state)));
        ui.etCountry.setHint(setMandatoryField(getResources().getString(R.string.country)));
        ui.etZip.setHint(setMandatoryField(getResources().getString(R.string.zip)));
        ui.etPhone.setHint(setMandatoryField(getResources().getString(R.string.ph_no)));
    }

    private boolean fieldValidation() {

        if (!(ui.etPassword.getText().toString().trim().contentEquals(ui.etConfirmPassword.getText().toString().trim()))) {
            Toast.makeText(RegisterActivity.this, "Password miss match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ui.etUserName.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter E-mail ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etConfirmPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etFirstName.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etLastName.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etAddress1.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter Address 1", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etCity.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etState.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter State", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etCountry.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter Country", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etZip.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter Zip", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etPhone.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ui.etPassword.getText().toString().trim().length() != 5) {
            Toast.makeText(RegisterActivity.this, "Password should be 5 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private SpannableStringBuilder setMandatoryField(String text) {
        String hintText = text;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(hintText);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    class UI {
        EditText etUserName, etPassword, etConfirmPassword, etFirstName, etLastName, etAddress1, etAddress2, etCity, etState, etCountry, etZip, etPhone, etFax;
        Button btnRegister;
    }
}
