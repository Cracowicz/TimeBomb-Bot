package mi.chel.discord.timebomb.card;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Player;

import javax.annotation.Nonnull;

public class NeutralCard extends Card {

    private static final String NAME = "neutral";

    public NeutralCard() {
        super(NAME, Type.NEUTRAL);
    }

    @Override
    public void onCut(@Nonnull Game<?> game, @Nonnull Player player) {
        // TODO
    }
}
