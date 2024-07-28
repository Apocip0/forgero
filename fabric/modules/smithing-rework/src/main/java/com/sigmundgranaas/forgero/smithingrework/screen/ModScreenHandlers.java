package com.sigmundgranaas.forgero.smithingrework.screen;

import com.sigmundgranaas.forgero.core.Forgero;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
	public static final ScreenHandlerType<BloomeryScreenHandler> BLOOMERY_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(Forgero.NAMESPACE, "bloomery"), new ScreenHandlerType<>(BloomeryScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    public static void registerScreenHandlers() {
        Forgero.LOGGER.info("Registering Screen Handlers for " + Forgero.NAMESPACE);
    }
}
