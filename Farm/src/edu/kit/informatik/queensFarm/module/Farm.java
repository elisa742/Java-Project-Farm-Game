package edu.kit.informatik.queensFarm.module;

import edu.kit.informatik.queensFarm.module.game.GameOver;
import edu.kit.informatik.queensFarm.module.game.Vector2D;
import edu.kit.informatik.queensFarm.module.game.VegetableSet;
import edu.kit.informatik.queensFarm.module.game.VegetableType;
import edu.kit.informatik.queensFarm.module.game.land.ArableLand;
import edu.kit.informatik.queensFarm.module.game.market.Market;
import edu.kit.informatik.queensFarm.module.game.Player;
import edu.kit.informatik.queensFarm.module.game.land.LandManager;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class represents the game queen's Farm.
 * @author uyjad
 * @version 1.0
 */
public class Farm {
    private static final List<String> NAMES = List.of("tomato", "mushroom", "salad", "carrot");
    private static final String MESSAGE_WHOSE_TURN = "It is %s's turn!";
    private static final String MESSAGE_ZERO_ITEM_SOLD = "You have sold 0 vegetables for 0 gold.";
    private int winningCapital;
    private int countInFirstRound = 0;
    private final Market market;
    private List<Player> listOfPlayers;
    private List<ArableLand> availableArableLands;
    private int currentPlayerIndex = 0;
    private int numOfPlayers;
    private int countOfActionsDone = 0;
    private boolean isGameOver;
    private boolean isNotFirstRound;

    /**
     * Constructs an instance of Farm.
     */
    public Farm() {
        this.market = new Market();
        this.isGameOver = false;
        this.isNotFirstRound = false;
    }

    /**
     * Initiates the farm by setting up the player list, available shuffled lands and winning capital.
     * @param players the list of players
     * @param startCapital start capital
     * @param winningCapital winning capital
     * @param numOfPlayers number of players
     * @param seed seed
     */
    public void initiateGame(List<Player> players, int startCapital, int winningCapital, int numOfPlayers, long seed) {
        this.winningCapital = winningCapital;
        this.numOfPlayers = numOfPlayers;
        this.listOfPlayers = players;
        initiatePlayers(startCapital);
        initiateAvailableLands();
        shuffleLand(seed);
    }

    /**
     * Initiates the list of players with the given start capital.
     * @param startCapital start capital
     */
    public void initiatePlayers(int startCapital) {
        for (Player player : this.listOfPlayers) {
            player.setStartCapital(startCapital);
        }
    }

    /**
     * Initiates the list of available lands.
     */
    public void initiateAvailableLands() {
        LandManager landManager = new LandManager(this.numOfPlayers);
        this.availableArableLands = landManager.initiateAvailableLands();
    }

    /**
     * Increases the count of actions.
     * If the player has finished two actions, update market, change turn and reset the counting of actions.
     */
    public void autoChangeTurn() {
        this.countOfActionsDone++;
        if (this.countOfActionsDone == 2) {
            this.countOfActionsDone = 0;
            changeTurn();
        }
    }

    /**
     * Check whether we have passed the first round. If yes, mark "isNotFirstRound" as true.
     */
    public void checkFirstRound() {
        if (!this.isNotFirstRound) {
            if (this.countInFirstRound < this.numOfPlayers - 1) {
                this.countInFirstRound++;
            } else {
                this.isNotFirstRound = true;
            }
        }
    }

    /**
     * Changes player's turn. If it was the last player in the round, check if we have winners.
     */
    public void changeTurn() {
        this.market.updatePriceIndex();
        this.countOfActionsDone = 0;
        if (this.currentPlayerIndex == this.numOfPlayers - 1) {
            this.currentPlayerIndex = 0;
            //situation
            if (!endGame()) {
                printTurnWithName();
            }
        } else {
            this.currentPlayerIndex++;
            printTurnWithName();
        }
        checkFirstRound();
        if (this.isNotFirstRound && !this.isGameOver) {
            getCurrentPlayer().checkRound();
        }
    }

    /**
     * Prints the sentence that shows whose turn it is.
     */
    public void printTurnWithName() {
        System.out.println();
        System.out.println(String.format(MESSAGE_WHOSE_TURN, getCurrentPlayer().getName()));
    }

    /**
     * Shuffles the available lands with the given seed input.
     * @param seed seed
     */
    public void shuffleLand(long seed) {
        Random random = new Random(seed);
        Collections.shuffle(this.availableArableLands, random);
    }

    /**
     * Attempts to end game by checking if the amount of gold owned by player exceeding the winning capital.
     * @return true if at least one player has more than winning capital, otherwise false
     */
    public boolean endGame() {
        GameOver gameOver = new GameOver(this.listOfPlayers, this.winningCapital);
        if (gameOver.printWinnerByWinningCapital()) {
            this.isGameOver = true;
            return true;
        }
        return false;
    }

