package com.vlasovs.fastshop.app.classes;

public class MiniItem {

    private int id;
    private String pictureURL;
    private String name;
    private float rating;
    private float price;
    private int reviews;

    public MiniItem(int id, String pictureURL, String name, float rating, float price, int reviews) {
        this.id = id;
        this.pictureURL = pictureURL;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.reviews = reviews;
    }

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
}
