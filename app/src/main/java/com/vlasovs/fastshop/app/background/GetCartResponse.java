package com.vlasovs.fastshop.app.background;

import com.vlasovs.fastshop.app.classes.CartItem;

import java.util.ArrayList;

public interface GetCartResponse {
    void processFinish(ArrayList<CartItem> itemList);
}
