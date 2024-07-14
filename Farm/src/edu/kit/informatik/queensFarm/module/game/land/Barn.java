package edu.kit.informatik.queensFarm.module.game.land;

import edu.kit.informatik.queensFarm.module.game.Timer;
import edu.kit.informatik.queensFarm.module.game.VegetableSet;
import edu.kit.informatik.queensFarm.module.game.VegetableType;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * This class describes the barn and execute actions on barn.
 *
 * @author uyjad
 * @version 1.0
 */
public class Barn {
    private static final String BARN_IS_EMPTY_SYMBOL = "*";
    private static final String MESSAGE_SINGLE_HARVEST = "You have harvested 1 %s.";
    private static final String MESSAGE_MULTIPLE_HARVEST = "You have harvested %d %s.";
    private static final String MESSAGE_SPOILS_COUNTDOWN = "Barn (spoils in %d turns)";
    private static final String MESSAGE_SPOILS_NEXT_TURN = "Barn (spoils in 1 turn)";
    private static final String MESSAGE_VEGETABLES_ARE_SPOILED = "The vegetables in your barn are spoiled.";
    private static final String BARN_ABBREV_WITH_REMAINING_ROUND = " B %s ";
    /**
     * this string represents a line that contains 5 spaces. It will be used for command "show board".
     */
    private static final String BOTTOM_OR_TOP_LINE = "     ";
    private static final int STARTING_POINT_OF_COUNTDOWN = 6;
    private Timer barnTimer;
    private final Map<VegetableType, VegetableSet> barnStorage;

    /**
     * Constructs a barn. Set six turns as the starting point of its timer. Also store the default given vegetables.
     */
    public Barn() {
        this.barnTimer = new Timer(STARTING_POINT_OF_COUNTDOWN);
        this.barnStorage = new HashMap<>();
        setUpBarn();
    }

    /**
     * Sets up the barn with one unit of each vegetable type.
     */
    public void setUpBarn() {
        for (VegetableType type : VegetableType.values()) {
            VegetableSet newVegetableSet = new VegetableSet(type);
            this.barnStorage.put(type, newVegetableSet);
        }
    }

    /**
     * Check whether the barn has the vegetables requested.
     * @param list list of vegetables to be checked
     * @return true if the barn has the vegetables requested, otherwise return false
     */
    public boolean checkAvailability(List<VegetableSet> list) {
        for (VegetableSet set : list) {
            VegetableType typeToCheck = set.getType();
            if (!this.barnStorage.containsKey(typeToCheck)) {
                System.out.println(ErrorMessage.LACK_OF_VEGETABLE.toString());
                return false;
            }
            int currentAmount = this.barnStorage.get(typeToCheck).getAmount();
            int remainingAmount = currentAmount - set.getAmount();
            if (remainingAmount < 0) {
                System.out.println(ErrorMessage.INSUFFICIENT_VEGETABLES.toString());
                return false;
            }
        }
        return true;
    }

    /**
     * Removes vegetables
     * @param list list of vegetables to remove
     */
    public void executeRemoval(List<VegetableSet> list) {
        for (VegetableSet set : list) {
            VegetableType type = set.getType();
            int remainingAmount = this.barnStorage.get(type).getAmount() - set.getAmount();
            if (remainingAmount == 0) {
                this.barnStorage.remove(type);
            } else {
                this.barnStorage.put(type, new VegetableSet(type, remainingAmount));
            }
        }
        if (this.barnStorage.isEmpty()) {
            removeTimer();
        }
    }

    /**
     * Sells vegetables.
     * @param salesList vegetables to sell
     * @return true if sale is successful, otherwise false
     */
    public boolean sell(List<VegetableSet> salesList) {
        if (checkAvailability(salesList)) {
            executeRemoval(salesList);
            return true;
        }
        return false;
    }

