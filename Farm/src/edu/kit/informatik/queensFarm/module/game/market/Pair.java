package edu.kit.informatik.queensFarm.module.game.market;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores one pair price of a pair of vegetables.
 * For example, one "Pair" instance can be current price tags of mushroom and carrot.
 *
 * @author uyjad
 * @version 1.0
 */
public class Pair {
    private final Map<String, Integer> priceTag;

    /**
     * Constructs a pair.
     */
    public Pair() {
        this.priceTag = new HashMap<>();
    }

    /**
     * Stores the price tag of a specific type of vegetable.
     *
     * @param nameOfItem name of vegetable to add
     * @param price price of the vegetable to add
     */
    public void addPriceTag(String nameOfItem, int price) {
        this.priceTag.put(nameOfItem, price);
    }

    /**
     * Gets the price of a specific type of vegetable.
     * @param nameOfItem name of vegetable to add
     * @return price of the requested vegetable
     */
    public int getPrice(String nameOfItem) {
        return this.priceTag.get(nameOfItem);
    }

}
