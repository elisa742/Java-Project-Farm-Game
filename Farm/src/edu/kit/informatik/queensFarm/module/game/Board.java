package edu.kit.informatik.queensFarm.module.game;

import edu.kit.informatik.queensFarm.module.FarmException;
import edu.kit.informatik.queensFarm.module.game.land.ArableLand;
import edu.kit.informatik.queensFarm.module.game.land.ArableLandType;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * This class describes board that have the location of a player's land. We can execute operations on the lands
 * through the board.
 *
 * @author uyjad
 * @version 1.0
 */
public class Board {
    private static final int LAND_PRICE_CALCULATION_FACTOR = 10;
    private static final String REGEX_OUTER_BAR = "|%s|";
    private static final int LENGTH_OF_CONTENT_STRINGS = 5;
    private static final int LENGTH_OF_BLANK_STRINGS = 6;
    private static final char SEPARATOR_CHAR_SPACE = ' ';
    private static final String SEPARATOR_SPACE = " ";
    private static final char NULL_CHARACTER = '\0';
    private static final String SEPARATOR_VERTICAL_BAR = "|";
    private static final String MESSAGE_VEGETABLES_GROWN = "%d vegetables have grown since your last turn.";
    private static final String MESSAGE_SINGLE_VEGETABLE_GROWN = "1 vegetable has grown since your last turn.";
    private static final String MESSAGE_SUCCESSFUL_LAND_PURCHASE = "You have bought a %s for %d gold.";
    private final Map<Vector2D, ArableLand> landMap;

    /**
     * Constructs a board with the default given lands.
     */
    public Board() {
        this.landMap = new HashMap<>();
        this.landMap.put(new Vector2D(0, 1), new ArableLand(ArableLandType.FIELD));
        this.landMap.put(new Vector2D(-1, 0), new ArableLand(ArableLandType.GARDEN));
        this.landMap.put(new Vector2D(1, 0), new ArableLand(ArableLandType.GARDEN));
    }

    /**
     * Checks the vegetables grown in this round
     * @return  the number of vegetables grown in this round
     */
    public String checkBoard() {
        int sumOfVegetables = 0;
        for (ArableLand land : this.landMap.values()) {
            sumOfVegetables += land.getGrowth();
        }
        if (sumOfVegetables == 0) {
            return null;
        } else if (sumOfVegetables == 1) {
            return MESSAGE_SINGLE_VEGETABLE_GROWN;
        } else {
            return String.format(MESSAGE_VEGETABLES_GROWN, sumOfVegetables);
        }
    }

    /**
     * Gets land by coordinates.
     *
     * @param coordinate coordinate of the land
     * @return the land searched by the coordinates
     * @throws FarmException if the player does not own this land
     */
    public ArableLand getLandByCoordinate(Vector2D coordinate) throws FarmException {
        if (!this.landMap.containsKey(coordinate)) {
            throw new FarmException(ErrorMessage.LAND_NOT_OWNED.toString());
        }
        return this.landMap.get(coordinate);
    }

