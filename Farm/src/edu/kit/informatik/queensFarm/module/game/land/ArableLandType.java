package edu.kit.informatik.queensFarm.module.game.land;

import edu.kit.informatik.queensFarm.module.FarmException;
import edu.kit.informatik.queensFarm.module.game.VegetableType;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.List;

/**
 * This class describes the types of lands.
 *
 * @author uyjad
 * @version 1.0
 */
public enum ArableLandType {
    /**
     * Garden.
     */
    GARDEN("Garden", "G", 2, List.of(VegetableType.values())),

    /**
     * Field.
     */
    FIELD("Field", "Fi", 4, List.of(VegetableType.CARROT, VegetableType.SALAD,
            VegetableType.TOMATO)),

    /**
     * Large Field.
     */
    LARGE_FIELD("Large Field", "LFi", 8, List.of(VegetableType.CARROT, VegetableType.SALAD,
            VegetableType.TOMATO)),

    /**
     * Forest.
     */
    FOREST("Forest", "Fo", 4, List.of(VegetableType.CARROT, VegetableType.MUSHROOM)),

    /**
     * Large Forest.
     */
    LARGE_FOREST("Large Forest", "LFo", 8, List.of(VegetableType.CARROT,
            VegetableType.MUSHROOM));

    private final List<VegetableType> typeRange;
    private final String name;
    private final String abbreviation;
    private final int capacity;

    /**
     * Constructs an arable land type.
     *
     * @param name name of land type
     * @param abbreviation abbreviation of land type
     * @param capacity capacity of land type
     * @param typeRange allowed list of vegetable types
     */
    ArableLandType(String name, String abbreviation, int capacity, List<VegetableType> typeRange) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.capacity = capacity;
        this.typeRange = typeRange;
    }

    /**
     * Gets the abbreviation of the land type.
     *
     * @return the abbreviation of the land type
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Gets the name of the land type.
     *
     * @return the name of the land type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the capacity of this land type.
     *
     * @return the capacity of this land type
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the list of allowed vegetable types.
     *
     * @return list of allowed vegetable types
     */
    public List<VegetableType> getTypeRange() {
        return typeRange;
    }

    /**
     * Gets the arable land type from the valid string name.
     *
     * @param typeName name of the type to be searched
     * @throws FarmException if the name is not valid
     * @return the arable land type if the input string name is valid, otherwise return null
     */
    public static ArableLandType getLandTypeFromString(String typeName) throws FarmException {
        for (ArableLandType type : ArableLandType.values()) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }
        throw new FarmException(ErrorMessage.ILLEGAL_LANDTYPE_NAME.toString());
    }
}
