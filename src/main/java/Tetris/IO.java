package Tetris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import static Tetris.Constants.*;

/**
 *
 * @author JunKiat
 */
public class IO {

    public void exportPopulation(double[][][] population) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");

            writer.print(population.length + ",");
            writer.print(population[0].length + ",");
            writer.println(population[0][0].length + ",");
            for (int i = 0; i < POPULATION_COUNT; i++) {
                for (int j = 0; j < POPULATION_SIZE; j++) {
                    for (int k = 0; k < SET_LENGTH; k++) {
                        writer.print(population[i][j][k] + ",");
                    }
                    writer.println();
                }
                writer.println();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
    }

    public double[][][] importPopulation() {
        double[][][] population = new double[POPULATION_COUNT][POPULATION_SIZE][SET_LENGTH];

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String[] line;
            line = br.readLine().trim().split(",");
            int inputPopulationCount = Integer.parseInt(line[0]);
            int inputPopulationSize = Integer.parseInt(line[1]);
            int inputSetLength = Integer.parseInt(line[2]);

            for (int k = 0; k < POPULATION_COUNT; k++) {
                if (k < inputPopulationCount) {
                    for (int i = 0; i < POPULATION_SIZE; i++) {
                        if (i < inputPopulationSize) {
                            line = br.readLine().split(",");
                            for (int j = 0; j < SET_LENGTH; j++) {
                                if (j < inputSetLength) {
                                    population[k][i][j] = Double.parseDouble(line[j]);
                                } else {
                                    population[k][i][j] = BigBang.randomWeight();
                                }
                            }
                        } else {
                            for (int j = 0; j < SET_LENGTH; j++) {
                                population[k][i][j] = BigBang.randomWeight();
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < POPULATION_SIZE; i++) {
                        for (int j = 0; j < SET_LENGTH; j++) {
                            population[k][i][j] = BigBang.randomWeight();
                        }
                    }
                }
                br.readLine();
            }
        } catch (Exception e) {

        }
        return population;
    }
}
