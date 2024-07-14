package edu.kit.informatik.queensFarm.module;

import edu.kit.informatik.queensFarm.resource.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles user input(Information for initialization of game) based on the current stage of the program.
 * On current stage, the according request will be printed and the user's input will also be processed accordingly.
 *
 * @author uyjad
 * @version 1.0
 */
public enum SetUpStage {
    /**
     * This is the first stage.It handles total number of players.
     * Exception will be thrown, if the user answer is not in the valid format or is less than minimum number
     */
    TOTAL_PLAYERS_REQUEST("Enter the name of player 1:") {
        @Override
        public void execute(String userAnswer, FarmUI farmUI) throws FarmException {
            if (userAnswer.contains(REGEX_SPACE)) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            int numOfPlayers;
            try {
                numOfPlayers = Integer.parseInt(userAnswer);
            } catch (NumberFormatException e) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }

            if (numOfPlayers < MINIMUM_NUMBER_OF_PLAYERS) {
                throw new FarmException(ErrorMessage.ILLEGAL_VALUE_OF_INTEGER.format(MINIMUM_NUMBER_OF_PLAYERS));
            }
            farmUI.setNumOfPlayers(numOfPlayers);
            sendRequest();
            farmUI.changeSetUpStage(PLAYER_NAME_REQUEST);
        }
    },

    /**
     * This is the second stage. It handles names of players.
     * Exception will be thrown, if the user answer is not in the valid format
     */
    PLAYER_NAME_REQUEST("With how much gold should each player start?") {
        @Override
        public void execute(String userAnswer, FarmUI farmUI) throws FarmException {
            int numOfPlayers = farmUI.getNumOfPlayers();
            List<String> names = new ArrayList<>();
            names.add(getPlayerNames(userAnswer));

            if (numOfPlayers > MINIMUM_NUMBER_OF_PLAYERS) {
                System.out.println(MESSAGE_REQUEST_NAME_OF_SECOND_PLAYER);
                int remainingPlayers = numOfPlayers - 1;
                for (int i = 0; i < numOfPlayers - 1; i++) {
                    boolean pass = false;
                    do {
                        String userInput = farmUI.getScanner().nextLine();
                        try {
                            pass = checkPlayerName(userInput);
                            names.add(userInput);
                        } catch (InputException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (!pass);

                    remainingPlayers--;
                    if (remainingPlayers != 0) {
                        System.out.println(String.format(MESSAGE_REQUEST_NAME, i + REQUEST_NAME_INDEX_DIFFERENCE));
                    }
                }
            }
            farmUI.createPlayerList(names);
            sendRequest();
            farmUI.changeSetUpStage(START_CAPITAL_REQUEST);
        }

        /**
         * Gets the player name.
         *
         * @param userAnswer input string by user
         * @throws FarmException if the name does not match the format
         * @return the name if the format of input is valid, otherwise throw exception
         */
        public String getPlayerNames(String userAnswer) throws FarmException {
            if (!userAnswer.matches(REGEX_NAME)) {
                throw new FarmException(ErrorMessage.ILLEGAL_NAME.toString());
            }
            return userAnswer;
        }

        /**
         * Checks whether the input is valid.
         *
         * @param userInput input string by user
         * @throws InputException if the name does not match the format
         * @return true if the format of input is valid, otherwise throw exception
         */
        public boolean checkPlayerName(String userInput) throws InputException {
            if (!userInput.matches(REGEX_NAME)) {
                throw new InputException(ErrorMessage.ILLEGAL_NAME.toString());
            }
            return true;
        }
    },
    /**
     * This is the third stage. User will be asked about the start capital. Exception with error message
     * will be thrown, if the user answer is not in the valid format or less than the required amount.
     */
    START_CAPITAL_REQUEST("With how much gold should a player win?") {
        @Override
        public void execute(String userAnswer, FarmUI farmUI) throws FarmException {
            if (userAnswer.contains(REGEX_SPACE)) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            int startCapital;
            try {
                startCapital = Integer.parseInt(userAnswer);
            } catch (NumberFormatException e) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            if (startCapital < MINIMUM_VALUE_OF_START_CAPITAL) {
                throw new FarmException(ErrorMessage.ILLEGAL_VALUE_OF_INTEGER.format(MINIMUM_VALUE_OF_START_CAPITAL));
            }
            farmUI.setStartCapital(startCapital);
            sendRequest();
            farmUI.changeSetUpStage(WINNING_CAPITAL_REQUEST);
        }
    },

    /**
     * This is the fourth stage. User will be asked about the winning capital. Exception with error message
     * will be thrown, if the user answer is not in the valid format or less than the required amount.
     */
    WINNING_CAPITAL_REQUEST("Please enter the seed used to shuffle the tiles:") {
        @Override
        public void execute(String userAnswer, FarmUI farmUI) throws FarmException {
            if (userAnswer.contains(REGEX_SPACE)) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            int winningCapital;
            try {
                winningCapital = Integer.parseInt(userAnswer);
            } catch (NumberFormatException e) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            if (winningCapital < MINIMUM_VALUE_OF_WINNING_CAPITAL) {
                throw new FarmException(ErrorMessage.ILLEGAL_VALUE_OF_INTEGER.format(MINIMUM_VALUE_OF_WINNING_CAPITAL));
            }
            farmUI.setWinningCapital(winningCapital);
            sendRequest();
            farmUI.changeSetUpStage(SEED_REQUEST);
        }
    },

    /**
     *  This is the last stage. User will be asked about the value of seed. Exception with error message
     *  will be thrown, if the user answer is not in the valid format.
     */
    SEED_REQUEST("") {
        @Override
        public void execute(String userAnswer, FarmUI farmUI) throws FarmException {
            if (userAnswer.contains(REGEX_SPACE)) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            int seed;
            try {
                seed = Integer.parseInt(userAnswer);
            } catch (NumberFormatException e) {
                throw new FarmException(ErrorMessage.ILLEGAL_INTEGER.format(userAnswer));
            }
            farmUI.setSeed(seed);
            setAsAllCompleted();
        }
    };

    private static final int MINIMUM_VALUE_OF_START_CAPITAL = 0;
    private static final int MINIMUM_VALUE_OF_WINNING_CAPITAL = 1;
    private static final int MINIMUM_NUMBER_OF_PLAYERS = 1;
    private static final String REGEX_SPACE = " ";
    private static final String REGEX_NAME = "[A-Za-z]+";
    private static final String MESSAGE_REQUEST_NAME_OF_SECOND_PLAYER = "Enter the name of player 2:";
    private static final String MESSAGE_REQUEST_NAME = "Enter the name of player %d:";
    private static final int REQUEST_NAME_INDEX_DIFFERENCE = 3;
    private final String requestMessage;
    private boolean isAlreadyRequested;
    private boolean isAllCompleted;

    /**
     * Constructs the stage with request message.
     *
     * @param requestMessage request message that will be printed
     */
    SetUpStage(String requestMessage) {
        this.requestMessage = requestMessage;
        this.isAlreadyRequested = false;
        this.isAllCompleted = false;
    }

    /**
     * Sets the status of stage as completely finished.
     */
    public void setAsAllCompleted() {
        this.isAllCompleted = true;
    }

    /**
     * Checks if the whole set-up stage is all completed.
     *
     * @return true if the whole set-up stage is all completed, otherwise false
     */
    public boolean isAllCompleted() {
        return isAllCompleted;
    }

    /**
     * Prints request only once. After printing, the status will be set as "already requested".
     */
    public void sendRequest() {
        if (!this.isAlreadyRequested) {
            System.out.println(this.requestMessage);
            this.isAlreadyRequested = true;
        }
    }

    /**
     * Parses the user answer.
     * If the input is valid, it will be stored in "farmUI". Then all stages but the last stage will move to the
     * next stage. If it is the last stage, after successful parsing it will set the set-up stage as completed.
     *
     * @param userAnswer input from user
     * @param farmUI the user interface of game
     * @throws FarmException if the user input cannot be parsed or does not meet the condition
     */
    public abstract void execute(String userAnswer, FarmUI farmUI) throws FarmException;
}
