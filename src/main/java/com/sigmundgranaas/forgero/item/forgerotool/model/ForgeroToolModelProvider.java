package com.sigmundgranaas.forgero.item.forgerotool.model;

import com.sigmundgranaas.forgero.Forgero;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.jetbrains.annotations.Nullable;

public record ForgeroToolModelProvider(
        ToolModelManager modelManager) implements ModelVariantProvider {

    @Override
    public @Nullable
    UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) {
        if (modelId.getNamespace().equals(Forgero.MOD_NAMESPACE) && modelId.getPath().startsWith("pickaxe") || modelId.getPath().startsWith("shovel")) {
            return new ForgeroToolModel(modelManager);
        }
        return null;
    }
}
