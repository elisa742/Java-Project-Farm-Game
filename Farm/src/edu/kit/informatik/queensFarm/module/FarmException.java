package edu.kit.informatik.queensFarm.module;

/**
 * An exception indicating that a user input is invalid.
 *
 * @author uyjad
 * @version 1.0
 */

public class FarmException extends Exception {
    private static final long serialVersionUID = -3368927514480888449L;

    /**
     * Constructs a new instance of farm exception with error message to be printed.
     *
     * @param message error message to be printed
     */
    public FarmException(final String message) {
        super(message);
    }
}
