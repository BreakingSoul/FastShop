package com.vlasovs.fastshop.app.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private int id;
    private String pictureURL;
    private String name;
    private float rating;
    private float price;
    private int reviews;

    public Item(int id, String pictureURL, String name, float rating, float price, int reviews) {
        this.id = id;
        this.pictureURL = pictureURL;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.reviews = reviews;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        pictureURL = in.readString();
        name = in.readString();
        rating = in.readFloat();
        price = in.readFloat();
        reviews = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String picture) {
        this.pictureURL = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(pictureURL);
        parcel.writeString(name);
        parcel.writeFloat(rating);
        parcel.writeFloat(price);
        parcel.writeInt(reviews);
    }
}
