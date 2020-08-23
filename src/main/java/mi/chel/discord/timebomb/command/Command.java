package mi.chel.discord.timebomb.command;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public interface Command {

    String getLabel();

    String getDescription();

    String getUsage();

    void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args);

    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel);
}
