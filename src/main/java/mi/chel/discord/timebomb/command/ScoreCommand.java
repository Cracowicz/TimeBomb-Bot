package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class ScoreCommand extends Command {

    private static final String LABEL = "score";
    private static final String DESCRIPTION = "Show players score.";

    public ScoreCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Message.notImplemented(channel);
        // TODO implementation
//        if(game == null) {
//            event.getChannel().sendMessage("To create a game use !create").queue();
//            break;
//        }
//        event.getChannel().sendMessage(game.scores()).queue();
    }
}
