package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.card.DefuseCard;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class ClassicGame extends Game<Player> {

    public ClassicGame(@Nonnull TimeBombBot bot, @Nonnull Long channelId, @Nonnull Long ownerId) {
        super(bot, channelId, ownerId);
    }

    @Override
    public Player initPlayer(Long userID) {
        return null;
    }

    @Override
    public BufferedImage gameBoard() {
        Collection<Player> players = getPlayers();
        final int W = 200;
        final int H = players.size() * 155;

        // Init canvas
        BufferedImage welcomeImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = welcomeImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Render
        int y = 0;
        for (Player player : players) {
            int cardCount = player.getCardCount();
            for (int x = 0; x < cardCount; x++) {
                g2.drawImage(new DefuseCard().getImage(), x * 75, y * 155, 75, 150, null);
                x++;
            }
            y++;
        }

        // Close the canvas
        g2.dispose();
        return welcomeImage;
    }
}
