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

    }
}
