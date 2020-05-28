package com.vlasovs.fastshop.app.background;

import com.vlasovs.fastshop.app.classes.MiniItem;

import java.util.ArrayList;

public interface MiniItemResponse {
    void processFinish(ArrayList<MiniItem> itemList);
}
