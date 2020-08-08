package mi.chel.discord.timebomb;

import javax.annotation.Nonnull;

public enum Emoji {

    BOMB(new String(Character.toChars(0x1F4A3))),
    CUT(new String(Character.toChars(0x2702))),
    DEFUSE(new String(Character.toChars(0x2705))),
    BOOM(new String(Character.toChars(0x1F4A5))),
    NEUTRAL(new String(Character.toChars(0x1F610))),
    ARROW(new String(Character.toChars(0x25B6)));

    private String value;

    Emoji(@Nonnull String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
