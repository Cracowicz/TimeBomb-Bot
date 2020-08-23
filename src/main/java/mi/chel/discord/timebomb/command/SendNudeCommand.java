package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class SendNudeCommand extends AbstractBotCommand {

    private static final String LABEL = "sendnude";
    private static final String DESCRIPTION = "NSFW";
    private static final String NUDE_LINK = "https://i.ytimg.com/vi/cMTAUr3Nm6I/maxresdefault.jpg";

    public SendNudeCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        user.openPrivateChannel().queue(privateChannel -> Message.sendNude(privateChannel, NUDE_LINK));
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return false;
    }
}
