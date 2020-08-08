package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StopCommand extends Command {

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
        if (game.getOwnerId() != user.getIdLong()) {
            User owner = Objects.requireNonNull(bot.getJda().getUserById(game.getOwnerId()));
            Message.onlyOwnerCanStopGame(channel, owner);
            return;
        }
        bot.removeGame(game.getChannelId());
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
