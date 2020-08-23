package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.command.AbstractBotCommand;
import mi.chel.discord.timebomb.command.CreateCommand;
import mi.chel.discord.timebomb.command.CutCommand;
import mi.chel.discord.timebomb.command.GameboardCommand;
import mi.chel.discord.timebomb.command.HelloCommand;
import mi.chel.discord.timebomb.command.HelpCommand;
import mi.chel.discord.timebomb.command.InfoCommand;
import mi.chel.discord.timebomb.command.JoinCommand;
import mi.chel.discord.timebomb.command.LeaveCommand;
import mi.chel.discord.timebomb.command.PingCommand;
import mi.chel.discord.timebomb.command.RestartCommand;
import mi.chel.discord.timebomb.command.ScoreCommand;
import mi.chel.discord.timebomb.command.SendNudeCommand;
import mi.chel.discord.timebomb.command.StartCommand;
import mi.chel.discord.timebomb.command.StopCommand;
import mi.chel.discord.timebomb.command.TestCommand;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    private static TimeBombBot bot;

    public static TimeBombBot getBot() {
        return Main.bot;
    }

    public static void main(String[] args) throws LoginException {
        String token = System.getenv("TimeBombToken");
        Main.bot = new TimeBombBot();

        bot.addCommand(new CreateCommand(bot));
        bot.addCommand(new CutCommand(bot));
        bot.addCommand(new GameboardCommand(bot));
        bot.addCommand(new HelloCommand(bot));
        bot.addCommand(new InfoCommand(bot));
        bot.addCommand(new JoinCommand(bot));
        bot.addCommand(new LeaveCommand(bot));
        bot.addCommand(new PingCommand(bot));
        bot.addCommand(new RestartCommand(bot));
        bot.addCommand(new ScoreCommand(bot));
        bot.addCommand(new SendNudeCommand(bot));
        bot.addCommand(new StartCommand(bot));
        bot.addCommand(new StopCommand(bot));
        bot.addCommand(new TestCommand(bot));

        AbstractBotCommand helpCommand = new HelpCommand(bot);
        bot.addCommand(helpCommand);

        bot.setJda(JDABuilder.createDefault(token)
                .addEventListeners(new EventListener(bot))
                .setActivity(Activity.playing(String.format("%s%s", AbstractBotCommand.PREFIX, helpCommand.getLabel())))
                .build());
    }
}
