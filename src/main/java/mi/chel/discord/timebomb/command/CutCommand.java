package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.player.Player;
import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CutCommand extends AbstractBotCommand {

    public static final String LABEL = "cut";
    private static final String DESCRIPTION = "Cut the cable with id. (e.g {prefix}{label} 5)";
    private static final String USAGE = "<@player> <id>";

    private static Pattern MENTION_PATTERN = Pattern.compile("^<@!(\\d{1,18})>$");
    private static Predicate<String> ID_PREDICATE = Pattern.compile("^\\d{1}$").asPredicate();

    public CutCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION, USAGE);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Game game = this.getBot().getGame(channel.getIdLong());
        if (game == null) {
            user.openPrivateChannel().queue(Message::noGameCreated);
            return;
        }
        if (game.getState() != Game.State.PLAYING) {
            user.openPrivateChannel().queue(Message::gameNotStarted);
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null || user.getIdLong() != currentPlayer.getId()) {
            user.openPrivateChannel().queue(Message::notYourTurn);
            return;
        }
        // Arg 0
        Matcher matcher = args.length < 1 ? null : MENTION_PATTERN.matcher(args[0]);
        if (matcher == null || !matcher.find()) {
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You must mention a player !").queue());
            return;
        }
        Player player = game.getPlayer(Long.parseLong(matcher.group(1)));
        if (player == null) {
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(String.format("'%s' is not in the game !", args[0])).queue());
            return;
        }
        if (player == game.getCurrentPlayer()) {
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You can not cut a card in your hand !").queue());
            return;
        }
        // Arg 2
        if (args.length < 3 || !ID_PREDICATE.test(args[1])) {
            user.openPrivateChannel().queue(Message::invalidCutId);
            return;
        }
        int id = Integer.parseInt(args[1]);
        if (id > player.getCardCount()) {
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(String.format("%d is invalid", id)).queue());
            return;
        }
        game.cut(player, id);
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
