package com.kmaloles.mymessagingapp.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kmaloles.mymessagingapp.BaseActivity;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;
import com.kmaloles.mymessagingapp.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.editText_signUp_username)
    EditText mUsername;
    @BindView(R.id.editText_signUp_email)
    EditText mEmail;
    @BindView(R.id.editText_signUp_password)
    EditText mPassword;

    private final String TAG = "SignUpActivity";
    Unbinder mUnbinder;

    DatabaseReference mDBReference;
    String mUsersRootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUnbinder = ButterKnife.bind(this);
        //Firebase reference root node: USERS
        mUsersRootNode = this.getString(R.string.users_root_node);
        mDBReference = FirebaseDatabase.getInstance().getReference();
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
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString();
        String username = mUsername.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) ){
            showLoading();

            //check if user is existing
            Query query = mDBReference.child(mUsersRootNode).orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        hideLoading();
                        // if username found, show error
                        showToast("Username already exists. Please select a different one", getBaseContext());
                        return;
                    }else{
                        //create the user in firebase database
                        mDBReference.push();
                        User user = new User(username,email,DefaultDataManager.USER_TYPE_COMMON);
                        mDBReference.child(mUsersRootNode).child(username).setValue(user);

                        //create the user in firebase auth
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    hideLoading();
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(getBaseContext(), "Authentication Successful.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getBaseContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideLoading();
                    Log.wtf(TAG, databaseError.toString());

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
