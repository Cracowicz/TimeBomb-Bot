import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.*;
import java.util.List;

public class Game {
    private static final int PLAYER_MAX = 8;
    private List<Player> players;
    private boolean isStarted;
    private MessageChannel channel;
    private int nbBlue;
    private int nbRed;
    private Player currentPlayer;
    private List<Integer> cards;
    private int nbDefuse;
    private int nbNeutral;
    private int nbBomb;
    private int round;
    private int cardCutes;
    private int defuseFound;
    private int bombFound;
    private int nbGame = 0;

    public Game(MessageChannel channel) {
        isStarted = false;
        players = new ArrayList<>();
        this.channel = channel;
        round = 0;
        defuseFound = 0;
        cardCutes = 0;
        bombFound = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addPlayers(Collection<Player> players) {
        players.addAll(players);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int nbPlayers() {
        return players.size();
    }

    public int placesRemaining() {
        return PLAYER_MAX - nbPlayers();
    }

    public boolean isInTheGame(User user) {
        for(Player player : players) {
            if(player.getUser() == user)
                return true;
        }
        return false;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start() {
        isStarted = true;
        nbGame++;
        sendMessage("Get ready for the next battle !");
        nbBomb = 1;
        switch (nbPlayers()) {
            case 4:
                nbBlue = 3;
                nbRed = 2;
                nbNeutral = 15;
                nbDefuse = 4;
            break;
            case 5:
                nbBlue = 3;
                nbRed = 2;
                nbNeutral = 19;
                nbDefuse = 5;
            break;
            case 6:
                nbBlue = 4;
                nbRed = 2;
                nbNeutral = 23;
                nbDefuse = 6;
            break;
            case 7:
                nbBlue = 5;
                nbRed = 3;
                nbNeutral = 27;
                nbDefuse = 7;
            break;
            case 8:
                nbBlue = 5;
                nbRed = 3;
                nbNeutral = 31;
                nbDefuse = 8;
            break;
        }
        distributeRole();
        currentPlayer = players.get((int) (Math.random() * nbPlayers()));
        initializesRound();
    }

    public void restart() {
        players.forEach(Player::clearCards);
        round = 0;
        bombFound = 0;
        defuseFound = 0;
        cardCutes = 0;
        start();
    }

    public void initializesRound() {
        round++;
        cardCutes = 0;
        initializesCards();
        drawCards();
        sendMessage(String.format("========== Round %s ==========", round));
        sendMessageTo(currentPlayer, String.format("You're the first player (%s/%s)", cardCutes+1, nbPlayers()));
        presentGameBoard();
    }

    public void presentGameBoard() {
        String gameBoard = "";
        int i = 0;
        for(Player player : players) {
            if(player != currentPlayer) {
                gameBoard += player.getUser().getAsMention() + ":";
                for(int j = 0; j < player.getNbCards(); j++) {
                    gameBoard += " |" + i + "| ";
                    i++;
                }
                gameBoard += "\n";
            }
        }
        sendMessage(gameBoard);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void distributeRole() {
        players.forEach(player -> {
            if(Math.random() < 0.5) {
                if(nbBlue > 0) {
                    player.setTeam(Teams.BLUE);
                    nbBlue--;
                } else {
                    player.setTeam(Teams.RED);
                    nbRed--;
                }
            } else {
                if(nbRed > 0) {
                    player.setTeam(Teams.RED);
                    nbRed--;
                } else {
                    player.setTeam(Teams.BLUE);
                    nbBlue--;
                }
            }
            player.giveHisTeam();
        });
    }

    public void initializesCards() {
        cards = new ArrayList<>();
        for(int i = 0; i < nbNeutral; i++)
            cards.add(Cards.NEUTRAL);
        for(int i = 0; i < nbDefuse; i++)
            cards.add(Cards.DEFUSE);
        for(int i = 0; i < nbBomb; i++)
            cards.add(Cards.BOMB);
        Collections.shuffle(cards);
    }

    public void drawCards() {
        int i = 0;
        for(Player player : players) {
            player.clearCards();
            int j = 0;
            while(j < 6-round) {
                player.addCard(cards.get(i));
                i++;
                j++;
            }
            player.giveHisCards(round);
        }
    }

    public void cut(int id) {
        Player cutter = currentPlayer;
        int i = 0;
        int idToCut = 0;
        for(Player player : players) {
            if(player != currentPlayer) {
                int j = 0;
                for(Integer card : player.getCards()) {
                    if(i == id) {
                        currentPlayer = player;
                        idToCut = j;
                    }
                    j++;
                    i++;
                }
            }
        }

        int cardCuted = currentPlayer.cut(idToCut);
        cardCutes++;
        nextTurn(cutter, cardCuted);
    }

    private void nextTurn(Player cutter, int cardCuted) {
        switch (cardCuted) {
            case Cards.DEFUSE:
                nbDefuse--;
                defuseFound++;
                sendFile(Images.DEFUSE);
                sendMessage(String.format("%s %s %s", cutter.getUser().getAsMention(), Emojis.CUT, currentPlayer.getUser().getAsMention()));
                sendMessage(String.format(Emojis.DEFUSE + " Defuse found !! %d remaining ", nbDefuse));
            break;
            case Cards.NEUTRAL:
                sendFile(Images.NEUTRAL);
                sendMessage(String.format("%s %s %s", cutter.getUser().getAsMention(), Emojis.CUT, currentPlayer.getUser().getAsMention()));
                sendMessage("Neutral found " + Emojis.NEUTRAL);
                nbNeutral--;
            break;
            case Cards.BOMB:
                sendMessage(String.format("%s %s %s", cutter.getUser().getAsMention(), Emojis.CUT, currentPlayer.getUser().getAsMention()));
                sendMessage(Emojis.BOOM + " BOOOOOM " + Emojis.BOOM);
                bombFound++;
            break;
        }

        if(defuseFound == nbPlayers()) {
            blueWin();
        } else if((cardCutes == nbPlayers() && round == 4) || bombFound == nbBomb) {
            redWin();
        } else if(cardCutes == nbPlayers()) {
            initializesRound();
        } else {
            sendMessage(String.format("%s it's your turn (%d/%d)", currentPlayer.getUser().getAsMention(), cardCutes+1, nbPlayers()));
            presentGameBoard();
        }
    }

    private void redWin() {
        sendMessage(Images.BOMB[(int) (Math.random() * Images.BOMB.length)]);
        sendMessage("===== Red Team WIN =====");
        StringBuilder winner = new StringBuilder();
        for(Player player : players) {
            if(player.getTeam() == Teams.RED) {
                winner.append(player.getUser().getAsMention()).append(" ");
                player.addVictory();
            } else {
                player.addDefeat();
            }
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Teams.RED_COLOR);
        eb.setDescription("Good game : " + winner);
        sendMessage(eb.build());
        endTheGame();
    }

    private void blueWin() {
        sendMessage("===== Blue Team WIN =====");
        StringBuilder winner = new StringBuilder();
        for(Player player : players) {
            if(player.getTeam() == Teams.BLUE) {
                winner.append(player.getUser().getAsMention()).append(" ");
                player.addVictory();
            } else {
                player.addDefeat();
            }
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Teams.BLUE_COLOR);
        eb.setDescription("Good game : " + winner);
        sendMessage(eb.build());
        endTheGame();
    }

    public String scores() {
        StringBuilder score = new StringBuilder("====== SCORE ======\n");
        List<Player> p = players;
        Comparator<Player> comparator = (x, y) -> Integer.compare(y.getVictory(), x.getVictory());
        p.sort(comparator);

        for(Player player : p) {
            score.append(player.getScores()).append("\n");
        }
        return score.toString();
    }

    public void endTheGame() {
        isStarted = false;
    }

    public int getNbGame() {
        return nbGame;
    }

    public int getRound() {
        return round;
    }

    public void leave(User user) {
        int id = 0;
        int i = 0;
        for(Player player : players) {
            if(player.getUser() == user)
                id = i;
            i++;
        }
        players.remove(id);
    }

    public int maxId() {
        int idMax = 0;
        for(Player player : players) {
            if(player != currentPlayer)
                idMax += player.getNbCards();
        }
        return idMax-1;
    }

    public int getDefuseFound() {
        return defuseFound;
    }

    public void sendMessage(String message) {
        channel.sendMessage(message).queue();
    }

    public void sendMessage(MessageEmbed message) {
        channel.sendMessage(message).queue();
    }

    public void sendFile(File image) {
        channel.sendFile(image).queue();
    }

    public void sendMessageTo(Player player, String message) {
        channel.sendMessage(String.format("%s %s", player.getUser().getAsMention(), message)).queue();
    }
}
