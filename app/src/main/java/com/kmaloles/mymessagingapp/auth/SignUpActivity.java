package com.kmaloles.mymessagingapp.auth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.kmaloles.mymessagingapp.BaseActivity;
import com.kmaloles.mymessagingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.editText_signUp_fName)
    EditText mFName;
    @BindView(R.id.editText_signUp_lName)
    EditText mLName;
    @BindView(R.id.editText_signUp_email)
    EditText mEmail;
    @BindView(R.id.editText_signUp_password)
    EditText mPassword;

    private final String TAG = "SignUpActivity";
    Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUnbinder = ButterKnife.bind(this);
    }

    public static void start(Context context){
        Intent i = new Intent(context, SignUpActivity.class);
        context.startActivity(i);
    }

    @OnClick(R.id.textView_sign_in_link)
    public void onBackToSignInClicked(){
        onBackPressed();
    }

    @OnClick(R.id.button_sign_up)
    public void onSignUpClicked(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            showLoading();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        hideLoading();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
