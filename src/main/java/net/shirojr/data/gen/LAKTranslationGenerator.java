package net.shirojr.data.gen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.shirojr.LAKMain;
import net.shirojr.init.LAKBlocks;
import net.shirojr.init.LAKKeyItems;
import net.shirojr.util.constants.MiscTranslationKeys;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LAKTranslationGenerator extends FabricLanguageProvider {

    public LAKTranslationGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
        builder.add(MiscTranslationKeys.ITEM_GROUP_KEY, "Lock and Key");
        LAKKeyItems.ALL_ITEMS.forEach(holder -> builder.add(holder.entry(), getReadable(holder.identifier(), true)));
        LAKBlocks.ALL_BLOCKS.forEach(holder -> builder.add(holder.entry(), getReadable(holder.identifier(), true)));

        builder.add(MiscTranslationKeys.ATTACK_LOCKED_KEY, "This is Locked!");
        builder.add(MiscTranslationKeys.INTERACT_LOCKED_KEY, "This is Locked!");

        try {
            Path existingFilePath = packOutput.getModContainer().findPath("assets/%s/lang/en_us.manual.json".formatted(LAKMain.MOD_ID)).orElseThrow();
            builder.add(existingFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static String getReadable(Identifier location, boolean reverse) {
        List<String> split = List.of(location.getPath().split("/"));
        List<String> words = Arrays.asList(split.getLast().split("_"));
        if (reverse) Collections.reverse(words);

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            char capitalized = Character.toUpperCase(word.charAt(0));
            output.append(capitalized).append(word.substring(1));
            if (i < words.size() - 1) {
                output.append(" ");
            }
        }
        return output.toString();
    }
}
