package edu.kit.informatik.queensFarm.module.game;

/**
 * This class describes a timer that can count down and restart counting down.
 *
 * @author uyjad
 * @version 1.0
 */
public class Timer {
    private final int maximumNumberOfTurns;
    private int remainingTurns;

    /**
     * Constructs a timer that takes the maximum number of turns as the start of counting down.
     * Initiate the remaining turn with the starting point.
     *
     * @param numberOfTurns maximum number of turns, which is where the counting down starts
     */
    public Timer(int numberOfTurns) {
        this.maximumNumberOfTurns = numberOfTurns;
        this.remainingTurns = numberOfTurns;
    }

    /**
     * Attempts to count down.
     * If the current turn is last turn, just return false.
     * If still has more than one turn before expiration, execute counting down and return true.
     *
     * @return true if the timer still has more than one turn before expiration. If it is the last turn, return wrong.
     */
    public boolean countdown() {
        if (this.remainingTurns == 1) {
            return false;
        }
        this.remainingTurns--;
        return true;
    }

    /**
     * Restarts the timer.
     */
    public void restart() {
        this.remainingTurns = this.maximumNumberOfTurns;
    }

    /**
     * Gets the remaining turns before expiration.
     *
     * @return remaining turns before expiration
     */
    public int getRemainingTurns() {
        return this.remainingTurns;
    }
}
