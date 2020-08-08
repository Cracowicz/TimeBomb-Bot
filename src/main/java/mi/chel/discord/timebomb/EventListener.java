package mi.chel.discord.timebomb;

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
        if (type != ChannelType.TEXT && type != ChannelType.PRIVATE) {
            return;
        }

        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }

        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();
        if (!contentRaw.startsWith(Command.PREFIX)) {
            return;
        }

        String[] args = contentRaw.substring(Command.PREFIX.length()).split(" ");
        Command command = this.bot.getCommand(args[0]);
        if (command != null) {
            command.onExecute(author, channel, args);
            if (type != ChannelType.PRIVATE) {
                message.delete().queue();
            }
        }
    }
}
