package com.kmaloles.mymessagingapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.kmaloles.mymessagingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GroupChatFragment extends Fragment {

    @BindView(R.id.edittext_chatbox)
    EditText mChatBox;
    Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
