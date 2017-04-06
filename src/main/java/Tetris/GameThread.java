package Tetris;

/**
 * Created by kazuhiro on 6/4/17.
 */
public class GameThread implements Runnable {
    Population population;
    int setIndex;

    public GameThread(Population population, int setIndex) {
        this.population = population;
        this.setIndex = setIndex;
    }

    public void run() {
        State s = new State();
        //TFrame frame = new TFrame(s);
        double[] set = population.population[setIndex];
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
        if (s.getRowsCleared() < population.worstScore) population.worstScore = s.getRowsCleared();
        if (s.getRowsCleared() > population.bestScore) population.bestScore = s.getRowsCleared();
        population.fitnessValues[setIndex] = s.getRowsCleared();
        System.out.print(s.getRowsCleared() + ", ");
    }
}
