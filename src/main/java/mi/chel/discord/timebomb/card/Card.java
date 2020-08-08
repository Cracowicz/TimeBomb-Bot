package mi.chel.discord.timebomb.card;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Player;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Card {

    private static final String FILE_PATH = "src/main/resources";
    private static final String FILE_EXTENSION = "png";

    private String name;
    private Type type;
    private BufferedImage image;

    Card(@Nonnull String name, @Nonnull Type type) {
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public BufferedImage getImage() {
        // Lazy loading
        if (this.image == null) {
            try {
                this.image = ImageIO.read(new File(String.format("%s/%s.%s", FILE_PATH, name, FILE_EXTENSION)));
            } catch (IOException ex) {
                this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            }
        }
        return this.image;
    }

    public abstract void onCut(@Nonnull Game<?> game, @Nonnull Player player);

    public enum Type {
        DEFUSE,
        NEUTRAL,
        BOMB
    }
}
