package com.sigmundgranaas.forgero.smithingrework;

import com.sigmundgranaas.forgero.core.Forgero;
import com.sigmundgranaas.forgero.fabric.api.entrypoint.ForgeroPreInitializationEntryPoint;
import com.sigmundgranaas.forgero.smithingrework.block.ModBlocks;
import com.sigmundgranaas.forgero.smithingrework.block.custom.BloomeryBlock;
import com.sigmundgranaas.forgero.smithingrework.block.custom.MoldBlock;
import com.sigmundgranaas.forgero.smithingrework.block.entity.BloomeryBlockEntity;
import com.sigmundgranaas.forgero.smithingrework.item.ModItemGroups;
import com.sigmundgranaas.forgero.smithingrework.item.ModItems;
import com.sigmundgranaas.forgero.smithingrework.screen.BloomeryScreenHandler;
import com.sigmundgranaas.forgero.smithingrework.block.entity.ModBlockEntities;
import com.sigmundgranaas.forgero.smithingrework.block.entity.MoldBlockEntity;
import com.sigmundgranaas.forgero.smithingrework.item.custom.LiquidMetalCrucibleItem;
import com.sigmundgranaas.forgero.smithingrework.recipe.MetalMoldRecipe;
import com.sigmundgranaas.forgero.smithingrework.recipe.MetalSmeltingRecipe;

import com.sigmundgranaas.forgero.smithingrework.recipe.ModRecipes;

import com.sigmundgranaas.forgero.smithingrework.screen.ModScreenHandlers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;


public class ForgeroSmithingInitializer implements ForgeroPreInitializationEntryPoint {
	public static final RegistryKey<ItemGroup> FORGERO_SMITHING_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Forgero.NAMESPACE, "smithing"));

	@Override
	public void onPreInitialization() {


		ModBlockEntities.registerBlockEntities();
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModRecipes.registerRecipes();
		ModScreenHandlers.registerScreenHandlers();

		// ModFluids.registerModFluids();
		// registerFluid();
	}


}

