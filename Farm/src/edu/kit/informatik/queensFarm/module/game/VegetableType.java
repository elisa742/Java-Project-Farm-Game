package edu.kit.informatik.queensFarm.module.game;

/**
 * This class describes the types of vegetables.
 *
 * @author uyjad
 * @version 1.0
 */
public enum VegetableType {
    /**
     * Mushroom.
     */
    MUSHROOM("mushroom", "M", 4, "mushrooms"),

    /**
     * Carrot.
     */
    CARROT("carrot", "C", 1, "carrots"),

    /**
     * Tomato.
     */
    TOMATO("tomato", "T", 3, "tomatoes"),

    /**
     * Salad.
     */
    SALAD("salad", "S", 2, "salads");

    private final String name;
    private final String abbreviation;
    private final int durationOfGrowth;
    private final String pluralForm;

    /**
     * Constructs a vegetable type.
     *
     * @param name name of this vegetable type
     * @param abbreviation abbreviation of this vegetable type
     * @param durationOfGrowth duration of growth by this vegetable type
     * @param pluralForm plural form of the name of this vegetable type
     */
    VegetableType(String name, String abbreviation, int durationOfGrowth, String pluralForm) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.durationOfGrowth = durationOfGrowth;
        this.pluralForm = pluralForm;
    }

    /**
     * Gets the duration of growth of the vegetable type.
     *
     * @return the duration of growth of the vegetable type
     */
    public int getDurationOfGrowth() {
        return this.durationOfGrowth;
    }

    /**
     * Gets abbreviation of the vegetable type.
     *
     * @return abbreviation of the vegetable type
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Gets the name of the vegetable type.
     *
     * @return  name of the vegetable type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets plural form of the name of this vegetable type.
     *
     * @return  plural form of the name of this vegetable type
     */
    public String getPluralForm() {
        return pluralForm;
    }

    /**
     * Gets the vegetable type according to the given input string if the input is valid.
     *
     * @param vegetableName name of vegetable type
     * @return the vegetable type according to the given input string if the input is valid, otherwise return false
     */
    public static VegetableType getVegetableTypeFromString(String vegetableName) {
        for (VegetableType type : VegetableType.values()) {
            if (vegetableName.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }

}

