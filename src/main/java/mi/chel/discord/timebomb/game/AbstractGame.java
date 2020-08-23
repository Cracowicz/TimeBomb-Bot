package mi.chel.discord.timebomb.game;

import mi.chel.discord.timebomb.Card;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.player.Player;
import mi.chel.discord.timebomb.Team;
import mi.chel.discord.timebomb.TimeBombBot;
import mi.chel.discord.timebomb.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class AbstractGame implements Game {

    Random rdm = new Random();

    private TimeBombBot bot;
    private Long channelId;
    private Game.State state;
    private int minPlayer;
    private int maxPlayer;
    private List<Player> players;
    private List<Card> cards;
    private Player currentPlayer;

    AbstractGame(@Nonnull TimeBombBot bot, @Nonnull Long channelId, int minPlayer, int maxPlayer) {
        this.bot = bot;
        this.channelId = channelId;
        this.state = Game.State.WAITING;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.players = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.currentPlayer = null;
    }

    public TimeBombBot getBot() {
        return this.bot;
    }

    @Override
    public Long getId() {
        return this.channelId;
    }

    @Override
    public Game.State getState() {
        return this.state;
    }

    @Override
    public void setState(@Nonnull Game.State state) {
        this.state = state;
    }

    @Override
    public int getMinPlayer() {
        return this.minPlayer;
    }

    @Override
    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Nullable
    @Override
    public Player getPlayer(long userId) {
        for (Player player : this.players) {
            if (player.getId() == userId) {
                return player;
            }
        }
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public int getPlayerCount() {
        return this.players.size();
    }

    @Override
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
        Message.playerJoinGame(this.getChannel(), player, this);
    }

    @Override
    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    @Override
    public void cut(Player player, int cardId) {
        Card card = player.removeCard(cardId);
        this.cards.remove(card);
        this.onCut(player, card);
    }

    abstract void onCut(Player player, Card card);

    void dealRoles(int red) {
        int i = 0;
        List<Player> players = new ArrayList<>(this.players);
        Collections.shuffle(players);
        for (Player player: players) {
            player.setTeam(i++ < red ? Team.RED : Team.BLUE);
        }
    }

    void dealCards(List<Card> cards) {
        int handSize = cards.size() / this.players.size();
        System.out.println(handSize);
        int i = 0;
        for (Player player : this.players) {
            for (int j = 0; j < handSize; j++) {
                player.addCard(cards.get(i++));
            }
        }
    }

    void showRole(Player player) {
        Team team = Objects.requireNonNull(player.getTeam());
        String message = String.format("You have joined the %s team", team.getLabel());
        player.sendPrivateMessage(message);
    }

    void showCards(Player player) {
        BufferedImage image = Utils.processHandCards(player.getCards());
        player.sendPrivateImage(Utils.toBytes(image, "png"), "hand.png");
    }

    MessageChannel getChannel() {
        return this.bot.getJda().getTextChannelById(this.channelId);
    }

    void sendImage(BufferedImage image) {
        this.getChannel().sendFile(Utils.toBytes(image, "png"), "timebomb_image.png").queue();
    }

    void sendMessage(String message) {
        Message.sendMessage(this.getChannel(), message);
    }

    void sendMessage(EmbedBuilder builder) {
        Message.sendMessage(this.getChannel(), builder);
    }

    void sendMessage(MessageEmbed message) {
        Message.sendMessage(this.getChannel(), message);
    }
}
