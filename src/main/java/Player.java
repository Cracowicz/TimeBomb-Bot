import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private User user;
    private int team;
    private List<Integer> cards;
    private EmbedBuilder eb = new EmbedBuilder();
    private int victory;
    private int defeat;

    public Player(User user) {
        cards = new ArrayList<>();
        this.user = user;
        victory = 0;
        defeat = 0;
    }

    public void addVictory() {
        victory++;
    }

    public void addDefeat() {
        defeat++;
    }

    public int getVictory() {
        return victory;
    }

    public String getScores() {
        return String.format("%s : %dV-%dD", user.getAsMention(), victory, defeat);
    }

    public User getUser() { return user; }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
        if(team == Teams.BLUE)
            eb.setColor(Teams.BLUE_COLOR);
        else
            eb.setColor(Teams.RED_COLOR);
    }

    public void giveHisTeam() {
        String str;
        if(team == Teams.BLUE)
            str = "blue";
        else
            str = "red";
        eb.setDescription(String.format("You have joined the %s team", str));
        sendPrivateMessage(eb.build());
    }

    public void addCard(Integer card) {
        cards.add(card);
    }

    public void clearCards() {
        cards.clear();
    }

    public void giveHisCards(int round) {
        int nbNeutral = 0;
        int nbDefuse = 0;
        int nbBomb = 0;

        for(Integer card : cards) {
            if(card == Cards.NEUTRAL)
                nbNeutral++;
            if(card == Cards.DEFUSE)
                nbDefuse++;
            if(card == Cards.BOMB)
                nbBomb++;
        }

        eb.setDescription(String.format("==== Round %d ====", round));
        sendPrivateMessage(eb.build());

        String id = String.format("%d%d%d", nbNeutral, nbDefuse, nbBomb);
        sendPrivateImage(new File(String.format(Images.CARDS, id)));
    }

    public int cut(int id) {
        int card = cards.get(id);
        cards.remove(id);
        return card;
    }

    public int getNbCards() {
        return cards.size();
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void sendPrivateMessage(String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

    public void sendPrivateMessage(MessageEmbed content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

    public void sendPrivateImage(File image) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendFile(image).queue();
        });
    }
}
