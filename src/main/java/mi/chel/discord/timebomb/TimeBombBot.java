package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.command.Command;
import mi.chel.discord.timebomb.command.CommandHandler;
import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.game.GameHandler;
import net.dv8tion.jda.api.JDA;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeBombBot implements GameHandler, CommandHandler {

    private JDA jda;
    private Map<Long, Game> gameMap;
    private Map<String, Command> commandMap;

    public TimeBombBot() {
        this.jda = null;
        this.gameMap = new HashMap<>();
        this.commandMap = new HashMap<>();
    }

    public JDA getJda() {
        return this.jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }

    public Collection<? extends Command> getCommands() {
        return this.commandMap.values();
    }

    @Nullable
    @Override
    public Command getCommand(String name) {
        return this.commandMap.get(name.toLowerCase());
    }

    @Override
    public void addCommand(@Nonnull Command command) {
        String label = command.getLabel();
        if (this.commandMap.containsKey(label)) {
            throw new IllegalStateException(String.format("Duplicated command '%s'", label));
        }
        this.commandMap.put(label, command);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <G extends Game> G getGame(long id) {
        return (G) this.gameMap.get(id);
    }

    @Override
    public void addGame(@Nonnull Game game) {
        Long channelId = game.getId();
        if (this.gameMap.containsKey(channelId)) {
            throw new IllegalStateException("Trying to run game with the same id.");
        }
        this.gameMap.put(channelId, game);
    }

    @Override
    public void removeGame(@Nonnull Long id) {
        if (!this.gameMap.containsKey(id)) {
            throw new IllegalStateException("No created game with this id !");
        }
        this.gameMap.remove(id);
    }
}
