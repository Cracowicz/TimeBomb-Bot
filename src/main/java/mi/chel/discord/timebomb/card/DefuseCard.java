package mi.chel.discord.timebomb.card;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Player;

import javax.annotation.Nonnull;

public class DefuseCard extends Card {

    private static final String NAME = "defuse";

    private static BombCard instance;

    public DefuseCard() {
        super(NAME, Type.DEFUSE);
    }

    @Override
    public void onCut(@Nonnull Game<?> game, @Nonnull Player player) {
        // TODO
    }
}
