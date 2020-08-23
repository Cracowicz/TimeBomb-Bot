package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.game.Game;
import mi.chel.discord.timebomb.player.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;

public final class Message {

    private Message() {}

    public static void pong(MessageChannel channel) {
        sendMessage(channel, "Pong");
    }

    public static void hello(MessageChannel channel, User user) {
        sendMessage(channel, String.format("Hello %s", user.getName()));
    }

    public static void sendNude(MessageChannel channel, String nudeLink) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setImage(nudeLink);
        eb.setDescription("T'as cru quoi frere ?!");
        sendMessage(channel, eb.build());
    }

    public static void notImplemented(MessageChannel channel) {
        sendMessage(channel, "Not implemented yet :(");
    }

    public static void gameAlreadyExist(MessageChannel channel) {
        sendMessage(channel, "Game already exist.");
    }

    public static void noGameCreated(MessageChannel channel) {
        sendMessage(channel, "No game created.");
    }

    public static void gameNotStarted(MessageChannel channel) {
        sendMessage(channel, "The game is not started.");
    }

    public static void gameAlreadyStarted(MessageChannel channel) {
        sendMessage(channel, "Game already started.");
    }

    public static void leaveStartedGame(MessageChannel channel) {
        sendMessage(channel, "You can't leave a game already started.");
    }

    public static void joinStartedGame(MessageChannel channel) {
        sendMessage(channel, "You can't join a game already started.");
    }

    public static void playerStartGame(MessageChannel channel, User user) {
        sendMessage(channel, String.format("%s start the game.", user.getAsMention()));
    }

    public static void noGameToStop(MessageChannel channel) {
        sendMessage(channel, "No game to stop.");
    }

    public static void alreadyInGame(MessageChannel channel) {
        sendMessage(channel, "You are already in the game.");
    }

    public static void notInGame(MessageChannel channel) {
        sendMessage(channel, "You are not in the game.");
    }

    public static void gameIsFull(MessageChannel channel) {
        sendMessage(channel, "The game is full.");
    }

    public static void noEnoughPlayers(MessageChannel channel) {
        sendMessage(channel, "Not enough players to launch the game.");
    }

    public static void playerJoinGame(MessageChannel channel, Player player, Game game) {
        sendMessage(channel, String.format("%s have joined the game (%d/%d)",
                player.getName(),
                game.getPlayerCount(),
                game.getMaxPlayer()));
    }

    public static void playerLeaveGame(MessageChannel channel, Player player, Game game) {
        sendMessage(channel, String.format("%s have leaved the game (%d/%d)",
                player.getName(),
                game.getPlayerCount(),
                game.getMaxPlayer()));
    }

    public static void notYourTurn(MessageChannel channel) {
        sendMessage(channel, "This is not your turn !");
    }

    public static void invalidCutId(MessageChannel channel) {
        sendMessage(channel, "You must give a valid ID !");
    }

    public static void gameCreated(MessageChannel channel, Game game) {
        sendMessage(channel, String.format("Game created: %d places remaining", game.getMaxPlayer() - game.getPlayerCount()));
    }

    public static void gameStopped(MessageChannel channel) {
        sendMessage(channel, "Game stopped");
    }

    public static void sendMessage(MessageChannel channel, String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        sendMessage(channel, builder.build());
    }

    public static void sendMessage(MessageChannel channel, EmbedBuilder builder) {
        builder.setColor(new Color(155, 155, 155));
        sendMessage(channel, builder.build());
    }

    public static void sendMessage(MessageChannel channel, MessageEmbed message) {
        channel.sendMessage(message).queue();
    }
}
