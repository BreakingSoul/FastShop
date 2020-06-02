package com.vlasovs.fastshop.app.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {

    private int id;
    private int clientId;
    private String pictureURL;
    private String name;
    private float price;
    private int amount;

    public CartItem(int id, int clientId, String pictureURL, String name, float price, int amount) {
        this.id = id;
        this.clientId = clientId;
        this.pictureURL = pictureURL;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    protected CartItem(Parcel in) {
        id = in.readInt();
        clientId = in.readInt();
        pictureURL = in.readString();
        name = in.readString();
        price = in.readFloat();
        amount = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(clientId);
        parcel.writeString(pictureURL);
        parcel.writeString(name);
        parcel.writeFloat(price);
        parcel.writeInt(amount);
    }
}
