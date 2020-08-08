package mi.chel.discord.timebomb.card;

import mi.chel.discord.timebomb.Game;
import mi.chel.discord.timebomb.Player;

import javax.annotation.Nonnull;

public class BombCard extends Card {

    private static final String NAME = "bomb";

    private static BombCard instance;

    public BombCard() {
        super(NAME, Type.BOMB);
    }

    @Override
    public void onCut(@Nonnull Game<?> game, @Nonnull Player player) {
        // TODO
    }

    public static BombCard getInstance() {
        return BombCard.instance == null ? BombCard.instance = new BombCard() : BombCard.instance;
    }
}
