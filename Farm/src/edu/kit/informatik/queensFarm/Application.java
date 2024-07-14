package edu.kit.informatik.queensFarm;

import edu.kit.informatik.queensFarm.module.Farm;
import edu.kit.informatik.queensFarm.module.FarmUI;

/**
 * Entry point of this application. It creates the needed instances and runs the interactive command session.
 *
 * @author uyjad
 * @version 1.0
 */
public final class Application {
    /**
     * Error message that arguments are not allowed.
     */
    public static final String ERROR_ARGUMENTS_NOT_ALLOWED = "Error: arguments are not allowed.";

    /**
     * Utility class should not have instance.
     */
    public static final String UTILITY_CLASS_INSTANTIATION = "Utility class cannot be instantiated.";

    /**
     * Private constructor to avoid object generation.
     */
    private Application() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION);
    }

    /**
     * The main entry point of the application. Starts the interactive command line session.
     * Expects no arguments. If there is arguments, error messages will be printed.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println(ERROR_ARGUMENTS_NOT_ALLOWED);
            return;
        }
        Farm farm = new Farm();
        FarmUI farmUI = new FarmUI(farm);
        farmUI.interactive();
    }
}