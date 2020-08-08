package mi.chel.discord.timebomb;

import mi.chel.discord.timebomb.card.Card;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public final class CardUtils {

    private CardUtils() {}

    public static BufferedImage processHandCards(List<Card> cards) {
        final int W = cards.size() * 75;
        final int H = 150;

        // Init canvas
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Render
        int i = 0;
        for (Card card : cards) {
            g2.drawImage(card.getImage(), i++ * 75, 0, 75, 150, null);
        }

        // Close the canvas
        g2.dispose();
        return image;
    }
}
