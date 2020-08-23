package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.player.DiscordPlayer;
import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class JoinCommand extends AbstractBotCommand {

    public static final String LABEL = "join";
    private static final String DESCRIPTION = "Join the current game.";

    public JoinCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        TimeBombBot bot = this.getBot();
        Game game = bot.getGame(channel.getIdLong());
        if (game == null) {
            Message.noGameCreated(channel);
            return;
        }
        if (game.getState() != Game.State.WAITING) {
            Message.joinStartedGame(channel);
            return;
        }
        long userId = user.getIdLong();
        if (game.getPlayer(userId) != null) {
            Message.alreadyInGame(channel);
            return;
        }
        if (game.getPlayerCount() >= game.getMaxPlayer()) {
            Message.gameIsFull(channel);
            return;
        }
        game.addPlayer(new DiscordPlayer(bot, userId));
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) != null;
    }
}
