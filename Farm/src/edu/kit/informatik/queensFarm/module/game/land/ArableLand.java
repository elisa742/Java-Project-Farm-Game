package edu.kit.informatik.queensFarm.module.game.land;

import edu.kit.informatik.queensFarm.module.FarmException;
import edu.kit.informatik.queensFarm.module.game.Timer;
import edu.kit.informatik.queensFarm.module.game.VegetableSet;
import edu.kit.informatik.queensFarm.module.game.VegetableType;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes an arable land and execute commands on this land.
 *
 * @author uyjad
 * @version 1.0
 */
public class ArableLand {
    private static final String STRING_NO_COUNTDOWN = "*";
    private static final String REGEX_PRINT_SHORT_ABBREVIATION = " %s %s ";
    private static final String REGEX_PRINT_MIDDLE_ABBREVIATION = " %s %s";
    private static final String REGEX_PRINT_LONG_ABBREVIATION = "%s %s";
    private static final String REGEX_NO_PLANT = "     ";
    private static final String REGEX_SHOW_PLANT_TYPE = "  %s  ";
    private static final String REGEX_CAPACITY_STATUS = " %d/%d ";
    private Timer landTimer;
    private ArableLandType landType;
    private String landAbbreviation;
    private int capacity;
    private List<VegetableType> allowedVegetableTypes;
    private VegetableSet landStorage;
    private boolean isLandFull;

    /**
     * Constructs an arable land with the given land type.
     * @param landType land type
     */
    public ArableLand(ArableLandType landType) {
        this.landType = landType;
        this.landAbbreviation = landType.getAbbreviation();
        this.capacity = landType.getCapacity();
        this.allowedVegetableTypes = landType.getTypeRange();
        this.isLandFull = false;
    }

    /**
     * Constructs an empty arable land.
     */
    public ArableLand() {
    }

    /**
     * Checks whether the land has zero vegetables.
     * @return true if the land is empty, otherwise false
     */
    public boolean isLandEmpty() {
        return this.landStorage == null;
    }

    /**
     * Sets the status of land as full, which means the capacity is reached.
     */
    public void setLandAsFull() {
        this.isLandFull = true;
    }

    /**
     * Adds a vegetable.
     * @param vegetableName vegetable name
     * @throws FarmException if given vegetable type is not allowed, or the land is not empty
     */
    public void addVegetable(String vegetableName) throws FarmException {
        if (!isLandEmpty()) {
            throw new FarmException(ErrorMessage.LAND_IS_OCCUPIED.toString());
        }
        VegetableType type = VegetableType.getVegetableTypeFromString(vegetableName);
        if (!isTypeAllowed(type)) {
            throw new FarmException(ErrorMessage.ILLEGAL_VEGETABLE_TYPE.toString());
        }
        this.landStorage = new VegetableSet(type);
        startTimer(type.getDurationOfGrowth());
    }

    /**
     * Attempts to remove vegetables from the land by given amount.
     * If successful, return the copy of the removed vegetables.
     *
     * @param amountToRemove amount to remove
     * @return copy of current vegetable set
     * @throws FarmException if the land is empty, amount is 0, or there is a shortage
     */
    public VegetableSet removeVegetable(int amountToRemove) throws FarmException {
        if (isLandEmpty()) {
            throw new FarmException(ErrorMessage.LAND_IS_EMPTY.toString());
        }
        if (amountToRemove == 0) {
            throw new FarmException(ErrorMessage.ILLEGAL_AMOUNT_TO_HARVEST.toString());
        }
        int difference = getVegetableAmount() - amountToRemove;
        if (difference < 0) {
            throw new FarmException(ErrorMessage.INSUFFICIENT_VEGETABLES.toString());
        }

        VegetableSet copySet = new VegetableSet(this.landStorage.getType(), amountToRemove);

        if (difference == 0) {
            this.landStorage = null;
            if (this.landTimer != null) {
                removeTimer();
            }
        } else {
            //if previously land is full, then start a new timer now.
            if (isLandFull) {
                startTimer(this.landStorage.getType().getDurationOfGrowth());
            }
            this.landStorage.reduceVegetable(amountToRemove);
        }
        if (isLandFull) {
            this.isLandFull = false;
        }
        return copySet;
    }

