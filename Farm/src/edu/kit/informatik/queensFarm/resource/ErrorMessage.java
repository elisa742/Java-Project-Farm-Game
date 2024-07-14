package edu.kit.informatik.queensFarm.resource;

/**
 * This class stores all error messages.
 *
 * @author uyjad
 * @version 1.0
 */
public enum ErrorMessage {
    /**
     * If there is no vegetable of a specific type, the error message will be printed.
     */
    LACK_OF_VEGETABLE("vegetable of this type is not found."),

    /**
     * If land is not owned, the error message will be printed.
     */
    LAND_NOT_OWNED("the land is not owned."),

    /**
     * If land the empty, the error message will be printed.
     */
    LAND_IS_EMPTY("land is empty."),

    /**
     * If land has vegetable, the error message will be printed.
     */
    LAND_IS_OCCUPIED("land is growing vegetable already."),

    /**
     * If vegetable type is not allowed in a land, the error message will be printed.
     */
    ILLEGAL_VEGETABLE_TYPE("this vegetable type is not allowed."),

    /**
     * If there is shortage of vegetables, the error message will be printed.
     */
    INSUFFICIENT_VEGETABLES("there is a shortage of this vegetable type."),

    /**
     * If the amount to harvest is 0, the error message will be printed.
     */
    ILLEGAL_AMOUNT_TO_HARVEST("the amount can not be 0."),

    /**
     * If the player does not have enough gold, the error message will be printed.
     */
    INSUFFICIENT_GOLD("player does not have enough gold."),

    /**
     * If wrong name was given by user, the error message will be printed.
     */
    ILLEGAL_NAME ("wrong format of name."),

    /**
     * When all lands are sold out, the error message will be printed.
     */
    LAND_SOLD_OUT ("all lands are sold out."),

    /**
     * When coordinates are illegal, the error message will be printed.
     */
    ILLEGAL_COORDINATE ("location is not valid"),

    /**
     * If command was not found, the error message will be printed.
     */
    ILLEGAL_COMMAND ("command not found."),

    /**
     * If integer was smaller than the expected number, the error message will be printed.
     * Expects one format argument: the number of expected argument(integer).
     */
    ILLEGAL_VALUE_OF_INTEGER("number must be equal or greater than %d."),

    /**
     * If a string could not be parsed to an integer, the error message will be printed.
     * Expects one format argument: the illegal string.
     */
    ILLEGAL_INTEGER("cannot parse %s into an integer."),
    /**
     * If the argument is blank, the error message will be printed.
     */
    ILLEGAL_BLANK("blank string is not allowed."),

    /**
     * If the name given by use are not vegetable names, the error message will be printed.
     */
    ILLEGAL_LANDTYPE_NAME("incorrect name of land type."),

    /**
     * If the name given by use are not vegetable names, the error message will be printed.
     */
    ILLEGAL_VEGETABLE_NAME("incorrect name of vegetable type.");

    private static final String PREFIX = "Error: ";
    private final String message;

    /**
     * Constructs an error message.
     *
     * @param message the template of the message
     */
    ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * Formats the error message with the user given input.
     *
     * @param args user given input that is needed for the error message
     * @return the format adjusted error message
     */
    public String format(Object... args) {
        return PREFIX + String.format(this.message, args);
    }

    @Override
    public String toString() {
        return PREFIX + this.message;
    }
}
