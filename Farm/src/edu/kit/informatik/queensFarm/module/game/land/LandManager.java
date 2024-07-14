package edu.kit.informatik.queensFarm.module.game.land;

import edu.kit.informatik.queensFarm.module.FarmException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class describes a land manager that can create new list of free lands.
 *
 * @author uyjad
 * @version 1.0
 */
public class LandManager {
    private static final int[] NUMBER_OF_DIFFERENT_LANDTYPE = new int[]{2, 3, 2, 2, 1};

    private final int numOfPlayers;

    /**
     * Constructor of a land manager.
     *
     * @param numOfPlayers number of players
     */
    public LandManager(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Initiates the list of available lands.
     * @return get list of available lands based on the string list of names of land types
     */
    public List<ArableLand> initiateAvailableLands() {
        List<ArableLand> totalLandList = new ArrayList<>();
        ArableLand newLand = new ArableLand();
        for (String nameOfLandType : getStringsOfAvailableLands()) {
            try {
                newLand = new ArableLand(ArableLandType.getLandTypeFromString(nameOfLandType));
            } catch (FarmException e) {
                System.out.println(e.getMessage());
            }
            totalLandList.add(newLand);
        }
        return totalLandList;
    }

    /**
     * Gets a list of all available lands' strings.
     *
     * @return a list of all available lands' strings
     */
    public List<String> getStringsOfAvailableLands() {
        List<String> allAvailableLands = new ArrayList<>();

        int index = 0;
        for (ArableLandType typeToAdd : ArableLandType.values()) {
            int numberOfLandsPerType = this.numOfPlayers * NUMBER_OF_DIFFERENT_LANDTYPE[index];
            allAvailableLands.addAll(Collections.nCopies(numberOfLandsPerType, typeToAdd.getName()));
            index++;
        }
        return allAvailableLands;
    }
}
