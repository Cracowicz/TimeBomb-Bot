package mi.chel.discord.timebomb;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Utils {

    private Utils() {}

    public static <T> List<T> toList(Map<T, Integer> map) {
        List<T> list = new ArrayList<>();
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            T value = entry.getKey();
            int amount = entry.getValue();
            for (int i = 0; i < amount; i++) {
                list.add(value);
            }
        }
        return list;
    }

    public static byte[] toBytes(RenderedImage image, String fileExtension) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, fileExtension, os);
            return os.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }

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
