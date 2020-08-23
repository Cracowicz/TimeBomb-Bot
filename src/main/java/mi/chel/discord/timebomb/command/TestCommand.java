package mi.chel.discord.timebomb.command;

import mi.chel.discord.timebomb.Card;
import mi.chel.discord.timebomb.player.DiscordPlayer;
import mi.chel.discord.timebomb.player.Player;
import mi.chel.discord.timebomb.Team;
import mi.chel.discord.timebomb.TimeBombBot;
import mi.chel.discord.timebomb.game.ClassicGame;
import mi.chel.discord.timebomb.game.Game;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestCommand extends AbstractBotCommand {

    private static final String LABEL = "test";
    private static final String DESCRIPTION = "A test command...";

    public TestCommand(TimeBombBot bot) {
        super(bot, LABEL, DESCRIPTION);
    }

    @Override
    public void onExecute(@Nonnull User user, @Nonnull MessageChannel channel, @Nonnull String[] args) {
        if (args.length < 1) {
            return;
        }
        switch (args[0]) {
            case "auto":
                autoGame(channel, user, args.length < 2 ? 4 : Integer.parseInt(args[1]));
                return;
            case "clean":
                clean(channel);
        }
    }

    private void autoGame(MessageChannel channel, User user, int maxPlayer) {
        TimeBombBot bot = getBot();
        long channelId = channel.getIdLong();
        if (bot.getGame(channelId) != null) {
            bot.removeGame(channelId);
        }
        Game game = new ClassicGame(bot, channelId) {
            @Override
            public void setCurrentPlayer(Player player) {
                super.setCurrentPlayer(player);
                Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                    if (bot.getGame(channelId) != null) {
                        Random rdm = new Random();
                        List<Player> players = getPlayers();
                        players.removeIf(p -> p == player || p.getCardCount() == 0);
                        Player nextPlayer = players.get(rdm.nextInt(players.size()));
                        System.out.printf("%s cut %s%n", player.getName(), nextPlayer.getName());
                        this.cut(nextPlayer, rdm.nextInt(nextPlayer.getCardCount()));
                    }
                }, 3L, TimeUnit.SECONDS);
            }
        };
        String[] names = new String[]{ "Michel", "Abella", "Roger", "Manu", "Lolo", "Maxime"};
        for (int i = 0; i < maxPlayer - 1; i++) {
            game.addPlayer(new FakePlayer(i, names[i]));
        }
        game.addPlayer(new DiscordPlayer(bot, user.getIdLong()));
        game.start();
        bot.addGame(game);
    }

    private void clean(MessageChannel channel) {
        channel.getIterableHistory().queue(history -> {
            history.removeIf(message -> !message.getAuthor().isBot());
            channel.purgeMessages(history);
        });
    }

    @Override
    public boolean isVisible(@Nonnull User user, @Nonnull MessageChannel channel) {
        return false;
    }

    private class FakePlayer implements Player {

        private long id;
        private String name;
        private Team team;
        private List<Card> cards;

        FakePlayer(long id, String name) {
            this.id = id;
            this.name = name;
            this.cards = new ArrayList<>();
        }

        @Override
        public long getId() {
            return this.id;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Team getTeam() {
            return this.team;
        }

        @Override
        public void setTeam(Team team) {
            this.team = team;
        }

        @Override
        public List<Card> getCards() {
            return this.cards;
        }

        @Override
        public Card getCard(int index) {
            return this.cards.get(index);
        }

        @Override
        public int getCardCount() {
            return this.cards.size();
        }

        @Override
        public void addCard(Card card) {
            this.cards.add(card);
        }

        @Override
        public Card removeCard(int index) {
            return this.cards.remove(index);
        }

        @Override
        public void clearCards() {
            this.cards.clear();
        }

        @Override
        public int getVictory() {
            return 0;
        }

        @Override
        public void addVictory() {

        }

        @Override
        public void addDefeat() {

        }

        @Override
        public String getScores() {
            return null;
        }

        @Override
        public void sendPrivateMessage(String message) {
            System.out.println(this.name + " : " + message);
        }

        @Override
        public void sendPrivateImage(byte[] data, String fileName) {
            System.out.println(this.name + " : " + fileName);
        }
    }
}
