package Tetris;

/**
 * Created by kazuhiro on 8/4/17.
 */
public class ReadTerminal {
    public static void main (String args[]) {
        IO io = new IO();
        io.exportEcosystemFromTerminal(io.importEcosystemFromTerminal());
    }
}
