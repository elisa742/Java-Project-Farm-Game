package edu.kit.informatik.queensFarm.module;

import edu.kit.informatik.queensFarm.module.game.Player;
import edu.kit.informatik.queensFarm.module.game.Vector2D;
import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class describes a session for interactive command execution.
 * @author uyjad
 * @version 1.0
 */
public class FarmUI {
    private static final String MESSAGE_REQUEST_NUMBER_OF_PLAYERS = "How many players?";
    private static final String COMMAND_END_TURN = "end turn";
    private static final String SEPARATOR_SPACE = " ";
    private static final List<String> WELCOME_PICTURE = List.of(
            "                           _.-^-._    .--.    "
                    , "                        .-'   _   '-. |__|    "
                    , "                       /     |_|     \\|  |    "
                    , "                      /               \\  |    "
                    , "                     /|     _____     |\\ |    "
                    , "                      |    |==|==|    |  |    "
                    , "  |---|---|---|---|---|    |--|--|    |  |    "
                    , "  |---|---|---|---|---|    |==|==|    |  |    "
                    , "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
                    , "^^^^^^^^^^^^^^^ QUEENS FARMING ^^^^^^^^^^^^^^^"
                    , "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
    );
    private static final String REGEX_COMMAND_QUIT = "quit";
    private static final String KEY_COORDINATE_X = "coordinateX";
    private static final String KEY_COORDINATE_Y = "coordinateY";
    private static final String KEY_VEGETABLE_NAME = "vegetableName";
    private static final String KEY_AMOUNT_TO_HARVEST = "amountToHarvest";
    private static final String KEY_SALE_DETAILS = "saleDetails";
    private static final String REGEX_COMMAND_SHOW_BOARD = "show board";
    private static final String REGEX_COMMAND_SHOW_MARKET = "show market";
    private static final String REGEX_COMMAND_SHOW_BARN = "show barn";
    private static final String REGEX_COMMAND_SELL_ALL = "all";
    private static final Pattern COMMAND_PLANT = Pattern.compile("plant (?<" + KEY_COORDINATE_X
                + ">-?\\d+) (?<" + KEY_COORDINATE_Y + ">-?\\d+) (?<" + KEY_VEGETABLE_NAME
                + ">mushroom|carrot|tomato|salad)");
    private static final Pattern COMMAND_HARVEST = Pattern.compile("harvest (?<" + KEY_COORDINATE_X
                + ">-?\\d+) (?<" + KEY_COORDINATE_Y + ">-?\\d+) (?<" + KEY_AMOUNT_TO_HARVEST + ">\\d+)");
    private static final Pattern COMMAND_BUY_LAND = Pattern.compile("buy land (?<" + KEY_COORDINATE_X
                + ">-?\\d+) (?<" + KEY_COORDINATE_Y + ">-?\\d+)");
    private static final Pattern COMMAND_BUY_VEGETABLE = Pattern.compile("buy vegetable (?<" + KEY_VEGETABLE_NAME
                + ">mushroom|carrot|tomato|salad)");
    private static final Pattern COMMAND_SELL = Pattern.compile("sell( (?<" + KEY_SALE_DETAILS + ">\\D+))?");
    private boolean isRunning;
    private final Scanner scanner = new Scanner(System.in);
    private final Farm farm;
    private List<Player> players = new ArrayList<>();
    private int numOfPlayers;
    private int startCapital;
    private int winningCapital;
    private int seed;
    private SetUpStage setUpStage;

    /**
     * Constructs a new session. Initiates the set-up stage: Set the first stage as the current set-up stage,
     * which is the request of the number of total players.
     * @param farm the farm that session is applied on.
     */
    public FarmUI(Farm farm) {
        this.farm = farm;
        this.isRunning = true;
        this.setUpStage = SetUpStage.TOTAL_PLAYERS_REQUEST;
    }