    /**
     * Checks whether the given location is valid for new land.
     *
     * @param coordinate the given location
     * @return true if the given location is valid for new land, otherwise false
     */
    public boolean isLocationValid(Vector2D coordinate) {
        if (this.landMap.containsKey(coordinate) || coordinate.equals(new Vector2D(0, 0))) {
            return false;
        }
        for (Vector2D vector2D : this.landMap.keySet()) {
            if (vector2D.isValidLocation(coordinate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the price of the given location.
     *
     * @param coordinateToCheck location to check
     * @return the price of the given location
     */
    public int calculateLandPrice(Vector2D coordinateToCheck) {
        return LAND_PRICE_CALCULATION_FACTOR * (Math.abs(coordinateToCheck.getX())
                + Math.abs(coordinateToCheck.getY()) - 1);
    }

    /**
     * Attempts to purchase a new land. If attempt is successful, return the success message and stores the land.
     *
     * @param coordinate the location attempt to buy
     * @param land the new land attempt to buy
     * @throws FarmException if the location to buy land is invalid
     */
    public void buyNewLand(Vector2D coordinate, ArableLand land) throws FarmException {
        if (!isLocationValid(coordinate)) {
            throw new FarmException(ErrorMessage.ILLEGAL_COORDINATE.toString());
        }
        this.landMap.put(coordinate, land);
        System.out.println(String.format(MESSAGE_SUCCESSFUL_LAND_PURCHASE, land.getLandTypeString(),
                calculateLandPrice(coordinate)));
    }

    /**
     * Gets the status of board in string format.
     * Breaks the information into three rows and stores them in an array.
     *
     * @param barnDetails details of barn
     * @return status of board in string format
     */
    public String shownBoard(List<String> barnDetails) {
        List<Vector2D> coordinates = new ArrayList<>();
        for (Vector2D coordinate : this.landMap.keySet()) {
            coordinates.add(coordinate);
        }
        Collections.sort(coordinates, Comparator.comparingInt(Vector2D::getX));
        int smallestXCoordinate = coordinates.get(0).getX();

        Collections.sort(coordinates, Comparator.comparingInt(Vector2D::getX).reversed());
        int greatestXCoordinate = coordinates.get(0).getX();

        Collections.sort(coordinates, Comparator.comparingInt(Vector2D::getY).reversed());
        int greatestYCoordinate = coordinates.get(0).getY();

        //This is the number of rows in the array where strings will be stored.
        int numOfRows = 3 * (greatestYCoordinate + 1);
        String[][] printDetails = new String[numOfRows][greatestXCoordinate - smallestXCoordinate + 1];

        for (int j = greatestYCoordinate; j > -1; j--) {
            for (int i = smallestXCoordinate; i < greatestXCoordinate + 1; i++) {
                Vector2D coordinateToSearch = new Vector2D(i, j);
                int rowOrder = 3 * (greatestYCoordinate - j);
                if (this.landMap.containsKey(coordinateToSearch)) {
                    //Break the information from one land into three rows.
                    for (int k = 0; k < 3; k++) {
                        int value = i - smallestXCoordinate;
                        printDetails[rowOrder][i - smallestXCoordinate] = this.landMap.get(coordinateToSearch)
                                .printLandInBoard().get(k);
                        rowOrder++;
                    }
                }
            }
        }
        return printArray(printDetails, barnDetails, smallestXCoordinate);
    }

    /**
     * Gets the string of array in board format.
     *
     * @param arrayToPrint array to print
     * @param xCoordinate coordinate x
     * @param barnDetails details of barn
     * @return the string of array in board format
     */
    public String printArray(String[][] arrayToPrint, List<String> barnDetails, int xCoordinate) {
        for (int k = 0; k < barnDetails.size(); k++) {
            arrayToPrint[arrayToPrint.length - barnDetails.size() + k][-xCoordinate] = barnDetails.get(k);
        }

        StringJoiner completeString = new StringJoiner(System.lineSeparator());
        //Space string to add at the beginning or end of the row representation when needed.
        String blankStarterOrEnding = new String(new char[LENGTH_OF_BLANK_STRINGS])
                .replace(NULL_CHARACTER, SEPARATOR_CHAR_SPACE);

        String blankStrings = new String(new char[LENGTH_OF_CONTENT_STRINGS])
                .replace(NULL_CHARACTER, SEPARATOR_CHAR_SPACE);

        for (int i = 0; i < arrayToPrint.length; i++) {
            StringBuilder rowRepresentation = new StringBuilder();
            StringJoiner spaceConnector = new StringJoiner(SEPARATOR_SPACE);
            //String joiner for non-blank string with vertical bar as separator.
            StringJoiner validString = new StringJoiner(SEPARATOR_VERTICAL_BAR);

            for (int j = 0; j < arrayToPrint[i].length; j++) {
                String currentString = arrayToPrint[i][j];
                // If the string begins with blank string, we need to add 6 spaces.
                if (j == 0 && currentString == null) {
                    //because it is special 6 spaces.
                    spaceConnector.add(blankStarterOrEnding);
                    continue;
                }
                // When the last string is blank, if previously we have non-blank strings, then add 6 space as ending.
                // If previous string is also blank, then fill in the spaceConnector and add it to the output string.
                if (j == arrayToPrint[i].length - 1 && currentString == null) {
                    if (validString.length() != 0) {
                        rowRepresentation.append(String.format(REGEX_OUTER_BAR, validString.toString()));
                        rowRepresentation.append(blankStarterOrEnding);
                    } else {
                        spaceConnector.add(blankStarterOrEnding);
                        rowRepresentation.append(spaceConnector.toString());
                    }
                    continue;
                }

                if (currentString == null) {
                    if (validString.length() != 0) {
                        rowRepresentation.append(String.format(REGEX_OUTER_BAR, validString.toString()));
                        validString = new StringJoiner(SEPARATOR_VERTICAL_BAR);
                    }
                    spaceConnector.add(blankStrings);

                    continue;
                }
                //If previously have blank string, now have non-blank string, then add previous space first.
                if (spaceConnector.length() != 0) {
                    rowRepresentation.append(spaceConnector.toString());
                    spaceConnector = new StringJoiner(SEPARATOR_SPACE);
                }

                //Starts concatenating valid string together to form final output.
                validString.add(currentString);
                if (j == arrayToPrint[i].length - 1) {
                    rowRepresentation.append(String.format(REGEX_OUTER_BAR, validString.toString()));
                }
            }
            completeString.add(rowRepresentation.toString());
        }
        return completeString.toString();
    }
}
