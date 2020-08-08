package mi.chel.discord.timebomb;

import javax.annotation.Nonnull;
import java.awt.*;

public enum Team {

    BLUE("blue", new Color(25, 121, 169)),
    RED("red", new Color(214, 48, 36));

    private String label;
    private Color color;

    Team(@Nonnull String label, @Nonnull Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return this.label;
    }

    public Color getColor() {
        return this.color;
    }
}
