package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.command.AbstractBotCommand;
import mi.chel.discord.timebomb.command.Command;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class EventListener extends ListenerAdapter {

    private TimeBombBot bot;

    EventListener(TimeBombBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        ChannelType type = channel.getType();
        if (type != ChannelType.TEXT) {
            return;
        }

        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }

        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();
        if (!contentRaw.startsWith(AbstractBotCommand.PREFIX)) {
            return;
        }

        String[] splitContent = contentRaw.substring(AbstractBotCommand.PREFIX.length()).split("[ ]+");
        String[] args = new String[splitContent.length - 1];
        System.arraycopy(splitContent, 1, args, 0, args.length);

        Command command = this.bot.getCommand(splitContent[0]);
        if (command != null) {
            command.onExecute(author, channel, args);
            message.delete().queue();
        }
    }
}
