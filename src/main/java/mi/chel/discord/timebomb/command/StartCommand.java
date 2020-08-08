package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class StartCommand extends Command {

    private static final String LABEL = "start";
    private static final String DESCRIPTION = "Start the current game.";

    public StartCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Game game = this.getBot().getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameCreated(channel);
            return;
        }
        if (game.getState() != Game.State.WAITING) {
            Message.gameAlreadyStarted(channel);
            return;
        }
        if (game.getPlayerCount() <= game.getMinPlayer()) {
            Message.noEnoughPlayers(channel);
            return;
        }
        Message.playerStartGame(channel, user);
        game.start();
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        Game game = this.getBot().getGame(channel.getIdLong());
        return game != null && game.getState() == Game.State.WAITING;
    }
}
