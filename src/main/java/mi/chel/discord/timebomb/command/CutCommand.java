package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CutCommand extends Command {

    private static final String LABEL = "cut";
    private static final String DESCRIPTION = "Cut the cable with id. (e.g {prefix}{label} 5)";
    private static final String USAGE = "{label} <id>";

    private static Predicate<String> ID_PREDICATE = Pattern.compile("^\\d+$").asPredicate();

    public CutCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION, USAGE);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Game game = this.getBot().getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameCreated(channel);
            return;
        }
        if (game.getState() != Game.State.PLAYING) {
            Message.gameNotStarted(channel);
            return;
        }
        if (user.getIdLong() != game.getCurrentPlayerId()) {
            Message.notYourTurn(channel);
            return;
        }
        if (args.length < 2 || ID_PREDICATE.test(args[0])) {
            Message.invalidCutId(channel, this);
            return;
        }
        int id = Integer.parseInt(args[0]);
        game.cut(id);
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
