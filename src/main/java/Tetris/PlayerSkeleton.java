package Tetris;

import java.util.Random;

import static Tetris.Constants.*;

public class PlayerSkeleton {

    public Heuristics h;
    public MakeMove m;
    public LegalMoves l;

    public PlayerSkeleton(double[] set) {
        h = new Heuristics(set.length, set);
        m = new MakeMove();
        l = new LegalMoves();
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

    public static void main(String[] args) {
        IO io = new IO();
        Population population = new Population(io.importPopulation(), 201);
        while(true) {
            population.play();
        }
    }

}
