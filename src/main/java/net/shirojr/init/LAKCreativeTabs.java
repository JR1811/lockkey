package net.shirojr.init;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.shirojr.LAKMain;
import net.shirojr.util.IdentifierHolder;

public interface LAKCreativeTabs {
    IdentifierHolder<CreativeModeTab> TAB = registerCreativeModeTab("lockkey", FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LAKMain.MOD_ID))
            .icon(LAKKeyItems.KEY::getDefaultInstance)
            .displayItems((_, output) -> LAKKeyItems.ALL_ITEMS.forEach(itemIdentifierHolder ->
                    output.accept(itemIdentifierHolder.entry())))
            .build());


    @SuppressWarnings("SameParameterValue")
    private static IdentifierHolder<CreativeModeTab> registerCreativeModeTab(String name, CreativeModeTab tab) {
        Identifier id = LAKMain.getId(name);
        return new IdentifierHolder<>(Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab), id);
    }

    static void initialize() {
        // static initialisation
        CreativeModeTabEvents.modifyOutputEvent(TAB.getResourceKey(Registries.CREATIVE_MODE_TAB)).register(output -> {
            LAKKeyItems.ALL_ITEMS.forEach(itemHolder -> output.accept(itemHolder.entry()));
        });
    }
}
