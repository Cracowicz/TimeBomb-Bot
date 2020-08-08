package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.command.Command;
import mi.chel.discord.timebomb.command.JoinCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public final class Message {

    private Message() {}

    public static void pong(MessageChannel channel) {
        channel.sendMessage("Pong").queue();
    }

    public static void hello(MessageChannel channel, User user) {
        channel.sendMessage(String.format("Hello %s", user.getName())).queue();
    }

    public static void sendNude(MessageChannel channel, String nudeLink) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setImage(nudeLink);
        eb.setDescription("T'as cru quoi frere ?!");
        channel.sendMessage(eb.build()).queue();
    }

    public static void notImplemented(MessageChannel channel) {
        channel.sendMessage("Not implemented yet :(").queue();
    }

    public static void gameAlreadyExist(MessageChannel channel) {
        channel.sendMessage("Game already exist.").queue();
    }

    public static void noGameCreated(MessageChannel channel) {
        channel.sendMessage("No game created.").queue();
    }

    public static void gameNotStarted(MessageChannel channel) {
        channel.sendMessage("The game is not started.").queue();
    }

    public static void gameAlreadyStarted(MessageChannel channel) {
        channel.sendMessage("Game already started.").queue();
    }

    public static void leaveStartedGame(MessageChannel channel) {
        channel.sendMessage("You can't leave a game already started.").queue();
    }

    public static void joinStartedGame(MessageChannel channel) {
        channel.sendMessage("You can't join a game already started.").queue();
    }

    public static void playerStartGame(MessageChannel channel, User user) {
        channel.sendMessage(String.format("%s start the game.", user.getAsMention())).queue();
    }

    public static void noGameToStop(MessageChannel channel) {
        channel.sendMessage("No game to stop.").queue();
    }

    public static void onlyOwnerCanStopGame(MessageChannel channel, User owner) {
        channel.sendMessage(String.format("Only %s can stop the game", owner.getAsMention())).queue();
    }

    public static void alreadyInGame(MessageChannel channel) {
        channel.sendMessage("You are already in the game.").queue();
    }

    public static void notInGame(MessageChannel channel) {
        channel.sendMessage("You are not in the game.").queue();
    }

    public static void gameIsFull(MessageChannel channel) {
        channel.sendMessage("The game is full.").queue();
    }

    public static void noEnoughPlayers(MessageChannel channel) {
        channel.sendMessage("Not enough players to launch the game.").queue();
    }

    public static void playerJoinGame(MessageChannel channel, User user, Game game) {
        channel.sendMessage(String.format("%s have joined the game (%d/%d)",
                user.getName(),
                game.getPlayerCount(),
                game.getMaxPlayer())).queue();
    }

    public static void playerLeaveGame(MessageChannel channel, User user, Game game) {
        channel.sendMessage(String.format("%s have leaved the game (%d/%d)",
                user.getName(),
                game.getPlayerCount(),
                game.getMaxPlayer())).queue();
    }

    public static void notYourTurn(MessageChannel channel) {
        channel.sendMessage("This is not your turn !").queue();
    }

    public static void invalidCutId(MessageChannel channel, Command command) {
        channel.sendMessage(command.format("You must give a valid ID ! (e.g {prefix}{label} 2")).queue();
    }

    public static void gameCreated(MessageChannel channel, Game game) {
        channel.sendMessage(String.format("Game created (%d-%d players)\nUse %s%s to play.",
                game.getMinPlayer(),
                game.getMaxPlayer(),
                Command.PREFIX,
                JoinCommand.LABEL)).queue();
    }
}
