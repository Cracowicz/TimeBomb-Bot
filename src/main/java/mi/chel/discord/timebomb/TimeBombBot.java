package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.command.Command;
import net.dv8tion.jda.api.JDA;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeBombBot {

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

    public Collection<Command> getCommands() {
        return this.commandMap.values();
    }

    @Nullable
    public Command getCommand(String name) {
        return this.commandMap.get(name.toLowerCase());
    }

    public void addCommand(@Nonnull Command command) {
        String label = command.getLabel();
        if (this.commandMap.containsKey(label)) {
            throw new RuntimeException(String.format("Duplicated command '%s'", label));
        }
        this.commandMap.put(label, command);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <G extends Game> G getGame(Long channelId) {
        return (G) this.gameMap.get(channelId);
    }

    public void addGame(@Nonnull Game game) {
        Long channelId = game.getChannelId();
        if (this.gameMap.containsKey(channelId)) {
            throw new RuntimeException("Trying to run game in the same channel.");
        }
        this.gameMap.put(channelId, game);
    }

    public void removeGame(@Nonnull Long channelId) {
        if (!this.gameMap.containsKey(channelId)) {
            throw new RuntimeException("No created game in the current channel !");
        }
        this.gameMap.remove(channelId);
    }
}
