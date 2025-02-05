package com.sigmundgranaas.forgero.fabric.mixins;

import com.sigmundgranaas.forgero.core.property.v2.ComputedAttribute;
import com.sigmundgranaas.forgero.core.type.Type;
import com.sigmundgranaas.forgero.minecraft.common.service.StateService;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerReachMixin {
	@Final
	@Shadow
	private MinecraftClient client;

	@Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
	private void adjustReachBasedOnWeapon(CallbackInfoReturnable<Float> cir) {
		ClientPlayerEntity player = client.player;

		if (player != null) {
			ItemStack mainHandStack = player.getMainHandStack();
			if (!mainHandStack.isEmpty()) {
				StateService.INSTANCE.convert(mainHandStack)
						.filter(state -> state.test(Type.HOLDABLE))
						.map(container -> ComputedAttribute.of(container, "forgero:reach"))
						.map(ComputedAttribute::asFloat)
						.map((reach) -> reach - 4.5f)
						.map(reach -> reach + cir.getReturnValue())
						.ifPresent(cir::setReturnValue);
			}
		}
	}
}
