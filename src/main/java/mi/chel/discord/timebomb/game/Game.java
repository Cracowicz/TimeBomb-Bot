package mi.chel.discord.timebomb.game;

import mi.chel.discord.timebomb.player.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface Game {

    Long getId();

    State getState();

    void setState(@Nonnull State state);

    int getMinPlayer();

    int getMaxPlayer();

    @Nullable
    Player getPlayer(long userId);

    List<Player> getPlayers();

    int getPlayerCount();

    Player getCurrentPlayer();

    void setCurrentPlayer(Player player);

    void addPlayer(Player player);

    void removePlayer(Player player);

    void start();

    void cut(Player player, int cardIndex);

    enum State {
        WAITING,
        PLAYING,
        ENDING
    }
}
