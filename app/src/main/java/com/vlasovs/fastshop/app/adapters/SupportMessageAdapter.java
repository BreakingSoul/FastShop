package com.vlasovs.fastshop.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vlasovs.fastshop.R;
import com.vlasovs.fastshop.app.classes.Message;

import java.util.ArrayList;

public class SupportMessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 0;

    private Context context;
    private ArrayList<Message> messageList;

    public SupportMessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position){
        Message message = messageList.get(position);

        if (message.getToSupport() == 1){
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageList.get(position);

        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedViewHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView sender, message, time;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.twSender);
            message = itemView.findViewById(R.id.twMessage);
            time = itemView.findViewById(R.id.twTime);
        }

        void bind(Message messageObject) {
            message.setText(messageObject.getMessage());
            time.setText(messageObject.getSendDate());
            sender.setText("You");
        }

    }

    public class ReceivedViewHolder extends RecyclerView.ViewHolder {

        TextView sender, message, time;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.twSender);
            message = itemView.findViewById(R.id.twMessage);
            time = itemView.findViewById(R.id.twTime);
        }

        void bind(Message messageObject) {
            message.setText(messageObject.getMessage());
            time.setText(messageObject.getSendDate());
            sender.setText("Customer Service");
        }

    }
}
