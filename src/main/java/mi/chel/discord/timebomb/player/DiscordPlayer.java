package mi.chel.discord.timebomb.player;

import mi.chel.discord.timebomb.Card;
import mi.chel.discord.timebomb.Team;
import mi.chel.discord.timebomb.TimeBombBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DiscordPlayer implements Player {

    private TimeBombBot bot;
    private long userId;
    private Team team;
    private List<Card> cards;
    private int victory;
    private int defeat;

    public DiscordPlayer(@Nonnull TimeBombBot bot, long userId) {
        this.bot = bot;
        this.userId = userId;
        this.team = null;
        this.cards = new ArrayList<>();
        this.victory = this.defeat = 0;
    }

    @Override
    public long getId() {
        return this.userId;
    }

    @Override
    public String getName() {
        return this.getUser().getAsMention();
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
        return new ArrayList<>(this.cards);
    }

    @Nullable
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
        return this.victory;
    }

    @Override
    public void addVictory() {
        this.victory++;
    }

    @Override
    public void addDefeat() {
        this.defeat++;
    }

    @Override
    public String getScores() {
        return String.format("%s : %dV-%dD", this.getName(), this.victory, this.defeat);
    }

    @Override
    public void sendPrivateMessage(String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(this.team == null ? null : this.team.getColor());
        builder.setDescription(message);
        this.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }

    @Override
    public void sendPrivateImage(byte[] data, String fileName) {
        this.getUser().openPrivateChannel().queue(channel -> channel.sendFile(data, fileName).queue());
    }

    private User getUser() {
        return this.bot.getJda().getUserById(this.userId);
    }
}
