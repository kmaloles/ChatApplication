package com.kmaloles.mymessagingapp.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.auth.SignInActivity;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSettingsActivity extends AppCompatActivity {


    @BindView(R.id.textEmail)
    TextView mEmail;

    DefaultDataManager mLocalDB;

    public static void start(Context context){
        Intent i = new Intent(context, AccountSettingsActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ButterKnife.bind(this);
        mLocalDB = new DefaultDataManager(this);
        initViews();
    }

    @OnClick(R.id.btnSignOut)
    public void onSignOutClicked(){
        FirebaseAuth.getInstance().signOut();
        SignInActivity.start(this);
    }

    @OnClick(R.id.btnBack)
    public void onBackPressed(){
        super.onBackPressed();
    }

    private void initViews(){
        mEmail.setText(mLocalDB.getUserLogin());
    }


}
