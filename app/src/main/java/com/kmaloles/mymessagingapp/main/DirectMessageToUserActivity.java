package com.kmaloles.mymessagingapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kmaloles.mymessagingapp.BaseActivity;
import com.kmaloles.mymessagingapp.Constants;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.adapter.MessengerAdapter;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;
import com.kmaloles.mymessagingapp.model.Message;
import com.kmaloles.mymessagingapp.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DirectMessageToUserActivity extends BaseActivity {

    @BindView(R.id.textView_username_recipient)
    TextView mUserNameLabel;
    @BindView(R.id.recycler_view_user_dm)
    RecyclerView mRecyclerView;
    @BindView(R.id.edittext_chatbox)
    EditText mChatBox;

    //the recipient
    String mUserName;
    //current user log in, in this case, Admin
    String mLoggedInUserName;


    MessengerAdapter mAdapter;
    Unbinder mUnbinder;

    DatabaseReference mDBReference;
    DefaultDataManager mLocalDB;
    List<Message> mMessageList;

    private final String TAG = "DirectMessageToUserActivity";

    private static final String RECIPIENT_USERNAME = "recipient_username";

    public static void start(Context context, String username){
        Intent i = new Intent(context, DirectMessageToUserActivity.class);
        i.putExtra(RECIPIENT_USERNAME, username);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message_to_user);

        mUserName = getIntent().getStringExtra(RECIPIENT_USERNAME);

        mUnbinder = ButterKnife.bind(this);

        mUserNameLabel.setText(mUserName);

        mMessageList = new ArrayList<>();

        //initializes local and firebase database
        initDB();

        mLoggedInUserName = mLocalDB.getUserLogin();

        //initialize the chat view
        initRecyclerView();

        ChildEventListener childEventListener  = new ChildEventListener() {
            //called when a message is sent or received
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //TODO: Correct date formatting on adapters
                Message message = dataSnapshot.getValue(Message.class);

                /**
                 * show messages from a particular user
                 */
                if ( message.getSender().equals(mUserName) || message.getRecipient().equals(mUserName)) {

                    message.setBody(getApplicationContext().getString(R.string.message_contains_banned_words));

                    mMessageList.add(message);
                    //refresh the table
                    mAdapter.notifyItemChanged(mMessageList.size() - 1);

                    mRecyclerView.scrollToPosition(mMessageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //use dataSnapshot.getkey() to update list
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //use dataSnapshot.getkey() to update list

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //use dataSnapshot.getkey() to update list

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf(TAG, "SendMessage:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();

            }

        };

        mDBReference.addChildEventListener(childEventListener);
    }

    @OnClick(R.id.button_chatbox_send)
    public void onSendMessageTapped(){
        //TODO: scroll to the bottom after sending
        String message = mChatBox.getText().toString();
        if (!TextUtils.isEmpty(message)){
            String id = mDBReference.push().getKey();
            //TODO: Dates in UTC
            Message m = new Message(id,message, Constants.MESSAGE_RECIPIENT_ADMIN, Util.Dates.getCurrentTime(), mUserName);
            mDBReference.child(id).setValue(m);

            mChatBox.setText("");
            mChatBox.clearFocus();

        }
    }

    @OnClick(R.id.btnBackToAdminMessages)
    public void onBackPressed(){
        finish();
    }

    private void initDB(){
        //initialize firebase database
        mDBReference = FirebaseDatabase.getInstance().getReference(this.getString(R.string.admin_chat_root_node));
        mLocalDB = new DefaultDataManager(this);
    }

    private void initRecyclerView(){
        LinearLayoutManager m = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(m);

        mAdapter = new MessengerAdapter(mMessageList, mLoggedInUserName);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
