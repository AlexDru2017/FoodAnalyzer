package com.example.final_project2;


import android.content.Context;



public class FoodItem {

    private String itemName;
    private String itemCategory;
    private String itemNDB; // primary key on https://ndb.nal.usda.gov/ndb/
    private String itemMeasure;
    private float itemQuantity;

    public FoodItem() {
        itemName = "";
        itemCategory = "";
        itemNDB = "";
        itemMeasure = "";
        itemQuantity = 0;
    }

    public FoodItem(String itemName, String itemCategory, String itemNDB, String itemMeasure) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemNDB = itemNDB;
        this.itemMeasure = itemMeasure;
        this.itemQuantity = (float) 1;
    }

    public FoodItem(String itemName, String itemCategory, String itemNDB, String itemMeasure, float itemQuantity) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemNDB = itemNDB;
        this.itemMeasure = itemMeasure;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemNDB() {
        return itemNDB;
    }

    public void setItemNDB(String itemNDB) {
        this.itemNDB = itemNDB;
    }

    public String getItemMeasure() {
        return itemMeasure;
    }

    public void setItemMeasure(String itemMeasure) {
        this.itemMeasure = itemMeasure;
    }

    public float getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(float itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "itemName='" + itemName + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", itemNDB='" + itemNDB + '\'' +
                ", itemMeasure='" + itemMeasure + '\'' +
                ", itemQuantity=" + itemQuantity +
                '}';
    }

}
