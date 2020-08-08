package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class HelloCommand extends Command {

    private static final String LABEL = "hello";
    private static final String DESCRIPTION = "Just say ... hello";

    public HelloCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        user.openPrivateChannel().queue(privateChannel -> {
            Message.hello(privateChannel, user);
        });
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return false;
    }
}
