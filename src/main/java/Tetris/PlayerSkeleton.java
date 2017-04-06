package Tetris;

import java.util.Random;

import static Tetris.Constants.*;

public class PlayerSkeleton {

    public static Heuristics h;
    public static MakeMove m;
    public static LegalMoves l;
    public static Random nature;

    public PlayerSkeleton(double[] set) {
        h = new Heuristics(set.length, set);
        m = new MakeMove();
        l = new LegalMoves();
        nature = new Random();
    }

    public int[] pickMove(State s, int[][] legalMoves) {
        int[] move = {0, 0};
        double max = -Double.MAX_VALUE;
        for (int[] x : legalMoves) {
            State dummyState = new State(s);
            dummyState.makeMove(x);
            double sum = h.heuristic(dummyState);
            
            if (sum > max) {
                max = sum;
                move[0] = x[0];
                move[1] = x[1];
            }
        }
        return move;
    }

    public static double[] copy(double[] a) {
        double[] field = new double[a.length];
        for (int i = 0; i < field.length; i++) {
            field[i] = a[i];
        }
        return field;
    }

    private static int[] getCumulativeFitness(double[][] population) {
        int totalFitness = 0;
        int[] cumulativeFitness = new int[population.length];
        int worstScore = Integer.MAX_VALUE;
        int bestScore = 0;

        for (int i = 0; i < population.length; i++) {
            State s = new State();
            //TFrame frame = new TFrame(s);
            double[] set = population[i];
            PlayerSkeleton p = new PlayerSkeleton(set);
            //int movesMade = 0;
            while (!s.hasLost() /* && movesMade < 2000000 */) {
                s.makeMove(p.pickMove(s, s.legalMoves()));
                //movesMade++;
                /*
                 s.draw();
                 s.drawNext(0, 0);
                 try {
                 Thread.sleep(1);
                 } catch (InterruptedException e) {
                 e.printStackTrace();
                 }
                 */

            }

            //frame.dispose();
            //System.out.println("Set " + i + " completed " + s.getRowsCleared() + " rows.");
            if (s.getRowsCleared() < worstScore) worstScore = s.getRowsCleared();
            if (s.getRowsCleared() > bestScore) bestScore = s.getRowsCleared();
            totalFitness += s.getRowsCleared();
            cumulativeFitness[i] = totalFitness;
        }

        System.out.println(worstScore + " | " + bestScore);

        return cumulativeFitness;
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

    private static double[][] select(double[][] population, int[] cumulativeFitness) {
        int totalFitness = cumulativeFitness[cumulativeFitness.length - 1];
        if (totalFitness == 0) {
            return BigBang.resetPopulation();
        }

        double[][] nextPopulation = new double[POPULATION_SIZE][SET_LENGTH];
        for (int i = 0; i < nextPopulation.length; i++) {
            int randomNumber = nature.nextInt(totalFitness);
            int selectedSet = binarySearch(cumulativeFitness, randomNumber);
            nextPopulation[i] = copy(population[selectedSet]);
            //System.out.println("Set " + selectedSet + " has been selected.");
        }
        return nextPopulation;
    }

    private static void crossOver(double[][] population) {
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

    private static void mutate(double[][] population) {
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

    private static void printPopulation(double[][] population) {
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

    public static void main(String[] args) {
        IO io = new IO();
        int generation = 196;
        double[][] population = io.importPopulation();

        while(true) {
            System.out.print("Generation " + generation + " (worst | best): ");
            int[] cumulativeFitness = getCumulativeFitness(population);
            population = select(population, cumulativeFitness);
            crossOver(population);
            mutate(population);
            if (generation % 5 == 0) {
                printPopulation(population);
            }
            generation++;
        }

    }

}
