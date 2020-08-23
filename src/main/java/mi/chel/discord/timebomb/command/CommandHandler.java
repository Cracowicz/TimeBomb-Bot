package mi.chel.discord.timebomb.command;

import java.util.Collection;

public interface CommandHandler {

    Collection<? extends Command> getCommands();

    Command getCommand(String label);

    void addCommand(Command command);
}
