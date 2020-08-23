package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.game.ClassicGame;
import mi.chel.discord.timebomb.player.DiscordPlayer;
import mi.chel.discord.timebomb.game.EvolutionGame;
import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class CreateCommand extends AbstractBotCommand {

    private static final String LABEL = "create";
    private static final String DESCRIPTION = "Create a game on the current channel.";
    private static final String EVO_ARG = "evo";
    private static final String USAGE = "{label} [" + EVO_ARG + "]";

    public CreateCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION, USAGE);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        long channelId = channel.getIdLong();
        TimeBombBot bot = this.getBot();
        if (bot.getGame(channelId) != null) {
            user.openPrivateChannel().queue(Message::gameAlreadyExist);
            return;
        }
        Game game;
        if (args.length == 1 && args[0].toLowerCase().equals(EVO_ARG)) {
            game = new EvolutionGame(bot, channelId);
        } else {
            game = new ClassicGame(bot, channelId);
        }
        game.addPlayer(new DiscordPlayer(bot, user.getIdLong()));
        bot.addGame(game);
        Message.gameCreated(channel, game);
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) == null;
    }
}
