package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public abstract class Command {

    public static final String PREFIX = "$";
    private static final String LABEL_PLACEHOLDER = "{label}";
    private static final String PREFIX_PLACEHOLDER = "{prefix}";

    private TimeBombBot bot;
    private String label;
    private String description;
    private String usage;

    Command(TimeBombBot bot, String label, String description) {
        this(bot, label, description, LABEL_PLACEHOLDER);
    }

    Command(TimeBombBot bot, String label, String description, String usage) {
        this.bot = bot;
        this.label = label;
        this.description = this.format(description);
        this.usage = this.format(usage);
    }

    public TimeBombBot getBot() {
        return this.bot;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDescription() {
        return this.description;
    }

    String getUsage() {
        return this.usage;
    }

    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return true;
    }

    public String format(String message) {
        return message
                .replace(LABEL_PLACEHOLDER, this.label)
                .replace(PREFIX_PLACEHOLDER, Command.PREFIX);
    }

    public abstract void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args);
}
