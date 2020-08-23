package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class InfoCommand extends AbstractBotCommand {

    private static final String LABEL = "info";
    private static final String DESCRIPTION = "Show information about the current game.";

    public InfoCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Game game = this.getBot().getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameCreated(channel);
            return;
        }
        if (game.getState() != Game.State.PLAYING) {
            Message.gameNotStarted(channel);
            return;
        }
        Message.notImplemented(channel);
        // TODO game.showInfo(user);
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
