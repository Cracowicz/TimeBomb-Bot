package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class StopCommand extends AbstractBotCommand {

    private static final String LABEL = "stop";
    private static final String DESCRIPTION = "Stop the current game.";

    public StopCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        TimeBombBot bot = this.getBot();
        Game game = bot.getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameToStop(channel);
            return;
        }
        bot.removeGame(game.getId());
        Message.gameStopped(channel);
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
