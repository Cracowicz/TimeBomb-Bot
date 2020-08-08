package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Message;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public class RestartCommand extends Command {

    private static final String LABEL = "restart";
    private static final String DESCRIPTION = "Restart with same players and configuration.";

    public RestartCommand(TimeBombBot bot) {
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
//        if(game.isStarted()) {
//            event.getChannel().sendMessage(String.format("%s you can't restart now, the game is underway !", author.getAsMention())).queue();
//            break;
//        }
//        if(game.nbPlayers() < 4) {
//            event.getChannel().sendMessage("You must be at least 4 players to start the game").queue();
//            break;
//        }
//        game.restart();
//        break;
    }

    @Override
    boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        Game game = this.getBot().getGame(channel.getIdLong());
        return game != null && game.getState() == Game.State.ENDING;
    }
}
