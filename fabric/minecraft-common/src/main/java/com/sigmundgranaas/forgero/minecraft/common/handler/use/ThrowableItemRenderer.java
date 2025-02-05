package com.sigmundgranaas.forgero.minecraft.common.handler.use;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import static net.minecraft.client.render.model.json.ModelTransformationMode.GROUND;
import static net.minecraft.util.math.RotationAxis.*;


public class ThrowableItemRenderer extends EntityRenderer<ThrowableItem> {
	private static final Identifier ARROW = new Identifier("textures/entity/projectiles/arrow.png");
	private final net.minecraft.client.render.item.ItemRenderer renderer;


	public ThrowableItemRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.renderer = context.getItemRenderer();
	}

	@Override
	public void render(ThrowableItem item, float partialTicks, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();

		float interpolatedYaw = MathHelper.lerp(partialTicks, item.prevYaw, item.getYaw()) - 90.0F;
		float interpolatedPitch = MathHelper.lerp(partialTicks, item.prevPitch, item.getPitch());

		matrixStack.multiply(POSITIVE_Y.rotation(interpolatedYaw));
		matrixStack.multiply(POSITIVE_Z.rotation(interpolatedPitch));

		matrixStack.scale(1.5F, 1.5F, 1.5F);

		ItemStack pickupItem = item.asItemStack();
		if (!item.isInGround()) {
			float velocity = Math.max((float) item.getVelocity().length(), 1);
			float spinSpeed = 50 * velocity;
			float totalAge = item.age + partialTicks;
			float angle = (totalAge * spinSpeed) % 360;

			switch (item.getSpinType()) {
				case HORIZONTAL -> matrixStack.multiply(NEGATIVE_Y.rotation(angle));
				case VERTICAL -> matrixStack.multiply(NEGATIVE_Z.rotation(angle));
			}
		}
		if (item.getSpinType() == ThrowableItem.SpinType.NONE) {
			matrixStack.multiply(POSITIVE_Z.rotation(45.0F));
		}

		this.renderer.renderItem(pickupItem, GROUND ,i, 0, matrixStack, vertexConsumerProvider, item.getWorld(), 0);
		matrixStack.pop();
		super.render(item, partialTicks, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(ThrowableItem entity) {
		return ARROW;
	}
}
