package mi.chel.discord.timebomb;

import net.dv8tion.jda.api.entities.MessageChannel;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Game<P extends Player> {

    private static final int MIN_PLAYER = 4;
    private static final int MAX_PLAYER = 6;

    private Random rdm = new Random();

    private TimeBombBot bot;
    private Long channelId;
    private State state;
    private int minPlayer;
    private int maxPlayer;
    private Long ownerId;
    private Map<Long, P> playerMap;
    private Long currentPlayerId;

    public Game(@Nonnull TimeBombBot bot, @Nonnull Long channelId, @Nonnull Long ownerId) {
        this(bot, channelId, ownerId, MIN_PLAYER, MAX_PLAYER);
    }

    private Game(@Nonnull TimeBombBot bot, @Nonnull Long channelId, @Nonnull Long ownerId, int minPlayer, int maxPlayer) {
        this.bot = bot;
        this.channelId = channelId;
        this.state = State.WAITING;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.ownerId = ownerId;
        this.playerMap = new HashMap<>();
        this.currentPlayerId = null;
    }

    protected TimeBombBot getBot() {
        return this.bot;
    }

    public MessageChannel getChannel() {
        return this.bot.getJda().getTextChannelById(this.channelId);
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public State getState() {
        return this.state;
    }

    protected void setState(@Nonnull State state) {
        this.state = state;
    }

    public int getMinPlayer() {
        return this.minPlayer;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public Collection<P> getPlayers() {
        return this.playerMap.values();
    }

    public int getPlayerCount() {
        return this.playerMap.size();
    }

    public Long getCurrentPlayerId() {
        return this.currentPlayerId;
    }

    public P getCurrentPlayer() {
        return this.playerMap.get(this.currentPlayerId);
    }

    public void setCurrentPlayerId(Long currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public boolean isPlayer(Long userId) {
        return this.playerMap.containsKey(userId);
    }

    public void join(Long userId) {
        if (this.state != State.WAITING) {
            throw new RuntimeException("New player can only join during waiting time !");
        }
        if (this.getPlayerCount() >= MAX_PLAYER) {
            throw new RuntimeException("The game is full");
        }
        if (isPlayer(userId)) {
            throw new RuntimeException("Already in the game");
        }
        this.playerMap.put(userId, initPlayer(userId));
    }

    public void leave(Long userId) {
        if (this.state == State.PLAYING) {
            throw new RuntimeException("Player can't leaves during the game");
        }
        if (!isPlayer(userId)) {
            throw new RuntimeException("Player not in the game");
        }
        this.playerMap.remove(userId);
    }

    public void start() {
        if (this.state != State.WAITING) {
            throw new RuntimeException("Game already started");
        }
        int playerCount = this.getPlayerCount();
        if (playerCount < MIN_PLAYER) {
            throw new RuntimeException(String.format("Not enought players %d (min: %d)", playerCount, MIN_PLAYER));
        }
        if (playerCount > MAX_PLAYER) {
            throw new RuntimeException(String.format("Too many players %d (max: %d)", playerCount, MAX_PLAYER));
        }
        this.state = State.PLAYING;
        this.currentPlayerId = getRandomPlayerId();
        try {
            this.getChannel().sendFile(ImageUtils.imageToByteArray(gameBoard(), "png"), "Gameboard.png").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cut(int id) {
        // TODO
    }

    protected Long getRandomPlayerId() {
        Long[] userIds = this.playerMap.keySet().toArray(new Long[0]);
        int index = this.rdm.nextInt(userIds.length);
        return userIds[index];
    }

    public abstract P initPlayer(Long userID);

    public abstract BufferedImage gameBoard();

    public enum State {
        WAITING,
        PLAYING,
        ENDING
    }
}
