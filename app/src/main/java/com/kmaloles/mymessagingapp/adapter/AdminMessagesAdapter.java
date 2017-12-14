package com.kmaloles.mymessagingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.model.Message;

import java.util.List;

/**
 * Created by kevinmaloles on 12/14/17.
 */

public class AdminMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessageList;

    public AdminMessagesAdapter(List<Message> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_admin_messages_list, parent, false);
        return new AdminMessagesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message m = mMessageList.get(position);
        AdminMessagesAdapter.ViewHolder viewHolder = (AdminMessagesAdapter.ViewHolder) holder;

        viewHolder.message.setText(m.getBody());
        viewHolder.date.setText(m.getCreated());
        viewHolder.name.setText(m.getSender());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textview_sender);
            message = itemView.findViewById(R.id.textview_message_preview);
            date = itemView.findViewById(R.id.textview_create_time);
        }
    }


}