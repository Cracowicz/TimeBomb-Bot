package mi.chel.discord.timebomb.game;

import mi.chel.discord.timebomb.Card;
import mi.chel.discord.timebomb.Emoji;
import mi.chel.discord.timebomb.Team;
import mi.chel.discord.timebomb.TimeBombBot;
import mi.chel.discord.timebomb.Utils;
import mi.chel.discord.timebomb.command.CutCommand;
import mi.chel.discord.timebomb.player.Player;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClassicGame extends AbstractGame {

    private static final int MIN_PLAYER = 4;
    private static final int MAX_PLAYER = 8;

    private int cuttedCards;
    private boolean bombFound;
    private int defuseFound;
    private int round;

    public ClassicGame(@Nonnull TimeBombBot bot, @Nonnull Long channelId) {
        super(bot, channelId, MIN_PLAYER, MAX_PLAYER);
        this.cuttedCards = 0;
        this.bombFound = false;
        this.defuseFound = 0;
        this.round = 0;
    }

    @Override
    public void start() {
        List<Player> players = getPlayers();

        int red = this.prepareRoles();
        this.dealRoles(red);
        players.forEach(this::showRole);

        List<Card> cards = this.prepareCards();
        this.setCards(cards);
        this.setState(State.PLAYING);
        Player firstPlayer = players.get(this.rdm.nextInt(players.size()));
        this.setCurrentPlayer(firstPlayer);
        this.nextRound();
    }

    @Override
    void onCut(Player player, Card card) {
        Player currentPlayer = this.getCurrentPlayer();
        int playerCount = this.getPlayerCount();
        this.cuttedCards++;
        sendMessage(String.format("%s %s %s", currentPlayer.getName(), Emoji.CUT, player.getName()));
        this.sendImage(card.getImage());
        switch (card) {
            case NEUTRAL:
                this.sendMessage(String.format("Neutral found %s", Emoji.NEUTRAL));
                break;
            case DEFUSE:
                this.defuseFound++;
                this.sendMessage(String.format("%s Defuse found !! %d remaining ", Emoji.DEFUSE, playerCount - this.defuseFound));
                break;
            case BOMB:
                this.bombFound = true;
                this.sendMessage(String.format("%1$s BOOOOOM %1$s", Emoji.BOMB));
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown card '%s'", card));
        }

        if (this.defuseFound == playerCount) {
            this.win(Team.BLUE);
        } else if (this.bombFound || this.cuttedCards == playerCount && this.round == 4) {
            this.win(Team.RED);
        } else if (this.cuttedCards == playerCount) {
            this.setCurrentPlayer(player);
            this.nextRound();
        } else {
            this.setCurrentPlayer(player);
            sendMessage(this.gameBoard());
        }
    }

    private EmbedBuilder gameBoard() {
        EmbedBuilder builder = new EmbedBuilder();
        StringBuilder sb = new StringBuilder();
        Player currentPlayer = this.getCurrentPlayer();
        for (Player player : this.getPlayers()) {
            sb.append(player.getName()).append(" : ");
            int cardCount = player.getCardCount();
            for (int i = 0; i < cardCount; i++) {
                sb.append(" |").append(i).append("| ");
            }
            if (player == currentPlayer) {
                sb.append(Emoji.CUT);
            }
            sb.append('\n');
        }
        builder.setDescription(sb.toString());
        builder.setFooter(String.format("Use: %s",
                Objects.requireNonNull(this.getBot().getCommand(CutCommand.LABEL)).getUsage())
        );
        return builder;
    }

    private int prepareRoles() {
        int playerCount = this.getPlayerCount();
        int red = playerCount < 7 ? 2 : 3;
        if ((playerCount == 4 || playerCount == 7) && Math.random() < red / (double) playerCount) {
            red--;
        }
        return red;
    }

    private List<Card> prepareCards() {
        Map<Card, Integer> cardMap = new HashMap<>();
        cardMap.put(Card.BOMB, 1);
        int playerCount = this.getPlayerCount();
        switch (playerCount) {
            case 4:
                cardMap.put(Card.NEUTRAL, 15);
                cardMap.put(Card.DEFUSE, 4);
                break;
            case 5:
                cardMap.put(Card.NEUTRAL, 19);
                cardMap.put(Card.DEFUSE, 5);
                break;
            case 6:
                cardMap.put(Card.NEUTRAL, 23);
                cardMap.put(Card.DEFUSE, 6);
                break;
            case 7:
                cardMap.put(Card.NEUTRAL, 27);
                cardMap.put(Card.DEFUSE, 7);
                break;
            case 8:
                cardMap.put(Card.NEUTRAL, 31);
                cardMap.put(Card.DEFUSE, 8);
                break;
        }
        List<Card> cards = Utils.toList(cardMap);
        Collections.shuffle(cards);
        return cards;
    }

    private void nextRound() {
        this.round++;
        this.cuttedCards = 0;
        List<Player> players = this.getPlayers();
        players.forEach(Player::clearCards);
        this.dealCards(this.getCards());
        players.forEach(this::showCards);
        sendMessage(new EmbedBuilder().setTitle(String.format("Round %d", this.round)));
        this.sendMessage(this.gameBoard());
    }

    private void win(Team team) {
        StringBuilder winner = new StringBuilder();
        for(Player player : getPlayers()) {
            if(player.getTeam() == team) {
                winner.append(player.getName()).append(" ");
                player.addVictory();
            } else {
                player.addDefeat();
            }
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(team.getColor());
        eb.setTitle(String.format("%s team win", team.getLabel()));
        eb.setDescription(String.format("Good game : %s", winner));
        sendMessage(eb.build());
    }
}
