package com.kmaloles.mymessagingapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kmaloles.mymessagingapp.Constants;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.adapter.AdminMessagesAdapter;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;
import com.kmaloles.mymessagingapp.model.Message;
import com.kmaloles.mymessagingapp.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdminFragment extends Fragment implements AdminMessagesAdapter.OnMessageClickedListener {
    // the fragment initialization parameters
    private static final String MODE = "mode";

    // TODO: Rename and change types of parameters
    private String mMode;
    private Unbinder mUnbinder;

    @BindView(R.id.recycler_view_admin_dm)
    RecyclerView mRecyclerView;

    AdminMessagesAdapter mAdapter;

    DatabaseReference mDBReference;
    DefaultDataManager mLocalDB;

    public AdminFragment() {
        // Required empty public constructor
    }

    List<Message> mMessagesList;
    //a list which contains messages to admin
    //contains only the most recent message
    //from a particular sender

    List<Message> mFilteredMessagesList;

    private final String TAG = "AdminFragment";

    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String mode) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMode = getArguments().getString(MODE);
        }
        initDB();
        //empty messages list
        mMessagesList = new ArrayList<>();
        mFilteredMessagesList = new ArrayList<>();

        ChildEventListener childEventListener  = new ChildEventListener() {
            //called when a message is sent or received
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //TODO: Add to displayed list
                //TODO: pagination
                Message message = dataSnapshot.getValue(Message.class);
                Log.e(TAG,message.toString());

                if (!message.getSender().equals(Constants.MESSAGE_RECIPIENT_ADMIN)){
                    //refresh the table
                    refreshAdminMessages(message);
                }

                mRecyclerView.scrollToPosition(mMessagesList.size() - 1);
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);
        mUnbinder = ButterKnife.bind(this,v);

        //initialize the chat view
        initRecyclerView();

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);


        mAdapter = new AdminMessagesAdapter(mFilteredMessagesList);
        mAdapter.setMessagesListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void initDB(){
        //initialize firebase database
        mDBReference = FirebaseDatabase.getInstance().getReference(getContext().getString(R.string.admin_chat_root_node));
        //TODO: dependency injection
        mLocalDB = new DefaultDataManager(getContext());
    }

    /**
     * ensures the list only contains the most recent
     * message for a particular sender
     * , newest at the top
     */
    private void refreshAdminMessages(Message pMessage){
        mMessagesList.add(pMessage);
        if (mFilteredMessagesList.size() == 0){
            mFilteredMessagesList.add(pMessage);
        }else{
            int indexToReplace = -1;
            for(int i = 0; i < mFilteredMessagesList.size(); i++){
                if(mFilteredMessagesList.get(i).getSender().equals(pMessage.getSender())){
                    indexToReplace = i;
                }
            }
            //if match is found
            if (indexToReplace >= 0){
                mFilteredMessagesList.set(indexToReplace, pMessage);
            }else{
                //just add it
                mFilteredMessagesList.add(pMessage);
            }
        }
        //TODO: reverse
        Collections.sort(mFilteredMessagesList,
                (message, t1) -> {
                Date d1 = Util.Dates.getDateFromString(message.getCreated());
                Date d2 = Util.Dates.getDateFromString(t1.getCreated());
                return d1.compareTo(d2);
        });

    }

    @Override
    public void onItemClicked(int adapterPos) {
        DirectMessageToUserActivity.start(getActivity(),mFilteredMessagesList.get(adapterPos).getSender());
    }
}
