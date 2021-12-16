package com.sigmundgranaas.forgero.mixins;

import com.sigmundgranaas.forgero.Forgero;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(NamespaceResourceManager.class)
public abstract class NameSpaceResourceManagerMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    public abstract Resource getResource(Identifier id) throws IOException;

    @Inject(method = "getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;", at = @At("HEAD"), cancellable = true)
    public void getResource(Identifier id, CallbackInfoReturnable<Resource> cir) throws IOException {
        String[] segments = id.getPath().split("/");
        String path = segments[segments.length - 1];

        if (!id.getNamespace().equals(Forgero.MOD_NAMESPACE) || !path.endsWith(".png")) {
            return;
        }
    }
}

