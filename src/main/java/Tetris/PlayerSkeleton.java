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

    private static void printPopulation(double[][][] ecosystem) {
        System.out.println("");
        for (int i = 0; i < ecosystem.length; i++) {
            for(int j = 0; j < ecosystem[i].length; j++) {
                for (int k = 0; k < ecosystem[i][j].length; k++) {
                    System.out.print(ecosystem[i][j][k] + ",");
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        IO io = new IO();
        Ecosystem ecosystem = new Ecosystem(io.importEcosystem(), 221, 4);
        while(true) {
            ecosystem.play();
        }
    }

}
