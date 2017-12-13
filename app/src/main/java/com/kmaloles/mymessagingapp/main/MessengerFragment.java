package com.kmaloles.mymessagingapp.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kmaloles.mymessagingapp.Constants;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.adapter.PublicChatAdapter;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;
import com.kmaloles.mymessagingapp.model.Message;
import com.kmaloles.mymessagingapp.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MessengerFragment extends Fragment {
    // the fragment initialization parameters
    private static final String MODE = "mode";

    // TODO: Rename and change types of parameters
    private String mMode;

    @BindView(R.id.edittext_chatbox)
    EditText mChatBox;

    Unbinder mUnbinder;
    String mLoggedInUsername;

    @BindView(R.id.recycler_view_public_chat)
    RecyclerView mRecyclerView;

    PublicChatAdapter mAdapter;

    DatabaseReference mDBReference;
    DefaultDataManager mLocalDB;
    List<Message> mMessageList;
    private final String TAG = "MessengerFragment";

    public MessengerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode Determines whether the view is used as either:
     *               -public messaging
     *               -direct messaging to admin
     * @return A new instance of fragment MessengerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessengerFragment newInstance(String mode) {
        MessengerFragment fragment = new MessengerFragment();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMode= getArguments().getString(MODE);
        }

        //initializes local and firebase database
        initDB();

        //get the current username logged in
        mLoggedInUsername = mLocalDB.getUserLogin();

        mMessageList = new ArrayList<>();

        ChildEventListener childEventListener  = new ChildEventListener() {
            //called when a message is sent or received
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //TODO: Add to displayed list
                //TODO: pagination
                Message message = dataSnapshot.getValue(Message.class);
                Log.e(TAG,message.toString());

//                if we are on direct message to admin mode
//                and the message is NOT sent by the current user
//                i.e. the message was sent to the admin
//                by some other account, exit the method
                boolean senderIsNotUser = !message.getSender().equals(mLoggedInUsername);
                boolean isInDirectMessageToAdminMode = mMode.equals(Constants.FRAGMENT_MODE_ADMIN_DIRECT_MESSAGE);
                if ( isInDirectMessageToAdminMode && senderIsNotUser ){return;}

                mMessageList.add(message);
                //refresh the table
                mAdapter.notifyItemChanged(mMessageList.size() - 1);

                mRecyclerView.scrollToPosition(mMessageList.size() - 1);
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
                Toast.makeText(getContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();

            }

        };

        mDBReference.addChildEventListener(childEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messenger, container, false);
        mUnbinder = ButterKnife.bind(this,v);

        //initialize the chat view
        initRecyclerView();

        return v;
    }

    private void initDB(){

        //get the string resource depending on the what we're showing in this fragment
        int rootNode = this.mMode.equals(Constants.FRAGMENT_MODE_PUBLIC_MESSAGING) ? R.string.public_chat_root_node : R.string.admin_chat_root_node;

        //initialize firebase database
        mDBReference = FirebaseDatabase.getInstance().getReference(getContext().getString(rootNode));
        //TODO: dependency injection
        mLocalDB = new DefaultDataManager(getContext());

    }

    @OnClick(R.id.button_chatbox_send)
    public void onSendMessageTapped(){
        //TODO: scroll to the bottom after sending
        String message = mChatBox.getText().toString();
        if (!TextUtils.isEmpty(message)){
            String id = mDBReference.push().getKey();
            //TODO: Dates in UTC
            String recipient = mMode.equals(Constants.FRAGMENT_MODE_PUBLIC_MESSAGING) ? Constants.FRAGMENT_MODE_PUBLIC_MESSAGING : Constants.FRAGMENT_MODE_ADMIN_DIRECT_MESSAGE;
            Message m = new Message(id,message, mLoggedInUsername, Util.Dates.getCurrentTime(), recipient);
            mDBReference.child(id).setValue(m);

            mChatBox.setText("");
            mChatBox.clearFocus();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initRecyclerView(){
        LinearLayoutManager m = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(m);

        mAdapter = new PublicChatAdapter(mMessageList, mLoggedInUsername);
        mRecyclerView.setAdapter(mAdapter);

    }

}
