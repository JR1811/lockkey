package net.shirojr.util.constants;

import net.shirojr.LAKMain;

public interface MiscTranslationKeys {
    String ITEM_GROUP_KEY = "itemGroup." + LAKMain.MOD_ID;
    String ATTACK_LOCKED_KEY = "info.%s.locked.attack".formatted(LAKMain.MOD_ID);
    String INTERACT_LOCKED_KEY = "info.%s.locked.interact".formatted(LAKMain.MOD_ID);
}
