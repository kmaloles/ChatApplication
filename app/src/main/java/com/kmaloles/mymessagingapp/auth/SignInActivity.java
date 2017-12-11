package com.kmaloles.mymessagingapp.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kmaloles.mymessagingapp.BaseActivity;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.main.MainActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity {


    @BindView(R.id.editText_signIn_email)
    EditText mEditTextEmail;
    @BindView(R.id.editText_signIn_password)
    EditText mEditTextPassword;

    private static final int RC_SIGN_IN = 123;

    private final String TAG = "SignInActivity";

    public static void start(Context context){
        Intent i = new Intent(context, SignInActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //TODO: check if currently LoggedIn
        ButterKnife.bind(this);
    }

    private void createSignInView(){

// Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @OnClick(R.id.button_sign_in)
    public void onSignInTapped(){
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            showLoading();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        hideLoading();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();

                            MainActivity.start(this);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @OnClick(R.id.textView_sign_up_link)
    public void onSignUpClicked(){
        SignUpActivity.start(this);
    }

}
