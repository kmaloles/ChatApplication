package com.kmaloles.mymessagingapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.model.Message;
import com.kmaloles.mymessagingapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PublicChatFragment extends Fragment {

    @BindView(R.id.edittext_chatbox)
    EditText mChatBox;
    Unbinder mUnbinder;

    DatabaseReference mDBReference;
    private final String TAG = "PublicChatFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBReference = FirebaseDatabase.getInstance().getReference(getContext().getString(R.string.public_chat_root_node));
        ChildEventListener childEventListener  = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //TODO: Add to displayed list
                Message message = dataSnapshot.getValue(Message.class);
                Log.e(TAG,message.toString());
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
        View v = inflater.inflate(R.layout.fragment_group_chat, container, false);
        mUnbinder = ButterKnife.bind(this,v);
        return v;
    }

    @OnClick(R.id.button_chatbox_send)
    public void onSendMessageTapped(){
        String message = mChatBox.getText().toString();
        if (!TextUtils.isEmpty(message)){
            String id = mDBReference.push().getKey();
            //TODO: Dates in UTC
            Message m = new Message(id,message,"k.a.maloles", Util.Dates.getCurrentTime());
            mDBReference.child(id).setValue(m);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
