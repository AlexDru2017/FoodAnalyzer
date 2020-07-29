package com.example.final_project2;


//Population class
public class Population {

    private int popSize;
    private Individual[] individuals;
    private int fittest;

    public Population() {
        super();
        popSize = 10;
        individuals = new Individual[10];
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

}
