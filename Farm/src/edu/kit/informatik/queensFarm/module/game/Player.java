package edu.kit.informatik.queensFarm.module.game;

import edu.kit.informatik.queensFarm.module.FarmException;
import edu.kit.informatik.queensFarm.module.game.land.ArableLand;
import edu.kit.informatik.queensFarm.module.game.land.Barn;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.List;

/**
 * This class describes a player.
 *
 * @author uyjad
 * @version 1.0
 */
public class Player implements Comparable<Player> {
    private static final String BARN_REPRESENTATION = "Barn";
    private static int idCount = 1;
    private static final String MESSAGE_RESULT_FORMAT = "Player %d (%s): %d";
    private static final String MESSAGE_SUCCESSFUL_BUY_ITEM = "You have bought a %s for %d gold.";
    private final String name;
    private final Board board;
    private final Barn barn;
    private final int id;
    private int totalGold;

    /**
     * Constructs a player.
     *
     * @param name name of player
     */
    public Player(String name) {
        this.name = name;
        this.board = new Board();
        this.barn = new Barn();
        this.id = idCount++;
    }

    /**
     * Checks the growth vegetable and status in the barn.
     */
    public void checkRound() {
        String growthDetails = this.board.checkBoard();
        if (growthDetails != null) {
            System.out.println(growthDetails);
        }

        String barnCheck = this.barn.endRoundCheck();
        if (barnCheck != null) {
            System.out.println(barnCheck);
        }
    }

    /**
     * Sells vegetable.
     * @param saleList list of sales
     * @return false if there is a shortage in the barn, otherwise true
     */
    public boolean sellMultiple(List<VegetableSet> saleList) {
        return this.barn.sell(saleList);
    }

    /**
     * Clears the barn.
     */
    public void clearBarn() {
        this.barn.clearBarn();
    }

    /**
     * Increases the total amount of gold by the given amount.
     * @param amountOfGold amount of gold earned from the sale at market
     */
    public void earnGoldFromSale(int amountOfGold) {
        this.totalGold += amountOfGold;
    }

    /**
     * Attempts to plant a vegetable.
     * @param coordinate coordinates of the land to plant
     * @param nameOfVegetable name of vegetable
     * @return true if planting is executed, otherwise return false
     */
    public boolean plant(Vector2D coordinate, String nameOfVegetable) {
        List<VegetableSet> vegetableSetToRemove = List.of(new VegetableSet(
                VegetableType.getVegetableTypeFromString(nameOfVegetable)));

        if (!this.barn.checkAvailability(vegetableSetToRemove)) {
            return false;
        }
        try {
            this.board.getLandByCoordinate(coordinate).addVegetable(nameOfVegetable);
        } catch (FarmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.barn.executeRemoval(vegetableSetToRemove);
        return true;
    }

    /**
     * Attempts to harvest a specific amount of vegetables in a specific land.
     * @param coordinate coordinate of the land to harvest
     * @param amountToRemove amount to harvest
     * @return true if harvest is executed, otherwise return false
     **/
    public boolean harvest(Vector2D coordinate, int amountToRemove) {
        try {
            this.barn.harvest(this.board.getLandByCoordinate(coordinate).removeVegetable(amountToRemove));
        } catch (FarmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Attempts to buy a vegetable and checks if the player has sufficient gold. If successful, prints purchase message.
     * @param nameOfVegetable name of vegetable to buy
     * @param priceOfVegetable price of vegetable to buy
     * @return true if purchase of vegetable is executed, otherwise return false
     */
    public boolean buyVegetable(String nameOfVegetable, int priceOfVegetable) {
        try {
            hasSufficientMoney(priceOfVegetable);
        } catch (FarmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.totalGold -= priceOfVegetable;
        this.barn.buyVegetable(VegetableType.getVegetableTypeFromString(nameOfVegetable));
        System.out.println(String.format(MESSAGE_SUCCESSFUL_BUY_ITEM, nameOfVegetable, priceOfVegetable));
        return true;
    }

    /**
     * Checks whether the purchase of land is successful, if yes, print success message and reduce the amount of gold.
     *
     * @param coordinate location of the land to buy
     * @param land land to buy
     * @return true if the purchase of land is successful, otherwise false
     */
    public boolean buyLand(Vector2D coordinate, ArableLand land) {
        int priceOfLand = this.board.calculateLandPrice(coordinate);
        try {
            hasSufficientMoney(priceOfLand);
        } catch (FarmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            this.board.buyNewLand(coordinate, land);
        } catch (FarmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.totalGold -= priceOfLand;
        return true;
    }

    /**
     * Gets the status of barn in string format
     * @return the status of barn in string format
     */
    public String shownBarn() {
        Printer barnPrinter = new Printer(this.barn.getAllVegetables());
        if (this.barn.isBarnEmpty()) {
            return barnPrinter.showBarn(this.totalGold, BARN_REPRESENTATION);
        }
        return barnPrinter.showBarn(this.totalGold, this.barn.getBarnDetails());
    }

    /**
     * Gets the status of board in string format
     * @return status of board in string format
     */
    public String shownBoard() {
        return this.board.shownBoard(this.barn.printBarnInBoard());
    }

    /**
     * Checks if the player does not have enough gold.
     *
     * @param amountToConsume amount to consume
     * @throws FarmException if the player does not have enough gold
     */
    public void hasSufficientMoney(int amountToConsume) throws FarmException {
        if (this.totalGold < amountToConsume) {
            throw new FarmException(ErrorMessage.INSUFFICIENT_GOLD.toString());
        }
    }

    /**
     * Gets all vegetables from barn.
     * @return all vegetables from barn
     */
    public List<VegetableSet> getAllVegetables() {
        return this.barn.getAllVegetables();
    }

    /**
     * Sets start capital.
     * @param startCapital start capital
     */
    public void setStartCapital(int startCapital) {
        this.totalGold = startCapital;
    }

    /**
     * Gets name of player
     * @return name of player
     */
    public String getName() {
        return name;
    }

    /**
     * Gets amount of total gold of player
     * @return amount of total gold of player
     */
    public int getTotalGold() {
        return totalGold;
    }

    /**
     * Gets id of player
     * @return id of player
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(MESSAGE_RESULT_FORMAT, this.id, this.name, this.totalGold);
    }

    @Override
    public int compareTo(Player anotherPlayer) {
        return this.id - anotherPlayer.getId();
    }
}
