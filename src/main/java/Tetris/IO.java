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

    public void exportEcosystem(int[][][] population) {
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

    public void exportEcosystemFromTerminal(int[][][] population) {
        try {
            PrintWriter writer = new PrintWriter("src/main/java/Tetris/output.txt", "UTF-8");

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

    public int[][][] importEcosystem() {
        int[][][] population = new int[POPULATION_COUNT][POPULATION_SIZE][SET_LENGTH];

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
                                    population[k][i][j] = Integer.parseInt(line[j]);
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

    public int[][][] importEcosystemFromTerminal() {
        int[][][] population = new int[POPULATION_COUNT][POPULATION_SIZE][SET_LENGTH];

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/Tetris/input.txt"));
            String[] line;
            line = br.readLine().trim().split(",");
            int inputPopulationCount = Integer.parseInt(line[0]);
            int inputPopulationSize = Integer.parseInt(line[1]);
            int inputSetLength = Integer.parseInt(line[2]);

            for (int k = 0; k < POPULATION_COUNT; k++) {
                if (k < inputPopulationCount) {
                    for (int i = 0; i < POPULATION_SIZE; i++) {
                        if (i < inputPopulationSize) {
                            skip(br);
                            line = br.readLine().split(",");
                            for (int j = 0; j < SET_LENGTH; j++) {
                                if (j < inputSetLength) {
                                    population[k][i][j] = Integer.parseInt(line[j]);
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

    private void skip(BufferedReader br) throws IOException {
        int count = 0;
        while (count < 4) {
            if (br.read() == ':') {
                count++;
            }
        }
        br.read();
    }
}