    /**
     * Creates a list of players with individual names.
     * @param names the list of names to be assigned.
     */
    public void createPlayerList(List<String> names) {
        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) {
            playerList.add(new Player(names.get(i)));
        }
        this.players = playerList;
    }

    /**
     * Starts the interactive session by requesting information from user. Then starts the game and executes commands.
     */
    public void interactive() {
        printWelcome();
        System.out.println(MESSAGE_REQUEST_NUMBER_OF_PLAYERS);

        while ((!this.setUpStage.isAllCompleted()) && this.isRunning) {
            String input = scanner.nextLine();
            if (input.equals(REGEX_COMMAND_QUIT)) {
                stop();
                return;
            }
            try {
                this.setUpStage.execute(input, this);
            } catch (FarmException e) {
                System.out.println(e.getMessage());
            }
            if (this.setUpStage.isAllCompleted()) {
                this.farm.initiateGame(this.players, this.startCapital, this.winningCapital,
                        this.numOfPlayers, this.seed);
                this.farm.printTurnWithName();
            }
        }
        //Game starts.
        while (this.isRunning && !this.farm.isGameOver()) {
            String commandInput = scanner.nextLine();
            if (commandInput.equals(REGEX_COMMAND_QUIT)) {
                this.farm.endGameByQuit();
                if (this.farm.isGameOver()) {
                    stop();
                    return;
                }
            }
            try {
                parseInput(commandInput);
            } catch (FarmException e) {
                System.out.println(e.getMessage());
            }
        }
        this.scanner.close();
    }

    /**
     * Parses the string input which will be formed as a command to execute.
     * @param input string input given by user
     * @throws FarmException if we cannot execute the command derived from the string input
     */
    public void parseInput(String input) throws FarmException {
        Matcher plantMatcher = COMMAND_PLANT.matcher(input);
        if (plantMatcher.matches()) {
            int xCoordinate = parseInteger(plantMatcher.group(KEY_COORDINATE_X));
            int yCoordinate = parseInteger(plantMatcher.group(KEY_COORDINATE_Y));
            String vegetableToPlant = plantMatcher.group(KEY_VEGETABLE_NAME);
            this.farm.plant(new Vector2D(xCoordinate, yCoordinate), vegetableToPlant);
            return;
        }

        Matcher harvestMatcher = COMMAND_HARVEST.matcher(input);
        if (harvestMatcher.matches()) {
            int xCoordinate = parseInteger(harvestMatcher.group(KEY_COORDINATE_X));
            int yCoordinate = parseInteger(harvestMatcher.group(KEY_COORDINATE_Y));
            int amountToHarvest = parseInteger(harvestMatcher.group(KEY_AMOUNT_TO_HARVEST));
            this.farm.harvest(new Vector2D(xCoordinate, yCoordinate), amountToHarvest);
            return;
        }

        Matcher buyLandMatcher = COMMAND_BUY_LAND.matcher(input);
        if (buyLandMatcher.matches()) {
            int xCoordinate = parseInteger(buyLandMatcher.group(KEY_COORDINATE_X));
            int yCoordinate = parseInteger(buyLandMatcher.group(KEY_COORDINATE_Y));
            this.farm.buyLand(xCoordinate, yCoordinate);
            return;
        }

        Matcher buyVegetableMatcher = COMMAND_BUY_VEGETABLE.matcher(input);
        if (buyVegetableMatcher.matches()) {
            String vegetableToBuy = buyVegetableMatcher.group(KEY_VEGETABLE_NAME);
            this.farm.buyVegetable(vegetableToBuy);
            return;
        }
        if (input.equals(COMMAND_END_TURN)) {
            this.farm.changeTurn();
            return;
        }

        if (!(handleSellCommand(input) || handleShowCommand(input))) {
            System.out.println(ErrorMessage.ILLEGAL_COMMAND);
        }
    }

    /**
     * Parses the string input into integer.
     * @param inputToParse string input to parse
     * @return integer if parsing is successful, otherwise throw exception
     * @throws FarmException if the string input cannot be parsed into an integer
     */
    public int parseInteger(String inputToParse) throws FarmException {
        int result;
        try {
            result = Integer.parseInt(inputToParse);
        } catch (NumberFormatException e) {
            throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(inputToParse));

        }
        return result;
    }

    /**
     * Checks if the input matches "show board", "show market" and "show barn" these three commands.
     * If yes, return true and execute the command, otherwise return false.
     * @param input line of input given by user
     * @return true if the input matches "show board", "show market" and "show barn", otherwise return false
     */
    public boolean handleShowCommand(String input) {
        if (input.equals(REGEX_COMMAND_SHOW_BARN)) {
            this.farm.shownBarn();
            return true;
        }
        if (input.equals(REGEX_COMMAND_SHOW_BOARD)) {
            this.farm.shownBoard();
            return true;
        }
        if (input.equals(REGEX_COMMAND_SHOW_MARKET)) {
            this.farm.showMarket();
            return true;
        }
        return false;
    }

    /**
     * Checks whether it is valid format of sell command. If yes, execute the command.
     * @param input line of input given by user
     * @return true if it is valid format of sell command, otherwise return false
     */
    public boolean handleSellCommand(String input) {
        Matcher sellMatcher = COMMAND_SELL.matcher(input);
        if (sellMatcher.matches()) {
            String saleDetails = sellMatcher.group(KEY_SALE_DETAILS);
            if (saleDetails == null) {
                this.farm.sellZeroVegetable();
                return true;
            }
            if (saleDetails.matches(REGEX_COMMAND_SELL_ALL)) {
                this.farm.sellAll();
                return true;
            }
            String[] salesArray = saleDetails.split(SEPARATOR_SPACE);
            try {
                this.farm.sellMultiple(salesArray);
            } catch (FarmException e) {
                System.out.println(e.getMessage());
            }
            return true;
        }
        return false;
    }

    /**
     * Updates the set-up stage.
     * @param nextStage the stage to be applied next
     */
    public void changeSetUpStage(SetUpStage nextStage) {
        this.setUpStage = nextStage;
    }

    /**
     * Prints the welcome information with a farm picture made by lines of strings.
     */
    public void printWelcome() {
        for (String sentence : WELCOME_PICTURE) {
            System.out.println(sentence);
        }
    }

    /**
     * Sets the status as "not running", which will stop the interactive session.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Gets the number of total players.
     * @return the number of total players.
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Sets the number of total players.
     * @param numOfPlayers the number of total players.
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Sets the seed value by given input.
     * @param seed the value of seed given by user.
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }

    /**
     * Sets the start capital by given input.
     * @param startCapital start capital
     */
    public void setStartCapital(int startCapital) {
        this.startCapital = startCapital;
    }

    /**
     * Sets winning capital by given input.
     * @param winningCapital winning capital
     */
    public void setWinningCapital(int winningCapital) {
        this.winningCapital = winningCapital;
    }

    /**
     * Gets the scanner.
     * @return scanner
     */
    public Scanner getScanner() {
        return this.scanner;
    }
}
