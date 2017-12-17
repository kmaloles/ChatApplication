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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kmaloles.mymessagingapp.BaseActivity;
import com.kmaloles.mymessagingapp.Constants;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;
import com.kmaloles.mymessagingapp.main.MainActivity;
import com.kmaloles.mymessagingapp.model.BannedWord;
import com.kmaloles.mymessagingapp.model.DatasnapshotValueModel;
import com.kmaloles.mymessagingapp.model.User;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignInActivity extends BaseActivity {


    @BindView(R.id.editText_signIn_email)
    EditText mEditTextEmail;
    @BindView(R.id.editText_signIn_password)
    EditText mEditTextPassword;

    Unbinder mUnbinder;

    private static final int RC_SIGN_IN = 123;

    private final String TAG = "SignInActivity";

    DatabaseReference mDBReference;
    DefaultDataManager mLocalDB;
    String mUsersRootNode;

    public static void start(Context context){
        Intent i = new Intent(context, SignInActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isUserAuthenticated()){
            startMainActivity();
            return;
        }
        setContentView(R.layout.activity_sign_in);
        //TODO: check if currently LoggedIn
        mUnbinder = ButterKnife.bind(this);
        mLocalDB = new DefaultDataManager(this);
        mUsersRootNode = this.getString(R.string.users_root_node);
        mDBReference = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.button_sign_in)
    public void onSignInTapped(){
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            showLoading();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            setUserTypeAndContinue(email);
                        } else {
                            hideLoading();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        hideLoading();

    }

    @OnClick(R.id.textView_sign_up_link)
    public void onSignUpClicked(){
        SignUpActivity.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void setUserTypeAndContinue(String email){
        Query query = mDBReference.child(mUsersRootNode).orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideLoading();
                if (dataSnapshot.exists()) {
                    // if username found
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        User user = d.getValue(User.class);
                        mLocalDB.setUserType(user.getUserType());
                        mLocalDB.persistUserLogin(user.getUsername());
                    }
                    startMainActivity();
                }else{
                    showToast("Something went wrong, please try again", getBaseContext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideLoading();
                Log.wtf(TAG, databaseError.toString());
            }
        });
    }

    private void startMainActivity(){
        MainActivity.start(this);
    }

    private boolean isUserAuthenticated(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
