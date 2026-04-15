package net.shirojr;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;
import net.shirojr.init.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LAKMain implements ModInitializer {
	public static final String MOD_ID = "lockkey";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LAKItemDataComponents.initialize();
		LAKKeyItems.initialize();
		LAKBlocks.initialize();
		LAKBlockEntityTypes.initialize();
		LAKBlockStateProperties.initialize();
		LAKCreativeTabs.initialize();
		LAKDataAttachments.initialize();
		LAKCommonEvents.initialize();

		LOGGER.info("We lock in!");
	}

	public static Identifier getId(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}