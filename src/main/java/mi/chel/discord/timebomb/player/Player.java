package mi.chel.discord.timebomb.player;

import mi.chel.discord.timebomb.Card;
import mi.chel.discord.timebomb.Team;

import javax.annotation.Nullable;
import java.util.List;

public interface Player {

    long getId();

    String getName();

    @Nullable
    Team getTeam();

    void setTeam(Team team);

    List<Card> getCards();

    Card getCard(int index);

    int getCardCount();

    void addCard(Card card);

    Card removeCard(int index);

    void clearCards();

    int getVictory();

    void addVictory();

    void addDefeat();

    String getScores();

    void sendPrivateMessage(String message);

    void sendPrivateImage(byte[] data, String fileName);
}
