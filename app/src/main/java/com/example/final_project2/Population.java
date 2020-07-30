package com.example.final_project2;


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
    public int checkDistance() {

        float disIndividuals = 0;
        for (int i = 0; i < individuals.length; i++) {
            disIndividuals = individuals[i].calcDistance();
            //if(disIndividuals>=0&&disIndividuals<=20) {
            if (disIndividuals == 0) {
                return 1;
            }
        }
        return 0;
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


    public ArrayList<Integer> selectionParent() {
        Random rn = new Random();
        double r = 0;
        double pc = 0.4;
        ArrayList<Integer> numOfParent = new ArrayList<Integer>();

        for (int i = 0; i < individuals.length; i++) {
            r = Math.random();
            if (r < pc) {
                numOfParent.add(i);
            }

        }
        return numOfParent;
    }


    public void crossoverOfParent() {
        int sizeParent = 0;
        int i = 0, j = 0, n = 0;
        Random rn = new Random();
        int r = 0;
        ArrayList<Integer> numOfParent = new ArrayList<Integer>();
        Individual[] tempIndividuals;
        tempIndividuals = new Individual[1];
        tempIndividuals[0] = new Individual();

        numOfParent = selectionParent();
        sizeParent = numOfParent.size();

        tempIndividuals[0]= individuals[0];


        for (i = 0; i < sizeParent-1; i++) {
            r = rn.nextInt() % sizeParent;
            for (j = r; i < popSize; j++) {
                ArrayList<Meal> genes = individuals[numOfParent.get(i)].getGenes();
                Meal meal = individuals[numOfParent.get(i+1)].getGenes().get(j);
                genes.set(j, meal);
            }
        }
        for (j = r; i < popSize; j++) {
            r = rn.nextInt() % sizeParent;
            ArrayList<Meal> genes = individuals[numOfParent.get(i)].getGenes();
            Meal meal = tempIndividuals[0].getGenes().get(j);
            genes.set(j, meal);
        }



    }


}
