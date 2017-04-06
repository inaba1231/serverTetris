package Tetris;

import java.util.Random;

import static Tetris.Constants.*;

/**
 * Created by kazuhiro on 6/4/17.
 */
public class Population {
    double[][] population;
    int generation;
    int[] fitnessValues;
    int[] cumulativeFitness;
    public Random nature;
    int worstScore;
    int bestScore;


    public Population(double[][] population, int generation) {
        this.population = population;
        this.generation = generation;
        this.nature = new Random();
    }

    public void play() {
        fitnessValues = new int[population.length];
        cumulativeFitness = new int[population.length];
        worstScore = Integer.MAX_VALUE;
        bestScore = 0;

        Thread[] threads = new Thread[population.length];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new GameThread(fitnessValues, population[i], i));
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();

                if (i == 0) {
                    cumulativeFitness[i] = fitnessValues[i];
                } else {
                    cumulativeFitness[i] = fitnessValues[i] + cumulativeFitness[i - 1];
                }

                if (fitnessValues[i] < worstScore) worstScore = fitnessValues[i];

                if (fitnessValues[i] > bestScore) bestScore = fitnessValues[i];
            } catch(Exception e) {

            }
        }

        System.out.println("Generation " + generation + " (worst | best): " + worstScore + " | " + bestScore);
        if (generation % 5 == 0) {
            printPopulation();
        }
        select();
        crossOver();
        mutate();
        generation++;
    }

    public static double[] copy(double[] a) {
        double[] field = new double[a.length];
        for (int i = 0; i < field.length; i++) {
            field[i] = a[i];
        }
        return field;
    }

    public static int binarySearch(int[] array, int number) {
        return binarySearch(array, number, 0, array.length - 1);
    }

    public static int binarySearch(int[] array, int number, int left, int right) {
        if (left == right) {
            return left;
        }

        int mid = (right - left) / 2 + left;
        if (array[mid] > number) {
            return binarySearch(array, number, left, mid);
        } else {
            return binarySearch(array, number, mid + 1, right);
        }
    }

    private void select() {
        int totalFitness = cumulativeFitness[cumulativeFitness.length - 1];
        if (totalFitness == 0) {
            population = BigBang.resetPopulation();
            return;
        }

        double[][] nextPopulation = new double[POPULATION_SIZE][SET_LENGTH];
        for (int i = 0; i < nextPopulation.length; i++) {
            int randomNumber = nature.nextInt(totalFitness);
            int selectedSet = binarySearch(cumulativeFitness, randomNumber);
            nextPopulation[i] = copy(population[selectedSet]);
            //System.out.println("Set " + selectedSet + " has been selected.");
        }
        population = nextPopulation;
    }

    private void crossOver() {
        for (int i = 1; i < population.length; i = i + 2) {
            int crossOverPoint = nature.nextInt(population[i].length);
            for (int j = crossOverPoint; j < population[i].length; j++) {
                double temp = population[i - 1][j];
                population[i - 1][j] = population[i][j];
                population[i][j] = temp;
            }
            //System.out.println("Cross over point for new sets " + (i-1) + " and " + i + " is " + crossOverPoint + ".");
        }
    }

    private void mutate() {
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[i].length; j++) {
                if (nature.nextDouble() < MUTATION_RATE) {
                    double mutation = nature.nextDouble();
                    if (nature.nextBoolean()) {
                        population[i][j] = mutation;
                    } else {
                        population[i][j] = -mutation;
                    }
                    //System.out.println("Mutation occurs in new set " + i + " at index " + j + " with mutation value: " + mutation + ".");
                }
            }
        }
    }

    private void printPopulation() {
        for (int i = 0; i < population.length; i++) {
            System.out.print("{");
            for (int j = 0; j < population[i].length; j++) {
                System.out.print(population[i][j]);
                if (j != population[i].length - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("}");
            if (i != population.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("");
    }
}
