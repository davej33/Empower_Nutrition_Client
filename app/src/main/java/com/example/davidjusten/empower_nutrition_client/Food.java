package com.example.davidjusten.empower_nutrition_client;

/**
 * Created by davidjusten on 3/12/18.
 */

public class Food {

    private String mName, mPrice, mDesc, mImage, mUser;
    private String mType;
    private int mQuantity;
    private String mOrderID;
    private String mTime;

    public Food() {
    }

    public Food(String name, String price, String desc, int quantity, String image, String type) {
        mName = name;
        mPrice = price;
        mDesc = desc;
        mImage = image;
        mType = type;
        mQuantity = quantity;

    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getOrderID() {
        return mOrderID;
    }

    public void setOrderID(String mOrderID) {
        this.mOrderID = mOrderID;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }
}
