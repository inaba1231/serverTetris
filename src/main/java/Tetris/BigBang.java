package Tetris;

import java.util.Random;

import static Tetris.Constants.POPULATION_SIZE;
import static Tetris.Constants.SET_LENGTH;

/**
 * Created by kazuhiro on 27/3/17.
 *
 * Generate random initial population.
 */
public class BigBang {

    public static int[][] resetPopulation() {

        int[][] population = new int[POPULATION_SIZE][SET_LENGTH];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < SET_LENGTH; j++) {
                population[i][j] = randomWeight();
            }
        }

        return population;

    }

    public static int randomWeight() {
        Random nature = new Random();
        int weight = nature.nextInt(5000);
        if (nature.nextBoolean()) {
            return weight;
        } else {
            return -weight;
        }
    }
}