    /**
     * Adds vegetable set to the barn and prints successful harvest message.
     * @param setToAdd vegetable set to add
     */
    public void addVegetable(VegetableSet setToAdd) {
        if (isBarnEmpty()) {
            startTimer();
        }
        int amountToAdd = setToAdd.getAmount();
        VegetableType type = setToAdd.getType();

        if (this.barnStorage.containsKey(type)) {
            int currentAmount = this.barnStorage.get(type).getAmount();
            this.barnStorage.put(type, new VegetableSet(type, currentAmount + amountToAdd));
        } else {
            this.barnStorage.put(type, new VegetableSet(type, amountToAdd));
        }
    }

    /**
     * Adds harvested vegetable to barn.
     * @param setToAdd vegetable to add
     */
    public void harvest(VegetableSet setToAdd) {
        addVegetable(setToAdd);
        int amountToAdd = setToAdd.getAmount();
        if (amountToAdd == 1) {
            System.out.println(String.format(MESSAGE_SINGLE_HARVEST, setToAdd.getName()));
        } else {
            System.out.println(String.format(MESSAGE_MULTIPLE_HARVEST, amountToAdd, setToAdd.getPluralForm()));
        }
    }

    /**
     * Buys a vegetable from market
     * @param type type of vegetable to buy
     */
    public void buyVegetable(VegetableType type) {
        addVegetable(new VegetableSet(type));
    }

    /**
     * Gets all vegetables in the barn.
     * @return all vegetables in the barn. If barn is empty, return null
     */
    public List<VegetableSet> getAllVegetables() {
        if (this.barnTimer == null) {
            return null;
        }
        List<VegetableSet> allVegetables = new ArrayList<>();
        for (VegetableSet vegetableSet : this.barnStorage.values()) {
            allVegetables.add(vegetableSet);
        }
        return allVegetables;
    }


    /**
     * Gets the barn details about whether vegetables are spoiled, if not spoiled, return the remaining turns left.
     * @return details about whether vegetables are spoiled, if not spoiled, return the remaining turns left
     */
    public String getBarnDetails() {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

        int remainingTurns = this.barnTimer.getRemainingTurns();
        if (remainingTurns > 1) {
            stringJoiner.add(String.format(MESSAGE_SPOILS_COUNTDOWN, remainingTurns));
        } else {
            stringJoiner.add(MESSAGE_SPOILS_NEXT_TURN);
        }
        return stringJoiner.toString();
    }

    /**
     * Checks if the barn is empty.
     * @return true if the barn is empty, otherwise false
     */
    public boolean isBarnEmpty() {
        return this.barnTimer == null;
    }

    /**
     * Checks the status of barn by counting down.
     * If the last turn is finished, remove all and return message about spoiled vegetables.
     * @return message about spoiled vegetables if last turn is finished, otherwise null
     */
    public String endRoundCheck() {
        if (this.barnTimer != null) {
            // if last round is finished, remove all.
            if (!this.barnTimer.countdown()) {
                clearBarn();
                return MESSAGE_VEGETABLES_ARE_SPOILED;
            }
        }
        return null;
    }

    /**
     * Clears the barn.
     */
    public void clearBarn() {
        this.barnStorage.clear();
        removeTimer();
    }

    /**
     * Starts the timer.
     */
    public void startTimer() {
        this.barnTimer = new Timer(STARTING_POINT_OF_COUNTDOWN);
    }

    /**
     * Removes the timer.
     */
    public void removeTimer() {
        this.barnTimer = null;
    }

    /**
     * Gets the status information of barn in the format requested by command "shown board".
     * @return the status information of barn in the format requested by command "shown board"
     */
    public List<String> printBarnInBoard() {
        List<String> list = new ArrayList<>();
        list.add(BOTTOM_OR_TOP_LINE);

        String remainingTurns;
        if (this.barnTimer == null) {
            remainingTurns = BARN_IS_EMPTY_SYMBOL;
        } else {
            remainingTurns = String.valueOf(this.barnTimer.getRemainingTurns());
        }

        list.add(String.format(BARN_ABBREV_WITH_REMAINING_ROUND, remainingTurns));
        list.add(BOTTOM_OR_TOP_LINE);
        return list;
    }

}
