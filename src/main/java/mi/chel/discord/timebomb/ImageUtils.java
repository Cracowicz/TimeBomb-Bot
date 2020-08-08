package mi.chel.discord.timebomb;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageUtils {

    private ImageUtils() {}

    public static byte[] imageToByteArray(RenderedImage image, String fileExtension) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, fileExtension, os);
            return os.toByteArray();
        }
    }
}
