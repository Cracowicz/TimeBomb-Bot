package mi.chel.discord.timebomb;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public enum Card {

    BOMB("bomb"),
    DEFUSE("defuse"),
    NEUTRAL("neutral"),

    BLUE_BOMB("blue_bomb"),
    GREEN_BOMB("green_bomb"),
    ORANGE_BOMB("orange_bomb"),
    PINK_BOMB("pink_bomb"),
    RED_BOMB("red_bomb"),
    YELLOW_BOMB("yellow_bomb");

    private static final String FILE_PATH = "src/main/resources";
    private static final String FILE_EXTENSION = "png";

    private String label;
    private BufferedImage image;

    Card(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public BufferedImage getImage() {
        if (this.image == null) {
            try {
                this.image = ImageIO.read(new File(String.format("%s/%s.%s", FILE_PATH, this.label, FILE_EXTENSION)));
            } catch (IOException ex) {
                this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            }
        }
        return this.image;
    }
}
