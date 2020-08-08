package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.ClassicGame;
import mi.chel.discord.timebomb.EvolutionGame;
import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class CreateCommand extends Command {

    private static final String LABEL = "create";
    private static final String DESCRIPTION = "Create a game on the current channel.";
    private static final String USAGE = "{label} [evo]";

    public CreateCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION, USAGE);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        long channelId = channel.getIdLong();
        TimeBombBot bot = this.getBot();
        if (bot.getGame(channelId) != null) {
            Message.gameAlreadyExist(channel);
            return;
        }

        long ownerId = user.getIdLong();
        Game game;
        if (args.length == 1 && args[0].toLowerCase().equals("evo")) {
            game = new EvolutionGame(bot, channelId, ownerId);
        } else {
            game = new ClassicGame(bot, channelId, ownerId);
        }
        game.join(ownerId);
        bot.addGame(game);
        Message.gameCreated(channel, game);
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return this.getBot().getGame(channel.getIdLong()) == null;
    }
}