    /**
     * Ends game triggered by command "quit".
     */
    public void endGameByQuit() {
        GameOver gameOver = new GameOver(this.listOfPlayers, this.winningCapital);
        if (gameOver.printWinnerByMaximumGold()) {
            this.isGameOver = true;
        }
    }

    /**
     * Sells all vegetables in the barn.
     */
    public void sellAll() {
        List<VegetableSet> allVegetables = getCurrentPlayer().getAllVegetables();
        if (allVegetables == null) {
            sellZeroVegetable();
        } else {
            this.market.recordSales(allVegetables);
            getCurrentPlayer().earnGoldFromSale(this.market.calculateGoldFromSale(allVegetables));
            getCurrentPlayer().clearBarn();
            autoChangeTurn();
        }
    }

    /**
     * Sells zero vegetable and prints the message.
     */
    public void sellZeroVegetable() {
        System.out.println(MESSAGE_ZERO_ITEM_SOLD);
        autoChangeTurn();
    }

    /**
     * Converts the given string list of vegetables into list of VegetableSet. Then sells the list of vegetables.
     * @param saleArray array of vegetables to sell
     * @throws FarmException if the name of saleArray is not valid
     */
    public void sellMultiple(String[] saleArray) throws FarmException {
        List<VegetableSet> vegetableSets = new ArrayList<>();
        for (String name : saleArray) {
            if (!NAMES.contains(name)) {
                throw new FarmException(ErrorMessage.ILLEGAL_VEGETABLE_NAME.toString());

            }
        }
        for (VegetableType type : VegetableType.values()) {
            int count = 0;
            for (String itemName : saleArray) {
                if (type.getName().equals(itemName)) {
                    count++;
                }
            }
            if (count != 0) {
                vegetableSets.add(new VegetableSet(type, count));
            }
        }
        sellVegetables(vegetableSets);
    }

    /**
     * Sells the given list of vegetables.
     * @param vegetables list of vegetables to sell
     */
    public void sellVegetables(List<VegetableSet> vegetables) {
        if (getCurrentPlayer().sellMultiple(vegetables)) {
            this.market.recordSales(vegetables);
            getCurrentPlayer().earnGoldFromSale(this.market.calculateGoldFromSale(vegetables));
            autoChangeTurn();
        }
    }

    /**
     * Plants the requested vegetable on a given location.
     * @param vegetableToPlant vegetable to plant
     * @param coordinate coordinate of the land to plant
     */
    public void plant(Vector2D coordinate, String vegetableToPlant) {
        if (getCurrentPlayer().plant(coordinate, vegetableToPlant)) {
            autoChangeTurn();
        }
    }

    /**
     * Harvests the vegetable on a given location.
     * @param amountToRemove amount to harvest
     * @param coordinate coordinate of the land to harvest
     */
    public void harvest(Vector2D coordinate, int amountToRemove) {
        if (getCurrentPlayer().harvest(coordinate, amountToRemove)) {
            autoChangeTurn();
        }
    }

    /**
     * Purchases a vegetable.
     * @param vegetableName name of vegetable to buy
     */
    public void buyVegetable(String vegetableName) {
        int priceOfVegetable = this.market.getCurrentPrice(vegetableName);
        if (getCurrentPlayer().buyVegetable(vegetableName, priceOfVegetable)) {
            autoChangeTurn();
        }
    }

    /**
     * Purchases a new land from the list of available lands.
     * @param xCoordinate coordinate x given by user
     * @param yCoordinate coordinate y given by user
     */
    public void buyLand(int xCoordinate, int yCoordinate) {
        if (this.availableArableLands.isEmpty()) {
            System.out.println(ErrorMessage.LAND_SOLD_OUT.toString());
            return;
        }
        if (this.getCurrentPlayer().buyLand(new Vector2D(xCoordinate, yCoordinate),
                this.availableArableLands.get(0))) {
            this.availableArableLands.remove(0);
            autoChangeTurn();
        }
    }

    /**
     * Prints barn.
     */
    public void shownBarn() {
        System.out.println(getCurrentPlayer().shownBarn());
    }

    /**
     * Prints board.
     */
    public void shownBoard() {
        System.out.println(getCurrentPlayer().shownBoard());
    }

    /**
     * Prints market.
     */
    public void showMarket() {
        this.market.printMarket();
    }

    /**
     * shows whether game is over.
     * @return true if game is over, otherwise false
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Gets the current player.
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return this.listOfPlayers.get(currentPlayerIndex);
    }

}
