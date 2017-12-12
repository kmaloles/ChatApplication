package com.kmaloles.mymessagingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.model.Message;

import java.util.List;

/**
 * Created by kevinmaloles on 12/12/17.
 */

public class PublicChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessages;

    public PublicChatAdapter(List<Message> mMessages) {
        this.mMessages = mMessages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private static class ReceivedViewholder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView date;

        public ReceivedViewholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.received_message_name);
            message = itemView.findViewById(R.id.received_message_body);
            date = itemView.findViewById(R.id.received_message_time);
        }
    }

    private static class SentViewholder extends RecyclerView.ViewHolder {
        TextView message;
        TextView date;

        public SentViewholder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sent_message_body);
            date = itemView.findViewById(R.id.sent_message_time);
        }
    }
}
