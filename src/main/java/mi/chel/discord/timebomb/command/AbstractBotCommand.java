package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractBotCommand implements Command {

    public static final String PREFIX = "$";
    private static final String LABEL_PLACEHOLDER = "{label}";
    private static final String PREFIX_PLACEHOLDER = "{prefix}";

    private String label;
    private String description;
    private String usage;
    private TimeBombBot bot;

    AbstractBotCommand(TimeBombBot bot, String label, String description) {
        this(bot, label, description, null);
    }

    AbstractBotCommand(TimeBombBot bot, String label, String description, @Nullable String usage) {
        this.label = label;
        this.description = description.replace(LABEL_PLACEHOLDER, this.label).replace(PREFIX_PLACEHOLDER, AbstractBotCommand.PREFIX);
        this.usage = usage == null ? label : String.format("%s%s %s", PREFIX, this.label, usage);
        this.bot = bot;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return true;
    }

    public TimeBombBot getBot() {
        return this.bot;
    }
}
