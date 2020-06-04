package com.vlasovs.fastshop.app.background;

import com.vlasovs.fastshop.app.classes.Message;

import java.util.ArrayList;

public interface GetMessagesResponse {
    void messageDownloadFinish(ArrayList<Message> messages);
}
