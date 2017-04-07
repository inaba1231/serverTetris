package Tetris;

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

    private static double[][][] printPopulation(double[][][] population) {
        for (int i = 0; i < population.length; i++) {
            for(int j = 0; j < population[i].length; j++) {
                for (int k = 0; k < population[i][j].length; k++) {
                    System.out.print(population[i][j][k] + ", ");
                }
                System.out.println("");
            }
            System.out.println("");
        }
        return population;
    }

    public static void main(String[] args) {
        IO io = new IO();
        Ecosystem ecosystem = new Ecosystem(io.importEcosystem(), 1, 100);
        while(true) {
            ecosystem.play();
        }
    }

}
