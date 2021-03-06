package com.kmaloles.mymessagingapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.model.Message;

import java.util.List;


/**
 * Created by kevinmaloles on 12/12/17.
 */

public class MessengerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessages;
    private String mLoggedInUsername;
    private final int FIRST = 1;
    private final int SECOND = 2;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public MessengerAdapter(List<Message> mMessages, String user) {
        this.mMessages = mMessages;
        this.mLoggedInUsername = user;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //selects whether the cell is a received or a sent message
        View v;
        if (viewType == FIRST){
            v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sent_message, parent, false);
            return new MessengerAdapter.SentViewHolder(v);
        }else{
            v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_received_message, parent, false);
            return new MessengerAdapter.ReceivedViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message m = mMessages.get(position);

        //bind the views inside the cell to the data
        //checks the instance of the cell first before binding
        if (holder instanceof MessengerAdapter.SentViewHolder){
            //view bindings for sent messages
            MessengerAdapter.SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.message.setText(m.getBody());
            //if the message is filtered
            if (m.getBody().equals(viewHolder.message.getContext().getString(R.string.message_contains_banned_words))){
                viewHolder.message.setTextSize(10);
                viewHolder.message.setTypeface(null, Typeface.ITALIC);
            }
            viewHolder.date.setText(m.getCreated());
        }else{
            //view bindings for received messages
            MessengerAdapter.ReceivedViewHolder viewHolder = (ReceivedViewHolder) holder;

            viewHolder.message.setText(m.getBody());
            if (m.getBody().equals(viewHolder.message.getContext().getString(R.string.message_contains_banned_words))){
                viewHolder.message.setTextSize(10);
                viewHolder.message.setTypeface(null, Typeface.ITALIC);
            }
            viewHolder.date.setText(m.getCreated());
            viewHolder.name.setText(m.getSender());
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getSender().equalsIgnoreCase(mLoggedInUsername ) ? FIRST:SECOND;

    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView date;

        public ReceivedViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.received_message_name);
            message = itemView.findViewById(R.id.received_message_body);
            date = itemView.findViewById(R.id.received_message_time);
        }
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView date;

        public SentViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sent_message_body);
            date = itemView.findViewById(R.id.sent_message_time);
        }
    }
}
