package Tetris;

/**
 * Created by kazuhiro on 6/4/17.
 */
public class GameThread implements Runnable {
    double[][] population;
    int populationIndex;
    int[][] cumulativeFitness;
    int[] worstScore;
    int[] bestScore;

    public GameThread(double[][] population, int populationIndex, int[][] cumulativeFitness, int[] worstScore, int[] bestScore) {
        this.population = population;
        this.populationIndex = populationIndex;
        this.cumulativeFitness = cumulativeFitness;
        this.worstScore = worstScore;
        this.bestScore = bestScore;
    }

    private synchronized void updateCumulativeFitness(int iteration, int score) {
        if (iteration == 0) {
            cumulativeFitness[populationIndex][iteration] = score;
        } else {
            cumulativeFitness[populationIndex][iteration] = score + cumulativeFitness[populationIndex][iteration - 1];
        }
    }

    private synchronized void updateWorstScore(int score) {
        if (score < worstScore[populationIndex]) worstScore[populationIndex] = score;
    }

    private synchronized void updateBestScore(int score) {
        if (score > bestScore[populationIndex]) bestScore[populationIndex] = score;
    }

    private String getString(double[] set) {
        String s = "";
        for (double weight : set) {
            s += weight + ",";
        }
        return s;
    }

    private synchronized void printSet(int score, int setIndex, double[] set) {
        System.out.println("Set " + setIndex + " of population " + populationIndex + " cleared " + score + " lines.");
        String s = getString(set);
        System.out.println(s);
    }

    public void run() {
        for (int i = 0; i < population.length; i++) {
            State s = new State();
            //TFrame frame = new TFrame(s);
            PlayerSkeleton p = new PlayerSkeleton(population[i]);
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
            //System.out.println("Set " + i + " of population " + populationIndex + " cleared " + s.getRowsCleared() + " rows.");
            if (s.getRowsCleared() >= 1000000) printSet(s.getRowsCleared(), i, population[i]);
            updateCumulativeFitness(i, s.getRowsCleared());
            updateWorstScore(s.getRowsCleared());
            updateBestScore(s.getRowsCleared());
        }
    }
}
