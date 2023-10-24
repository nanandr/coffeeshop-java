package com.nandanarafiardika.mycoffeeshopdrinklist.model;

import com.google.gson.annotations.SerializedName;

public class CoffeeDrink {
    private String id;
    private String name;
    private String price;
    private String detail;
    private String photoThumbnail;
    private String photoPoster;
    private String createdAt;

    @SerializedName("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("price")
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    @SerializedName("detail")
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    @SerializedName("photoThumbnail")
    public String getPhotoThumbnail() {
        return photoThumbnail;
    }
    public void setPhotoThumbnail(String photoThumbnail) {
        this.photoThumbnail = photoThumbnail;
    }

    @SerializedName("photoPoster")
    public String getPhotoPoster() {
        return photoPoster;
    }
    public void setPhotoPoster(String photoPoster) {
        this.photoPoster = photoPoster;
    }

    @SerializedName("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CoffeeDrink(String id, String name, String price, String description, String photoThumbnail, String photoPoster, String createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.detail = description;
        this.photoThumbnail = photoThumbnail;
        this.photoPoster = photoPoster;
        this.createdAt = createdAt;
    }
}
