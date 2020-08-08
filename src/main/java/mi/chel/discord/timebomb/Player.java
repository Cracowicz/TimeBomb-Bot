package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.card.Card;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private TimeBombBot bot;
    private EmbedBuilder embedBuilder = new EmbedBuilder();
    private Long userId;
    private Team team;
    private List<Card> cards;
    private int victory;
    private int defeat;

    public Player(@Nonnull TimeBombBot bot, @Nonnull Long userId) {
        this.bot = bot;
        this.userId = userId;
        this.team = null;
        this.cards = new ArrayList<>();
        this.victory = this.defeat = 0;
    }

    public Long getUserId() {
        return this.userId;
    }

    public User getUser() {
        return this.bot.getJda().getUserById(this.userId);
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
        this.embedBuilder.setColor(team.getColor());
    }

    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    @Nullable
    public Card getCard(int index) {
        return this.cards.get(index);
    }

    public int getCardCount() {
        return this.cards.size();
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void removeCard(int index) {
        this.cards.remove(index);
    }

    public void clearCards() {
        this.cards.clear();
    }

    public int getVictory() {
        return this.victory;
    }

    public void addVictory() {
        this.victory++;
    }

    public void addDefeat() {
        this.defeat++;
    }

    public String getScores() {
        return String.format("%s : %dV-%dD", this.getUser().getAsMention(), victory, defeat);
    }

    protected EmbedBuilder getEmbedBuilder() {
        return this.embedBuilder;
    }

    protected void sendPrivateMessage(MessageEmbed content) {
        this.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(content).queue());
    }

    protected void sendPrivateImage(byte[] data, String fileName) {
        this.getUser().openPrivateChannel().queue(channel -> channel.sendFile(data, fileName).queue());
    }
}
