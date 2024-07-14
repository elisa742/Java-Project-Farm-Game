package edu.kit.informatik.queensFarm.module;

/**
 * An exception indicating that the name given by user is not valid.
 *
 * @author uyjad
 * @version 1.0
 */
public class InputException extends Exception {
    private static final long serialVersionUID = 5937156274931788702L;

    /**
     * Constructs a new instance of farm exception with error message to be printed.
     *
     * @param message error message to be printed
     */
    public InputException(final String message) {
        super(message);
    }
}
