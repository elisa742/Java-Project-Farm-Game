package edu.kit.informatik.queensFarm.module.game.market;

import edu.kit.informatik.queensFarm.module.FarmException;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a price display where the current price tag is found and updated.
 *
 * @author uyjad
 * @version 1.0
 */
public enum PriceDisplay {
    /**
     * All possible pair prices of mushroom and carrot.
     */
    MUSHROOM_AND_CARROT_PAIR_PRICE(List.of(
            List.of(12, 3),
            List.of(15, 2),
            List.of(16, 2),
            List.of(17, 2),
            List.of(20, 1)), 2, List.of("mushroom", "carrot")),

    /**
     * All possible pair prices of tomato and salad.
     */
    TOMATO_AND_SALAD_PAIR_PRICE(List.of(
            List.of(3, 6),
            List.of(5, 5),
            List.of(6, 4),
            List.of(7, 3),
            List.of(9, 2)), 2, List.of("tomato", "salad"));

    private static final int FIRST_ITEM_POSITION = 0;
    private static final int SECOND_ITEM_POSITION = 1;
    private static final int MINIMUM_OF_INDEX = 0;
    private static final int MAXIMUM_OF_INDEX = 4;

    // This size of this price tag is 5.
    // Each element is a "Pair" object, which contains the name of vegetables and the according prices.
    private final Pair[] priceTag;

    // The price index indicates the location of the symbol "*" or "◇" in the price tag.
    private int priceIndex;
    private final List<String> itemNames;

    /**
     * Constructs a price display.
     *
     * @param pairPriceSet all possible pair prices of two vegetables
     * @param priceIndex the current price index
     * @param itemNames names of the items in this price display
     */
    PriceDisplay(List<List<Integer>> pairPriceSet, int priceIndex, List<String> itemNames) {
        this.priceIndex = priceIndex;
        this.itemNames = itemNames;
        this.priceTag = initiatePriceTag(pairPriceSet, itemNames);
    }

    /**
     * Gets the names of the items in this price display.
     *
     * @return names of the items in this price display
     */
    public List<String> getItemNames() {
        return itemNames;
    }

    /**
     * Initiates the price tag.
     * For example, for the pair of mushroom and carrot, in the for loop, the first entry of the Map "pair price set"
     * will be split into price of different items. Then we create a Pair object and store the two prices into it.
     * After the for loop, turn the list into the array.
     *
     * @param pairPriceSet all possible pair prices for a pair of vegetables
     * @param itemNames names of the items to store together with the price
     * @return an array of price tags for a pair of vegetables
     */
    public Pair[] initiatePriceTag(List<List<Integer>> pairPriceSet, List<String> itemNames) {
        List<Pair> totalPairsOfPrices = new ArrayList<>();
        for (List<Integer> list : pairPriceSet) {
            int priceOfFirstItem = list.get(FIRST_ITEM_POSITION);
            int priceOfSecondItem = list.get(SECOND_ITEM_POSITION);

            Pair pairOfPrices = new Pair();
            pairOfPrices.addPriceTag(itemNames.get(FIRST_ITEM_POSITION), priceOfFirstItem);
            pairOfPrices.addPriceTag(itemNames.get(SECOND_ITEM_POSITION), priceOfSecondItem);

            totalPairsOfPrices.add(pairOfPrices);
        }
        return totalPairsOfPrices.toArray(new Pair[0]);
    }

    /**
     * Update the position of price indicator.
     *
     * @param changeOfIndex change of position of price indicator
     */
    public void changePriceIndex(int changeOfIndex) {
        int newIndex = this.priceIndex - changeOfIndex;

        if (newIndex > MAXIMUM_OF_INDEX) {
            this.priceIndex = MAXIMUM_OF_INDEX;
        } else if (newIndex < MINIMUM_OF_INDEX) {
            this.priceIndex = MINIMUM_OF_INDEX;
        } else {
            this.priceIndex = newIndex;
        }
    }

    /**
     * Gets the price tag from string vegetable name.
     *
     * @param vegetableName name of vegetable
     * @return price tag of the vegetable
     * @throws FarmException if the vegetable name given is not valid
     */
    public static int getPriceFromString(String vegetableName) throws FarmException {
        PriceDisplay display = getPriceDisplayFromString(vegetableName);
        return display.priceTag[display.priceIndex].getPrice(vegetableName);
    }

    /**
     * Gets the price display from the string vegetable name.
     *
     * @param vegetableName name of vegetable
     * @return price display which the input vegetable belongs
     * @throws FarmException if the vegetable name given is not valid
     */
    public static PriceDisplay getPriceDisplayFromString(String vegetableName) throws FarmException {
        for (PriceDisplay display : PriceDisplay.values()) {
            if (display.itemNames.contains(vegetableName)) {
                return display;
            }
        }
        throw new FarmException(ErrorMessage.ILLEGAL_VEGETABLE_NAME.toString());
    }

}