    /**
     * Starts the timer with the starting point.
     * @param duration the countdown's starting point
     */
    public void startTimer(int duration) {
        this.landTimer = new Timer(duration);
    }

    /**
     * Removes the timer.
     */
    public void removeTimer() {
        this.landTimer = null;
    }

    /**
     * Executes the countdown and gets the number of the grown vegetables.
     * @return the number of the grown vegetables
     */
    public int getGrowth() {
        if (!executeCountdown()) {
            int previousAmount = getVegetableAmount();
            growVegetable();
            return getVegetableAmount() - previousAmount;
        }
        return 0;
    }

    /**
     * Executes countdown. If there is no timer, do nothing. Otherwise, count down the timer.
     * @return true if there is no timer, or it is not the last turn, otherwise false
     */
    public boolean executeCountdown() {
        if (this.landTimer != null) {
            return this.landTimer.countdown();
        }
        return true;
    }

    /**
     * Vegetables grow when it hits the countdown.
     * Vegetable will only grow up to the capacity of the land and then remove the timer.
     */
    public void growVegetable() {
        int currentAmount = getVegetableAmount();
        if (currentAmount * 2 >= this.capacity) {
            VegetableType currentType = this.landStorage.getType();
            this.landStorage = new VegetableSet(currentType, this.capacity);
            setLandAsFull();
            removeTimer();
        } else {
            this.landTimer.restart();
            this.landStorage.doubleAmount();
        }
    }

    /**
     * Gets the name string of land type.
     * @return the name string of land type
     */
    public String getLandTypeString() {
        return this.landType.getName();
    }

    /**
     * Gets the list of string of land details in "print board" format.
     * @return the list of string of land details in "print board" format
     */
    public List<String> printLandInBoard() {
        List<String> totalList = new ArrayList<>();
        String countdownStatus;
        if (this.landTimer == null) {
            countdownStatus = STRING_NO_COUNTDOWN;
        } else {
            countdownStatus = String.valueOf(this.landTimer.getRemainingTurns());
        }

        if (this.landAbbreviation.length() < 2) {
            totalList.add(String.format(REGEX_PRINT_SHORT_ABBREVIATION, this.landAbbreviation, countdownStatus));
        } else if (this.landAbbreviation.length() == 2) {
            totalList.add(String.format(REGEX_PRINT_MIDDLE_ABBREVIATION, this.landAbbreviation, countdownStatus));
        } else {
            totalList.add(String.format(REGEX_PRINT_LONG_ABBREVIATION, this.landAbbreviation, countdownStatus));
        }

        if (isLandEmpty()) {
            totalList.add(REGEX_NO_PLANT);
        } else {
            totalList.add(String.format(REGEX_SHOW_PLANT_TYPE, this.landStorage.getType().getAbbreviation()));
        }

        totalList.add(String.format(REGEX_CAPACITY_STATUS, getVegetableAmount(), this.capacity));

        return totalList;
    }



    /**
     * checks whether the vegetable type is allowed in this land.
     * @param typeToCheck the vegetable type to be checked.
     * @return true if the vegetable type is allowed in this land, otherwise false.
     */
    public boolean isTypeAllowed(VegetableType typeToCheck) {
        return this.allowedVegetableTypes.contains(typeToCheck);
    }

    /**
     * Gets the amount of vegetable.
     * @return the amount of vegetable
     */
    public int getVegetableAmount() {
        if (isLandEmpty()) {
            return 0;
        }
        return this.landStorage.getAmount();
    }
}
