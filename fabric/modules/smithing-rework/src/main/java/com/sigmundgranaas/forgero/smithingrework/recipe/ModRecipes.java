package com.sigmundgranaas.forgero.smithingrework.recipe;

import com.sigmundgranaas.forgero.core.Forgero;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

	public static final Identifier METAL_SMELTING_RECIPE_TYPE_ID = new Identifier(Forgero.NAMESPACE, MetalSmeltingRecipe.ID);
	public static final Identifier METAL_MOLD_RECIPE_TYPE_ID = new Identifier(Forgero.NAMESPACE, MetalMoldRecipe.ID);
    public static void registerRecipes() {
		Registry.register(Registries.RECIPE_TYPE, METAL_SMELTING_RECIPE_TYPE_ID, MetalSmeltingRecipe.TYPE);
		Registry.register(Registries.RECIPE_SERIALIZER, METAL_SMELTING_RECIPE_TYPE_ID, MetalSmeltingRecipe.Serializer.INSTANCE);

		Registry.register(Registries.RECIPE_TYPE, METAL_MOLD_RECIPE_TYPE_ID, MetalMoldRecipe.TYPE);
		Registry.register(Registries.RECIPE_SERIALIZER, METAL_MOLD_RECIPE_TYPE_ID, MetalMoldRecipe.Serializer.INSTANCE);
    }
}
