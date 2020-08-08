package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class GameboardCommand extends Command {

    private static final String LABEL = "gameboard";
    private static final String DESCRIPTION = "Show the gameboard of the current game.";

    public GameboardCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Game game = this.getBot().getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameCreated(channel);
            return;
        }
        if (game.getState() == Game.State.WAITING) {
            Message.gameNotStarted(channel);
            return;
        }
        Message.notImplemented(channel);
        // TODO game.presentGameBoard();
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
