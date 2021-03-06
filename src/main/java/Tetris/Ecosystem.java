package Tetris;

import java.util.Arrays;
import java.util.Random;

import static Tetris.Constants.*;

/**
 * Created by kazuhiro on 6/4/17.
 */
public class Ecosystem {
    int[][][] ecosystem;
    int generation;
    int[][] cumulativeFitness;
    int[] worstScore;
    int[] bestScore;
    int exchangePeriod;
    public Random nature;


    public Ecosystem(int[][][] ecosystem, int generation, int exchangePeriod) {
        this.ecosystem = ecosystem;
        this.generation = generation;
        this.exchangePeriod = exchangePeriod;
        this.nature = new Random();
    }

    public void play() {
        cumulativeFitness = new int[ecosystem.length][ecosystem[0].length];
        worstScore = new int[ecosystem.length];
        Arrays.fill(worstScore, Integer.MAX_VALUE);
        bestScore = new int[ecosystem.length];
        Arrays.fill(bestScore, 0);

        if (generation % exchangePeriod == 0) {
            System.out.println("Before exchange: ");
            printPopulation();
        }

        Thread[] threads = new Thread[ecosystem.length];

        System.out.println("Generation " + generation + "... ");

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new GameThread(ecosystem[i], i, cumulativeFitness, worstScore, bestScore));
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
                System.out.println("Population " + (i + 1) + " (worst | avg | best): " + worstScore[i] + " | " + ((cumulativeFitness[i][cumulativeFitness[i].length - 1])/ecosystem[i].length) + " | " + bestScore[i]);
            } catch(Exception e) {

            }
        }

        select();
        crossOver();
        mutate();
        generation++;
    }

    public static int[] copy(int[] a) {
        int[] field = new int[a.length];
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
        for (int j = 0; j < ecosystem.length; j++) {
            int totalFitness = cumulativeFitness[j][cumulativeFitness.length - 1];
            if (totalFitness == 0) {
                ecosystem[j] = BigBang.resetPopulation();
                return;
            }

            int[][] nextPopulation = new int[POPULATION_SIZE][SET_LENGTH];
            if (generation % exchangePeriod != 0) {
                for (int i = 0; i < nextPopulation.length; i++) {
                    int randomNumber = nature.nextInt(totalFitness);
                    int selectedSet = binarySearch(cumulativeFitness[j], randomNumber);
                    nextPopulation[i] = copy(ecosystem[j][selectedSet]);
                }
            } else {
                for (int i = 0; i < nextPopulation.length; i++) {
                    int index = i + j;
                    if (index >= nextPopulation.length) {
                        index = index - nextPopulation.length;
                    }
                    int randomNumber = nature.nextInt(cumulativeFitness[index][cumulativeFitness[index].length - 1]);
                    int selectedSet = binarySearch(cumulativeFitness[index], randomNumber);
                    nextPopulation[i] = copy(ecosystem[index][selectedSet]);
                }
            }
            ecosystem[j] = nextPopulation;
        }
    }

    private void crossOver() {
        for (int k = 0; k < ecosystem.length; k++) {
            for (int i = 1; i < ecosystem[k].length; i = i + 2) {
                int crossOverPoint = nature.nextInt(ecosystem[k][i].length);
                for (int j = crossOverPoint; j < ecosystem[k][i].length; j++) {
                    int temp = ecosystem[k][i - 1][j];
                    ecosystem[k][i - 1][j] = ecosystem[k][i][j];
                    ecosystem[k][i][j] = temp;
                }
                //System.out.println("Cross over point for new sets " + (i-1) + " and " + i + " is " + crossOverPoint + ".");
            }
        }
    }

    private void mutate() {
        for (int k = 0; k < ecosystem.length; k++) {
            for (int i = 0; i < ecosystem[k].length; i++) {
                for (int j = 0; j < ecosystem[k][i].length; j++) {
                    if (nature.nextDouble() < MUTATION_RATE) {
                        ecosystem[k][i][j] = BigBang.randomWeight();
                        //System.out.println("Mutation occurs in new set " + i + " at index " + j + " with mutation value: " + mutation + ".");
                    }
                }
            }
        }
    }

    private void printPopulation() {
        System.out.println("");
        for (int i = 0; i < ecosystem.length; i++) {
            for(int j = 0; j < ecosystem[i].length; j++) {
                for (int k = 0; k < ecosystem[i][j].length; k++) {
                    System.out.print(ecosystem[i][j][k] + ",");
                }
                System.out.println("");
            }
            System.out.println("");
        }
    }
}
