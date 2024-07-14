package edu.kit.informatik.queensFarm.module.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

/**
 * This class turns the string information into a required format.
 *
 * @author uyjad
 * @version 1.0
 */
public class Printer {
    private static final int LENGTH_OF_COLON_AND_SINGLE_SPACE = 2;
    private static final String SEPARATOR_COLON_AND_SPACE = ": ";
    private static final String SUM_REPRESENTATION = "Sum: ";
    private static final char NULL_CHARACTER = '\0';
    private static final char LINE_SEPARATOR = '-';
    private static final String GOLD_REPRESENTATION = "Gold: ";
    private final List<VegetableSet> list;

    /**
     * Formats the message to be printed.
     * @param inputList list of vegetable that will be used as the content of message
     */
    public Printer(List<VegetableSet> inputList) {
        this.list = cloneList(inputList);
    }

    /**
     * Copies the list of vegetable set if the list is not empty.
     *
     * @param inputList given list of vegetable set
     * @return a copied list of vegetable set if the list is not empty, otherwise return null
     */
    public List<VegetableSet> cloneList(List<VegetableSet> inputList) {
        if (inputList == null) {
            return null;
        }
        if (!inputList.isEmpty()) {
            List<VegetableSet> newList = new ArrayList<>();
            for (VegetableSet vegetableSet : inputList) {
                newList.add(vegetableSet);
            }
            return newList;
        }
        return null;
    }

    /**
     * Gets the fixed length of regex for printing barn information.
     *
     * @param goldAmount amount of gold
     * @return the fixed length of regex for printing barn information
     */
    public int getBarnLengthOfRegex(int goldAmount) {
        int maximumAmount = Math.max(goldAmount, getSumOfVegetables());
        return getMaximumLengthOfPluralism() + String.valueOf(maximumAmount).length()
                + LENGTH_OF_COLON_AND_SINGLE_SPACE;
    }

    /**
     * Calculates the fixed length of regex for printing market information.
     *
     * @return the fixed length of regex for printing market information.
     */
    public int getMarketLengthOfRegex() {
        return getLengthOfMaximumAmount() + getMaximumLengthOfPluralism() + LENGTH_OF_COLON_AND_SINGLE_SPACE;
    }

    /**
     * Calculates the maximum length of the plural names of vegetables.
     *
     * @return the maximum length of the plural names of vegetables
     */
    public int getMaximumLengthOfPluralism() {
        int maxLength = 0;
        for (VegetableSet vegetable : this.list) {
            int length = vegetable.getPluralForm().length();
            maxLength = Math.max(maxLength, length);
        }
        return maxLength;
    }

    /**
     * Calculates the length of the maximum number of vegetables.
     *
     * @return the maximum number of vegetables
     */
    public int getLengthOfMaximumAmount() {
        int maxLength = 0;
        for (VegetableSet vegetable : this.list) {
            int length = String.valueOf(vegetable.getAmount()).length();
            maxLength = Math.max(length, maxLength);
        }
        return maxLength;
    }

    /**
     * Calculates the sum of vegetables.
     *
     * @return the sum of vegetables
     */
    public int getSumOfVegetables() {
        int sum = 0;
        for (VegetableSet vegetableSet : this.list) {
            sum += vegetableSet.getAmount();
        }
        return sum;
    }

    /**
     * Formats string of current prices of vegetables in the market.
     *
     * @return string that represents current price of vegetables in the market.
     */
    public String printMarket() {
        int maxLength = getMarketLengthOfRegex();
        return printVegetableTable(maxLength);
    }

    /**
     * Formats the string of information of vegetables.
     *
     * @param maxLength max length of the printed string of each vegetable
     * @return string of vegetables
     */
    public String printVegetableTable(int maxLength) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

        for (VegetableSet vegetableSet : this.list) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(vegetableSet.getPluralForm());
            stringBuilder.append(SEPARATOR_COLON_AND_SPACE);

            stringBuilder.append(convert(stringBuilder.toString(), maxLength,
                    String.valueOf(vegetableSet.getAmount())));

            stringJoiner.add(stringBuilder.toString());
        }
        return stringJoiner.toString();
    }

    /**
     * Converts the string of an item and value to a format with fixed length.
     *
     * @param itemToFormat item to format
     * @param maxLength length of the output string
     * @param numberToAdd value to add in the output string
     * @return the string of an item and value to a format with fixed length
     */
    public String convert(String itemToFormat, int maxLength, String numberToAdd) {
        int length = maxLength - itemToFormat.length();
        String newRegex = "%" + length + "s";
        return String.format(newRegex, numberToAdd);
    }

    /**
     * Formats message with the amount of gold.
     *
     * @param goldAmount amount of gold
     * @return message with the amount of gold
     */
    public String showGoldOnly(int goldAmount) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GOLD_REPRESENTATION);
        stringBuilder.append(goldAmount);

        return stringBuilder.toString();
    }

    /**
     * Formats string of vegetables in the barn.
     *
     * @param goldAmount amount of gold
     * @param barnMessage message about when vegetables are going to spoil
     * @return string of vegetables in the barn
     */
    public String showBarn(int goldAmount, String barnMessage) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        stringJoiner.add(barnMessage);
        if (this.list == null) {
            stringJoiner.add(showGoldOnly(goldAmount));
            return stringJoiner.toString();
        }

        //Adds vegetable details.
        int maxLength = getBarnLengthOfRegex(goldAmount);
        Comparator<VegetableSet> compareByAmountThenName = Comparator
                .comparingInt(VegetableSet::getAmount)
                .thenComparing(VegetableSet::getName);
        Collections.sort(this.list, compareByAmountThenName);
        stringJoiner.add(printVegetableTable(maxLength));

        //Adds the separator line made of symbol "-".
        String separatorLine = new String(new char[maxLength]).replace(NULL_CHARACTER, LINE_SEPARATOR);
        stringJoiner.add(separatorLine);

        //Adds the sum of vegetables.
        stringJoiner.add(SUM_REPRESENTATION + convert(SUM_REPRESENTATION, maxLength,
                String.valueOf(getSumOfVegetables())));

        //Adds a new line.
        stringJoiner.add("");

        //Adds gold information.
        stringJoiner.add(GOLD_REPRESENTATION + convert(GOLD_REPRESENTATION, maxLength, String.valueOf(goldAmount)));

        return stringJoiner.toString();
    }

}