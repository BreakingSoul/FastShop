package com.vlasovs.fastshop.app.background;

import com.vlasovs.fastshop.app.classes.Item;

import java.util.ArrayList;

public interface ItemResponse {
    void processFinish(ArrayList<Item> itemList);
}
