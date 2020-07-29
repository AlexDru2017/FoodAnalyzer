package com.example.final_project2;

import java.util.ArrayList;
import java.util.Random;

//Individual class
public class Individual {

    private int fitness = 0;
    private ArrayList<Meal> genes;
    private int geneLength = 5;

    public Individual() {
        Random rn = new Random();
        int r = 0;
        genes = new ArrayList<>();
        //Set genes randomly for each individual
        if (MealMenu.getmMeal().size() != 0) {
            ArrayList<Meal> mMeal = MealMenu.getmMeal();
            for (int i = 0; i < geneLength; i++) {
                r = Math.abs(rn.nextInt() % mMeal.size());
                genes.add(i, new Meal(mMeal.get(r).getMealName(), mMeal.get(r).getCal()));
            }

            fitness = 0;
        }


    }

    public ArrayList<Meal> getGenes() {
        return genes;
    }

    public void setGenes(ArrayList<Meal> genes) {
        this.genes = genes;
    }

    //Calculate fitness
    public void calcFitness() {

        int distance = 0;
        int sum = 0;
        int calForUser = 2500; //צריך לבוא מהחלון של נתוני המתשתמש , חישוב קלוריות לפי גובה משקל..
        for (int i = 0; i < geneLength; i++) {
            sum = sum + (int) genes.get(i).getCal();
        }

        distance = Math.abs(sum - calForUser);
        fitness = 1 / (1 + distance);


    }


}