package mi.chel.discord.timebomb.game;

import javax.annotation.Nonnull;

public interface GameHandler {

    <G extends Game> G getGame(long id);

    void addGame(@Nonnull Game game);

    void removeGame(@Nonnull Long id);
}
