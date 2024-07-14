package edu.kit.informatik.queensFarm.module.game.market;

import edu.kit.informatik.queensFarm.module.FarmException;
import edu.kit.informatik.queensFarm.module.game.Printer;
import edu.kit.informatik.queensFarm.module.game.VegetableSet;
import edu.kit.informatik.queensFarm.module.game.VegetableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a market where trades happen and price would be adjusted.
 *
 * @author uyjad
 * @version 1.0
 */
public class Market {
    private static final String MESSAGE_MULTIPLE_SALE = "You have sold %d vegetables for %d gold.";
    private static final String MESSAGE_SINGLE_SALE = "You have sold 1 vegetable for %d gold.";
    private Map<VegetableType, Integer> salesRecord;

    /**
     * Constructs a market.
     */
    public Market() {
        this.salesRecord = new HashMap<>();
    }

    /**
     * Gets the current price of a certain vegetable.
     *
     * @param nameOfVegetable name of vegetable
     * @return the current price of a certain vegetable
     */
    public int getCurrentPrice(String nameOfVegetable) {
        int currentPrice = 0;
        try {
            currentPrice = PriceDisplay.getPriceFromString(nameOfVegetable);
        } catch (FarmException e) {
            System.out.println(e.getMessage());
        }
        return currentPrice;
    }

    /**
     * If there was sales, then update the price indicator.
     */
    public void updatePriceIndex() {
        if (this.salesRecord.isEmpty()) {
            return;
        }
        int changeOfIndex;
        for (PriceDisplay display : PriceDisplay.values()) {
            changeOfIndex = calculateChangeOfIndex(display.getItemNames());
            if (changeOfIndex != 0) {
                display.changePriceIndex(changeOfIndex);
            }
        }
        this.salesRecord.clear();
    }

    /**
     * Calculates the change of index (the change of position of price indicator).
     * If integer is positive, the current price tag will be marked higher, negative means going down.
     *
     * @param itemNames name of the pair of vegetables
     * @return the change of index (the change of position of the price indicator)
     */
    public int calculateChangeOfIndex(List<String> itemNames) {
        int count = 0;
        int changeOfIndex = 0;
        for (String name : itemNames) {
            VegetableType typeToCheck = VegetableType.getVegetableTypeFromString(name);
            if (this.salesRecord.containsKey(typeToCheck)) {
                //First vegetable type of the fixed pair is checked, then the second vegetable type is checked.
                if (count == 0) {
                    changeOfIndex = this.salesRecord.get(typeToCheck);
                } else {
                    changeOfIndex -= this.salesRecord.get(typeToCheck);
                }
            }
            count++;
        }
        return changeOfIndex / 2;
    }

    /**
     * Records the sales.
     *
     * @param salesList the list of sales
     */
    public void recordSales(List<VegetableSet> salesList) {
        if (!this.salesRecord.isEmpty()) {
            for (VegetableSet vegetableSet : salesList) {
                VegetableType typeToSearch = vegetableSet.getType();
                if (this.salesRecord.containsKey(typeToSearch)) {
                    this.salesRecord.put(typeToSearch, this.salesRecord.get(typeToSearch) + vegetableSet.getAmount());
                }
            }
        } else {
            for (VegetableSet vegetableSet : salesList) {
                this.salesRecord.put(vegetableSet.getType(), vegetableSet.getAmount());
            }
        }
    }

    /**
     * Calculates gold earned from the sale and print the sale message.
     *
     * @param salesList the list of sales of vegetables
     * @return gold earned from the sale
     */
    public int calculateGoldFromSale(List<VegetableSet> salesList) {
        int sumOfGold = 0;
        int sumOfVegetables = 0;
        for (VegetableSet vegetableSet : salesList) {
            int numberOfVegetableInSet = vegetableSet.getAmount();
            sumOfVegetables += numberOfVegetableInSet;
            sumOfGold += numberOfVegetableInSet * getCurrentPrice(vegetableSet.getName());
        }
        if (sumOfVegetables < 2) {
            System.out.println(String.format(MESSAGE_SINGLE_SALE, sumOfGold));
        } else {
            System.out.println(String.format(MESSAGE_MULTIPLE_SALE, sumOfVegetables, sumOfGold));
        }
        return sumOfGold;
    }

    /**
     * Prints the status of the market, in other words, vegetables' current prices.
     */
    public void printMarket() {
        List<VegetableSet> vegetableSetList = new ArrayList<>();
        for (VegetableType type : VegetableType.values()) {
            int priceOfVegetable = getCurrentPrice(type.getName());
            VegetableSet newSet = new VegetableSet(type, priceOfVegetable);
            vegetableSetList.add(newSet);
        }

        Printer markePrinter = new Printer(vegetableSetList);
        System.out.println(markePrinter.printMarket());
    }

}
