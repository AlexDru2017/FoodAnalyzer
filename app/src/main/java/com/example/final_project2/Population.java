package com.example.final_project2;


import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

//Population class
public class Population {

    private int popSize;
    private Individual[] individuals;
    private int fittest;


    public Population() {
        super();
        popSize = 5; //לשנות ל10
        individuals = new Individual[5]; //לשנות ל10
        fittest = 0;
    }

    //Initialize population
    public void initializePopulation(int size) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
        Log.d("asdasd", "initializePopulation: ");
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }

    public int getFittest() {
        return fittest;
    }

    public void setFittest(int fittest) {
        this.fittest = fittest;
    }

    //Calculate fitness of each individual
    public void calculateFitness() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
        }
        // getFittest();
    }

    //Check if the calories the user consumes are equal to what he needs to consumes.
    public Individual checkDistance() {

        float disIndividuals = 0;
        for (int i = 0; i < individuals.length; i++) {
            disIndividuals = individuals[i].calcDistance();
            if (disIndividuals >= 0 && disIndividuals <= 20) {
                // if (disIndividuals == 0) {
                return individuals[i];
            }
        }
        return null;
    }

    //Get the new individual
    public void getNewIndividual() {

        float sum = 0;
        sum = sumFittest();
        probability(sum);
        sort();
        cumulativeProbability();
        NewIndividual();


    }

    //clac sum Fittest of Individual for probability
    public float sumFittest() {
        float sum = 0;
        for (int i = 0; i < individuals.length; i++) {
            sum = sum + individuals[i].getFitness();
        }
        return sum;
    }

    //probability for Individual
    public void probability(float sum) {
        float probability = 0;
        for (int i = 0; i < individuals.length; i++) {
            probability = (float) individuals[i].getFitness() / sum;
            individuals[i].setFitness(probability);
        }
    }

    //sort Individual array for cumulative Probability
    public void sort() {
        int i = 0, j = 0;
        Individual[] tempIndividuals;
        tempIndividuals = new Individual[1];
        tempIndividuals[0] = new Individual();

        for (i = 0; i < individuals.length - 1; i++) {
            for (j = i + 1; j < individuals.length; j++) {
                if (individuals[i].getFitness() > individuals[j].getFitness()) {
                    tempIndividuals[0] = individuals[i];
                    individuals[i] = individuals[j];
                    individuals[j] = tempIndividuals[0];

                }
            }
        }

    }

    //cumulative Probability
    public void cumulativeProbability() {
        float sum = 0;
        for (int i = 0; i < individuals.length; i++) {
            sum = sum + individuals[i].getFitness();
            individuals[i].setFitness(sum);
        }
    }

    //New Individual
    public void NewIndividual() {
        int flag = 1;
        Random rn = new Random();
        double r = 0;
        Individual[] NewIndividual;
        NewIndividual = new Individual[popSize];
        for (int i = 0; i < popSize; i++) {
            NewIndividual[i] = new Individual();
        }

        for (int i = 0; i < popSize; i++) {
            r = Math.random();
            flag = 1;
            for (int j = 0; j < popSize && flag != 0; j++) {
                if (r <= individuals[j].getFitness()) {
                    NewIndividual[i] = individuals[j];
                    flag = 0;
                }
            }
        }

        for (int i = 0; i < popSize; i++) {

            individuals[i] = NewIndividual[i];
        }


    }


    //selection Parent from the population
    public ArrayList<Integer> selectionParent() {
        Random rn = new Random();
        double r = 0;
        double pc = 0.45;
        ArrayList<Integer> numOfParent = new ArrayList<Integer>();

        for (int i = 0; i < individuals.length; i++) {
            r = Math.random();
            if (r < pc) {
                numOfParent.add(i);
                System.out.println("i : " + i);

            }

        }
        return numOfParent;
    }


    //crossover Of Parent
    public void crossoverOfParent() {
        int sizeParent = 0;
        int i = 0, j = 0, n = 0;
        Random rn = new Random();
        int r = 0;
        ArrayList<Integer> numOfParent = new ArrayList<Integer>();

        //selection Parent for crossover
        numOfParent = selectionParent();
        sizeParent = numOfParent.size();


        //check if Parent is selected
        if (sizeParent < 2) {
            return;
        }


        //Create a new object because the objects in java is by reference
        Individual[] NewIndividual;
        NewIndividual = new Individual[popSize];
        for (i = 0; i < popSize; i++) {
            NewIndividual[i] = new Individual();
        }


        //print for check
       /* for (i = 0; i < individuals.length; i++) {
            System.out.println("--------------------");
            for (j = 0; j < individuals[i].getGenes().size(); j++) {
                System.out.println(" : " + j + " " + individuals[i].getGenes().get(j).getMealName());
            }
            System.out.println("--------------------");
        }
        System.out.println("sizeParent : " + sizeParent);*/


        //crossover Of Parent
        for (i = 0, n = 0; i < individuals.length; i++) {


            //Extreme case 1 : last parent that selected should cross with the first
            if (numOfParent.get(n) == i && n == sizeParent - 1) {
                do {
                    r = rn.nextInt(sizeParent);
                } while (r < 0);
                System.out.println("-----i=" + i + "r:" + r + "---");

                for (j = 0; j < popSize; j++) {
                    if (j < r) {
                        ArrayList<Meal> genes = NewIndividual[i].getGenes();
                        Meal meal = individuals[i].getGenes().get(j);
                        genes.set(j, meal);
                    } else {
                        ArrayList<Meal> genes = NewIndividual[i].getGenes();
                        Meal meal = individuals[numOfParent.get(0)].getGenes().get(j);
                        genes.set(j, meal);
                    }
                }
            }
            //case 2 : cross 2 parents
            else if (numOfParent.get(n) == i && n < sizeParent - 1) {
                do {
                    r = rn.nextInt(sizeParent);
                } while (r < 0);
                System.out.println("-----i=" + i + "r:" + r + "---");

                for (j = 0; j < popSize; j++) {
                    if (j < r) {
                        ArrayList<Meal> genes = NewIndividual[i].getGenes();
                        Meal meal = individuals[i].getGenes().get(j);
                        genes.set(j, meal);
                    } else {
                        ArrayList<Meal> genes = NewIndividual[i].getGenes();
                        Meal meal = individuals[numOfParent.get(n + 1)].getGenes().get(j);
                        genes.set(j, meal);
                    }
                }

                n++;

            }
            //case 3 : no cross , the gene is transmitted as is
            else {
                NewIndividual[i] = individuals[i];
            }

        }


        for (i = 0; i < popSize; i++) {

            individuals[i] = NewIndividual[i];
        }

        //print for check
      /*  for (i = 0; i < individuals.length; i++) {
            System.out.println("--------------------");
            for (j = 0; j < individuals[i].getGenes().size(); j++) {
                System.out.println(" : " + j + " " + individuals[i].getGenes().get(j).getMealName());
            }
            System.out.println("--------------------");
        }

        System.out.println("--------------------");*/


    }

    //mutation
    public void mutationForPopulation() {
        ArrayList<Meal> mMeal = MealMenu.getmMeal();
        int len = 0, numberOfMutations = 0;
        int rForMeal = 0, rForMutations = 0;
        int row = 0, col = 0;
        int i = 0, j = 0;
        Random rn = new Random();


        len = individuals.length * individuals[0].getGenes().size();
        numberOfMutations = (int) len / 10;


        //print for check
        System.out.println("----------mutationForPopulation before ----------");

        for (i = 0; i < individuals.length; i++) {
            System.out.println("--------------------");
            for (j = 0; j < individuals[i].getGenes().size(); j++) {
                System.out.println(" : " + j + " " + individuals[i].getGenes().get(j).getMealName());
            }
            System.out.println("--------------------");
        }


        for (i = 0; i < numberOfMutations; i++) {
            System.out.println("-----i=" + i + "---");

            //A random number from the user's list of foods
            do {
                rForMeal = rn.nextInt(mMeal.size() - 1);
                System.out.println("-----rForMeal=" + rForMeal + "---");

            } while (rForMeal < 0);
            //A random number from the two-dimensional matrix of meals
            do {
                rForMutations = rn.nextInt(len - 1);
                System.out.println("-----rForMutations=" + rForMutations + "---");

            } while (rForMutations < 0);
            row = (rForMutations / individuals[0].getGenes().size());
            col = rForMutations % individuals[0].getGenes().size();
            System.out.println("-----row=" + row + "col:" + col + "---");

            ArrayList<Meal> genes = individuals[row].getGenes();
            Meal meal = mMeal.get(rForMeal);
            genes.set(col, meal);
        }

        //print for check

        System.out.println("----------mutationForPopulation after ----------");

        for (i = 0; i < individuals.length; i++) {
            System.out.println("--------------------");
            for (j = 0; j < individuals[i].getGenes().size(); j++) {
                System.out.println(" : " + j + " " + individuals[i].getGenes().get(j).getMealName());
            }
            System.out.println("--------------------");
        }

        System.out.println("--------------------");


    }


}
