package edu.kit.informatik.queensFarm.module.game;

/**
 * This class describes vegetable set with type and amount. We can also execute operations on it.
 *
 * @author uyjad
 * @version 1.0
 */
public class VegetableSet {
    private final VegetableType type;
    private final String pluralForm;
    private int amount;
    private final String abbreviation;
    private final String singularForm;
    private final int growDuration;

    /**
     * Constructor of vegetable set with given vegetable type.
     *
     * @param type vegetable type
     */
    public VegetableSet(VegetableType type) {
        this.type = type;
        this.amount = 1;
        this.abbreviation = type.getAbbreviation();
        this.singularForm = type.getName();
        this.pluralForm = type.getPluralForm();
        this.growDuration = type.getDurationOfGrowth();
    }

    /**
     * Constructor of vegetable set with given vegetable type and amount.
     *
     * @param type vegetable type
     * @param amount amount of vegetable
     */
    public VegetableSet(VegetableType type, int amount) {
        this.type = type;
        this.amount = amount;
        this.abbreviation = type.getAbbreviation();
        this.singularForm = type.getName();
        this.pluralForm = type.getPluralForm();
        this.growDuration = type.getDurationOfGrowth();
    }

    /**
     * Reduces vegetable by the decrement.
     *
     * @param decrement amount to reduce
     */
    public void reduceVegetable(int decrement) {
        this.amount -= decrement;
    }

    /**
     * Gets amount of vegetables.
     *
     * @return amount of vegetables
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the vegetable type.
     *
     * @return vegetable type
     */
    public VegetableType getType() {
        return type;
    }

    /**
     * Doubles the amount of vegetables.
     */
    public void doubleAmount() {
        this.amount *= 2;
    }

    /**
     * Gets the plural form of the name of the vegetable.
     *
     * @return plural form of the name of the vegetable
     */
    public String getPluralForm() {
        return this.pluralForm;
    }

    /**
     * Gets the name of vegetable.
     *
     * @return the name of vegetable
     */
    public String getName() {
        return this.singularForm;
    }
}
