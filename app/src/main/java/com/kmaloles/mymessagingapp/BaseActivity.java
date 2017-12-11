package com.kmaloles.mymessagingapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by kevinmaloles on 12/8/17.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog mDialog;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    public void showLoading() {
        if (mDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mDialog = progressDialog;
        }

    }

    public void hideLoading() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void showToast(String text, Context context) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (context != null) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
