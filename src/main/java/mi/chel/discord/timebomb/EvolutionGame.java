package mi.chel.discord.timebomb;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;

public class EvolutionGame extends Game<Player> {

    public EvolutionGame(@Nonnull TimeBombBot bot, @Nonnull Long channelId, @Nonnull Long ownerId) {
        super(bot, channelId, ownerId);
    }

    @Override
    public Player initPlayer(Long userID) {
        return null;
    }

    @Override
    public BufferedImage gameBoard() {

        return null;
    }
}
