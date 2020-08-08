package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Emoji;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.awt.Color;
public class HelpCommand extends Command  {

    private static final String LABEL = "help";
    private static final String DESCRIPTION = "Show this help message.";
    private static final String USAGE = "{label} [all]";
    private static final String GAME_INFO = "Each player will be assigned a secret identity, Agent or Terrorist.\n" +
            "The game will unroll in a maximum of four rounds in which the agents want to defuse the bomb and the terrorists want it to explode.\n" +
            "Each player will be dealt wire cards, which he will see and then will shuffle. Starting with the first player, he will be choosing a secret card and play it. Each card will have an action. Players will follow this way doing actions to try and defuse or explode the bomb.\n" +
            "The game ends if all the success cards are revealed (agents win), the explosion card is revealed (terrorists win), or the 4th round ends without the bomb being defused (terrorists win).";
    private static final String MORE_INFO = "Full rules : https://cdn.1j1ju.com/medias/ff/29/72-time-bomb-rulebook.pdf";

    public HelpCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION, USAGE);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(180, 181, 172));
        embed.setDescription(GAME_INFO);
        embed.appendDescription("\n");
        embed.appendDescription(MORE_INFO);
        embed.appendDescription("\n\n");
        embed.appendDescription(buildCommandHelp(user, channel, args.length == 1 && args[0].toLowerCase().equals("all")));
        channel.sendMessage(embed.build()).queue();
    }

    private String buildCommandHelp(@Nonnull User user, @Nonnull MessageChannel channel, boolean allVisible) {
        String[] helpArray = this.getBot().getCommands().stream()
                .filter(command -> allVisible || command.isVisible(user, channel))
                .map(this::helpMessage)
                .sorted()
                .toArray(String[]::new);

        return String.join("\n", helpArray);
    }

    private String helpMessage(Command command) {
        return String.format("%s %s%s - %s", Emoji.ARROW, PREFIX, command.getUsage(), command.getDescription());
    }
}