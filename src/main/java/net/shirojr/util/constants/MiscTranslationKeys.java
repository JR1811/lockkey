package net.shirojr.util.constants;

import net.shirojr.LAKMain;

public interface MiscTranslationKeys {
    String ITEM_GROUP = "itemGroup." + LAKMain.MOD_ID;
    String ATTACK_LOCKED = "info.%s.locked.attack".formatted(LAKMain.MOD_ID);
    String INTERACT_LOCKED = "info.%s.locked.interact".formatted(LAKMain.MOD_ID);
    String NO_GROOVES = "info.%s.grooves_missing".formatted(LAKMain.MOD_ID);
    String NOT_LOCKABLE = "info.%s.error.not_lockable".formatted(LAKMain.MOD_ID);

    String TOOLTIP_LOCK_1 = "tooltip.%s.lock.line1";
    String TOOLTIP_KEY_1 = "tooltip.%s.key.line1";
    String TOOLTIP_KEY_2 = "tooltip.%s.key.line2";
}
