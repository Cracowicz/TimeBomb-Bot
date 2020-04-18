import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EventListener extends ListenerAdapter {
    private Map<MessageChannel, Game> games;

    public EventListener() {
        games = new HashMap<>();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            return;
        }

        Game game = null;
        if(games.containsKey(event.getChannel())) {
            game = games.get(event.getChannel());
        }

        String message = event.getMessage().getContentRaw();
        User author = event.getAuthor();
        if(event.getChannelType().name().equals("TEXT") && message.startsWith("!")) {
            String messageToSend;
            switch (message.substring(1)) {
                case "ping":
                    event.getChannel().sendMessage("pong!").queue();
                    break;
                case "hello":
                    event.getChannel().sendMessage("Hello " + author.getAsMention()).queue();
                    break;
                case "sendnude":
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setImage("https://i.ytimg.com/vi/cMTAUr3Nm6I/maxresdefault.jpg");
                    eb.setDescription("T'as cru quoi frere ?!");
                    sendPrivateMessage(author, eb.build());
                    //sendPrivateMessage(author, "T'as cru quoi frere ?!");
                    break;
                case "create":
                    if(game != null)
                        messageToSend = "Game is already created !";
                    else {
                        game = new Game(event.getChannel());
                        games.put(event.getChannel(), game);
                        messageToSend = String.format("Game created: %d places remaining", game.placesRemaining());
                    }
                    event.getChannel().sendMessage(messageToSend).queue();
                    break;
                case "join":
                    if(game == null) {
                        event.getChannel().sendMessage(String.format("%s to create a game use !create command", author.getAsMention())).queue();
                        break;
                    }
                    if(game.placesRemaining() <= 0) {
                        event.getChannel().sendMessage(String.format("%s I'm sorry, the game is full :(", author.getAsMention())).queue();
                        break;
                    }
                    if(game.isStarted()) {
                        event.getChannel().sendMessage(String.format("%s I'm sorry, the game has already started ", author.getAsMention())).queue();
                        break;
                    }
                    /*if(game.isInTheGame(author)) {
                        event.getChannel().sendMessage(String.format("%s you're already in the game", author.getAsMention())).queue();
                        break;
                    }*/
                    game.addPlayer(new Player(event.getAuthor()));
                    messageToSend = String.format("%s has joined the game: %d places remaining", author.getAsMention(), game.placesRemaining());
                    event.getChannel().sendMessage(messageToSend).queue();
                    //sendPrivateMessage(author, "\nWait until the game starts\n");
                    break;
                case "leave":
                    if(game == null) {
                        event.getChannel().sendMessage("To create a game use !create").queue();
                        break;
                    }
                    if(!game.isInTheGame(author)) {
                        event.getChannel().sendMessage(String.format("%s you're not in the game ..", author.getAsMention())).queue();
                        break;
                    }
                    if(game.isStarted()) {
                        event.getChannel().sendMessage(String.format("%s you can't leave the game now", author.getAsMention())).queue();
                        break;
                    }
                    game.leave(author);
                    event.getChannel().sendMessage(String.format("%s left the game: %d places remaining", author.getAsMention(), game.placesRemaining())).queue();
                    break;
                case "start":
                    if(game == null) {
                        event.getChannel().sendMessage("To create a game use !create").queue();
                        break;
                    }
                    if(game.nbPlayers() < 4) {
                        event.getChannel().sendMessage("You must be at least 4 players to start the game").queue();
                        break;
                    }
                    if(game.isStarted()) {
                        event.getChannel().sendMessage("The game has already started").queue();
                        break;
                    }
                    if(game.getNbGame() > 0) {
                        event.getChannel().sendMessage("Please use !restart command").queue();
                        break;
                    }
                    game.start();
                    break;
                case "stop":
                    games.remove(event.getChannel());
                    event.getChannel().sendMessage(String.format("%s stop the game", author.getAsMention())).queue();
                    break;
                case "info":
                    if(game == null) {
                        event.getChannel().sendMessage("To create a game use !create").queue();
                        break;
                    }
                    if(game.isStarted()) {
                        event.getChannel().sendMessage(String.format("Round %d !\n%d defusing remaining", game.getRound() ,game.nbPlayers() - game.getDefuseFound())).queue();
                    }
                    break;
                case "gameboard":
                    if(game == null) {
                        event.getChannel().sendMessage("To create a game use !create").queue();
                        break;
                    }
                    if(game.isStarted()) {
                        game.presentGameBoard();
                    }
                    break;
                case "score":
                    if(game == null) {
                        event.getChannel().sendMessage("To create a game use !create").queue();
                        break;
                    }
                    event.getChannel().sendMessage(game.scores()).queue();
                    break;
                case "restart":
                    if(game == null) {
                        event.getChannel().sendMessage("To create a game use !create").queue();
                        break;
                    }
                    if(game.isStarted()) {
                        event.getChannel().sendMessage(String.format("%s you can't restart now, the game is underway !", author.getAsMention())).queue();
                        break;
                    }
                    if(game.nbPlayers() < 4) {
                        event.getChannel().sendMessage("You must be at least 4 players to start the game").queue();
                        break;
                    }
                    game.restart();
                    break;
                case "help":
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(new Color(180, 181, 172));
                    embed.setDescription("Each player will be assigned a secret identity, Agent or Terrorist.\n" +
                            "The game will unroll in a maximum of four rounds in which the agents want to defuse the bomb and the terrorists want it to explode.\n" +
                            "Each player will be dealt wire cards, which he will see and then will shuffle. Starting with the first player, he will be choosing a secret card and play it. Each card will have an action. Players will follow this way doing actions to try and defuse or explode the bomb.\n" +
                            "The game ends if all the success cards are revealed (agents win), the explosion card is revealed (terrorists win), or the 4th round ends without the bomb being defused (terrorists win)."
                            + "\nFull rules : https://cdn.1j1ju.com/medias/ff/29/72-time-bomb-rulebook.pdf"
                            + "\n" + Emojis.ARROW + " !create: create the game"
                            + "\n" + Emojis.ARROW + " !join: join the game"
                            + "\n" + Emojis.ARROW + " !leave: leave the game"
                            + "\n" + Emojis.ARROW + " !start: start the game"
                            + "\n" + Emojis.ARROW + " !restart: restart with same player at the end of a game"
                            + "\n" + Emojis.ARROW + " !cut {id}: cut the cable with id = {id}");
                    event.getChannel().sendMessage(embed.build()).queue();
                default:
            }
            //event.getMessage().delete().queue();
        }

        if(game != null && event.getChannelType().name().equals("TEXT") && game.isStarted() && message.startsWith("!") && message.contains("cut")) {
            if(author != game.getCurrentPlayer().getUser()) {
                event.getChannel().sendMessage(String.format("%s is not your turn !!", author.getAsMention())).queue();
            } else {
                boolean isInvalid = false;
                if(message.substring(5).matches("^[0-9]*$")) {
                    int id = Integer.parseInt(message.substring(5));
                    if(id >= 0 && id <= game.maxId())
                        game.cut(id);
                    else
                        isInvalid = true;
                } else {
                    isInvalid = true;
                }

                if(isInvalid)
                    event.getChannel().sendMessage(String.format("%s please enter a valid id !", author.getAsMention())).queue();
            }
        }
    }

    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

    public void sendPrivateMessage(User user, MessageEmbed content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }
}
