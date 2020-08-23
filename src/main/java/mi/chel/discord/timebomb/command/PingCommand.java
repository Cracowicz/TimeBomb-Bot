package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class PingCommand extends AbstractBotCommand {

    private static final String LABEL = "ping";
    private static final String DESCRIPTION = "Pong";

    public PingCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Message.pong(channel);
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return false;
    }
}
