package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class LeaveCommand extends Command {

    private static final String LABEL = "leave";
    private static final String DESCRIPTION = "Leave the current game.";

    public LeaveCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        Game game = this.getBot().getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameCreated(channel);
            return;
        }
        if (game.getState() != Game.State.WAITING) {
            Message.leaveStartedGame(channel);
            return;
        }
        if (!game.isPlayer(user.getIdLong())) {
            Message.notInGame(channel);
            return;
        }
        game.leave(user.getIdLong());
        Message.playerLeaveGame(channel, user, game);
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        Game game = this.getBot().getGame(channel.getIdLong());
        return game != null && game.getState() != Game.State.PLAYING;
    }
}
