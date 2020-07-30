package com.example.final_project2;

import java.util.ArrayList;
import java.util.Random;

//Individual class
public class Individual {

    private float fitness = 0;
    private ArrayList<Meal> genes;
    private int geneLength = 5;
    int distance = 0;


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

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    //Calculate fitness
    public void calcFitness() {

        calcDistance();
        fitness = (float) 1.0/(1 + distance)*100;


    }

    //    calc Distance
    public int calcDistance() {
        int sum = 0;
        int calForUser = 250; //צריך לבוא מהחלון של נתוני המתשתמש , חישוב קלוריות לפי גובה משקל..
        for (int i = 0; i < geneLength; i++) {
            sum = sum + (int) genes.get(i).getCal();
        }

        distance = Math.abs(sum - calForUser);
        return distance;
    }




}