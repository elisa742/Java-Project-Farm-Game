package edu.kit.informatik.queensFarm.module.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * This class checks whether the game is over and print winner message out.
 * @author uyjad
 * @version 1.0
 */
public class GameOver {
    private static final String PLAYER_RESULT = "Player %d (%s): %d";
    private static final String SEPARATOR_COMMA = ", ";
    private static final String MESSAGE_SINGLE_WINNER = "%s has won!";
    private static final String MESSAGE_TWO_WINNERS = "%s and %s have won!";
    private static final String MESSAGE_MULTIPLE_WINNERS = " and %s have won!";
    private final List<Player> listOfPlayers;
    private final int winningCapital;

    /**
     * Constructs an instance.
     * @param players players to analyse
     * @param winningCapital winning capital
     */
    public GameOver(List<Player> players, int winningCapital) {
        this.listOfPlayers = players;
        this.winningCapital = winningCapital;
    }

    /**
     * Checks winners by winning capital.
     * @return winners if game is over, otherwise null
     */
    public List<Player> checkWinnerByWinningCapital() {
        List<Player> winners = new ArrayList<>();
        for (Player playerToCheck : this.listOfPlayers) {
            if (playerToCheck.getTotalGold() >= this.winningCapital) {
                winners.add(playerToCheck);
            }
        }
        return winners;
    }

    /**
     * Checks winners by maximum of gold.
     * @return winners with maximum of gold
     */
    public List<Player> checkWinnerByMaximumGold() {
        List<Player> copyOfPlayers = new ArrayList<>();
        for (Player playerToAdd : this.listOfPlayers) {
            copyOfPlayers.add(playerToAdd);
        }
        copyOfPlayers.sort((player, anotherPlayer) -> anotherPlayer.getTotalGold() - player.getTotalGold());
        int maximumGold = copyOfPlayers.get(0).getTotalGold();
        List<Player> winners = copyOfPlayers.stream()
                .filter(player -> player.getTotalGold() == maximumGold)
                .collect(Collectors.toList());

        return winners;
    }

    /**
     * Check whether there exists at least a winner, whose capital exceeds winning capital.
     * If the winner exists, print out the message.
     * @return true if at least one winner exists, otherwise false.
     */
    public boolean printWinnerByWinningCapital() {
        List<Player> winners = checkWinnerByWinningCapital();
        if (!winners.isEmpty()) {
            print(winners);
            return true;
        }
        return false;
    }

    /**
     * Check winners whose capital exceeds winning capital. If no winner, check whose owns the maximum amount.
     * @return true if at least one winner exists, otherwise false.
     */
    public boolean printWinnerByMaximumGold() {
        if (printWinnerByWinningCapital()) {
            return true;
        }
        List<Player> winners = checkWinnerByMaximumGold();
        if (!winners.isEmpty()) {
            print(winners);
            return true;
        }
        return false;
    }

    /**
     * Prints the player result and the winner message.
     * @param winners winners
     */
    public void print(List<Player> winners) {
        printAllPlayerResult(this.listOfPlayers);
        Collections.sort(winners);

        if (winners.size() == 1) {
            System.out.println(String.format(MESSAGE_SINGLE_WINNER, winners.get(0).getName()));
        } else if (winners.size() == 2) {
            System.out.println(String.format(MESSAGE_TWO_WINNERS, winners.get(0).getName(), winners.get(1).getName()));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            StringJoiner stringJoiner = new StringJoiner(SEPARATOR_COMMA);

            for (int i = 0; i < winners.size() - 1; i++) {
                stringJoiner.add(winners.get(i).getName());
            }
            stringBuilder.append(stringJoiner.toString());
            stringBuilder.append(String.format(MESSAGE_MULTIPLE_WINNERS, winners.get(winners.size() - 1).getName()));

            System.out.println(stringBuilder.toString());
        }
    }

    /**
     * Prints the result of all players with name and amount of gold.
     * @param players players to check
     */
    public void printAllPlayerResult(List<Player> players) {
        for (Player player : players) {
            System.out.println(String.format(PLAYER_RESULT, player.getId(), player.getName(), player.getTotalGold()));
        }
    }
}
