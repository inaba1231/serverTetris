package Tetris;

/**
 * Created by kazuhiro on 6/4/17.
 */
public class GameThread implements Runnable {
    int[] fitnessValues;
    double[] set;
    int setIndex;

    public GameThread(int[] fitnessValues, double[] set, int setIndex) {
        this.fitnessValues = fitnessValues;
        this.set = set;
        this.setIndex = setIndex;
    }

    private synchronized void updateFitness(int score) {
        fitnessValues[setIndex] = score;
    }

    public void run() {
        State s = new State();
        //TFrame frame = new TFrame(s);
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
        updateFitness(s.getRowsCleared());
        //System.out.print(s.getRowsCleared() + ", ");
    }
}
