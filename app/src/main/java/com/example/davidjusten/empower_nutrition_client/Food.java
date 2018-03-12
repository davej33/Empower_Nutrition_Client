package com.example.davidjusten.empower_nutrition_client;

/**
 * Created by davidjusten on 3/12/18.
 */

public class Food {

    private String mName, mPrice, mDesc, mImage;

    public Food(){}

    public Food(String name, String price, String desc, String image){
        mName = name;
        mPrice = price;
        mDesc = desc;
        mImage = image;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
