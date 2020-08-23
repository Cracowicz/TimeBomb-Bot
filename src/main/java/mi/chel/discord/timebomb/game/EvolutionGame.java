package mi.chel.discord.timebomb.game;

import mi.chel.discord.timebomb.Card;
import mi.chel.discord.timebomb.TimeBombBot;
import mi.chel.discord.timebomb.Utils;
import mi.chel.discord.timebomb.player.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EvolutionGame extends AbstractGame {

    private static final int MIN_PLAYER = 4;
    private static final int MAX_PLAYER = 6;

    public EvolutionGame(@Nonnull TimeBombBot bot, @Nonnull Long channelId) {
        super(bot, channelId, MIN_PLAYER, MAX_PLAYER);
    }

    @Override
    public void start() {
        this.dealRoles(this.prepareRoles());
        this.dealCards(this.prepareCards());
        List<Player> players = getPlayers();
        this.setCurrentPlayer(players.get(this.rdm.nextInt(players.size())));
        this.setState(State.PLAYING);
    }

    @Override
    void onCut(Player player, Card card) {

    }

    private int prepareRoles() {
        int red = 2;
        int playerCount = this.getPlayerCount();
        if (playerCount == 4 && Math.random() < red / (double) playerCount) {
            red--;
        }
        return red;
    }

    private List<Card> prepareCards() {
        int playerCount = getPlayerCount();

        // Prepare bomb cards
        List<Card> bombCards = new ArrayList<>();
        bombCards.add(Card.BLUE_BOMB);
        bombCards.add(Card.GREEN_BOMB);
        bombCards.add(Card.ORANGE_BOMB);
        bombCards.add(Card.PINK_BOMB);
        bombCards.add(Card.RED_BOMB);
        bombCards.add(Card.YELLOW_BOMB);
        Collections.shuffle(bombCards);

        // Keep only one bomb's color per player
        bombCards = bombCards.subList(0, playerCount);

        // Remove randomly one bomb per player
        bombCards = Utils.toList(bombCards.stream().collect(Collectors.toMap(card -> card, card -> 5)));
        for (int i = 0; i < playerCount; i++) {
            bombCards.remove(this.rdm.nextInt(bombCards.size()));
        }

        List<Card> cards = new ArrayList<>(bombCards);

        // Prepare defuse cards
        Card defuseCard = Card.DEFUSE;
        for (int i = 0; i < playerCount; i++) {
            cards.add(defuseCard);
        }

        Collections.shuffle(cards);
        return cards;
    }
}
