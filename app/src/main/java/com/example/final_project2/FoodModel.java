package com.example.final_project2;

import android.net.Uri;
import android.util.Log;


public class FoodModel {

    private String protein = "0.0";
    private String sugar = "0.0";
    private String fat = "0.0";
    private String carb = "0.0";
    private String energy = "0.0";

    public FoodModel() {
    }

    public FoodModel(String protein, String sugar, String fat, String carb, String energy) {
        this.protein = protein;
        this.sugar = sugar;
        this.fat = fat;
        this.carb = carb;
        this.energy = energy;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCarb() {
        return carb;
    }

    public void setCarb(String carb) {
        this.carb = carb;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                "protein='" + protein + '\'' +
                ", sugar='" + sugar + '\'' +
                ", fat='" + fat + '\'' +
                ", carb='" + carb + '\'' +
                ", energy='" + energy + '\'' +
                '}';
    }
}