package com.example.final_project2;

import java.io.Serializable;

public class Meal implements Serializable {

    String mealName;
    double cal;

    public Meal(String mealName) {
        this.mealName = mealName;
        cal = 0;
    }

    public Meal(String mealName, String cal) {
        this.mealName = mealName;
        this.cal = Double.parseDouble(cal);
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "mealName='" + mealName + '\'' +
                ", cal=" + cal +
                '}';
    }
}
