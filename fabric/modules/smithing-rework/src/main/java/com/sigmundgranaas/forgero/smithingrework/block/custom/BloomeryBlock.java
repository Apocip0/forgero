package com.sigmundgranaas.forgero.smithingrework.block.custom;

import com.sigmundgranaas.forgero.core.Forgero;
import com.sigmundgranaas.forgero.smithingrework.block.entity.BloomeryBlockEntity;
import com.sigmundgranaas.forgero.smithingrework.block.entity.ModBlockEntities;
import com.sigmundgranaas.forgero.smithingrework.block.entity.SmithingAnvilBlockEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

public class BloomeryBlock extends AbstractFurnaceBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 13, 16);
	public String type;
	public final boolean emitsParticles;

	public BloomeryBlock(boolean emitsParticles, AbstractBlock.Settings settings) {
		super(settings);
		this.emitsParticles = emitsParticles;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return BloomeryBlock.checkType(world, type, ModBlockEntities.BLOOMERY_BLOCK);
	}

	@Override
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BloomeryBlockEntity) {
			player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
			player.incrementStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!state.get(LIT).booleanValue()) {
			return;
		}
		double d = (double)pos.getX() + 0.5;
		double e = pos.getY();
		double f = (double)pos.getZ() + 0.5;
		if (random.nextDouble() < 0.1) {
			world.playSound(d, e, f, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
		}
		Direction direction = state.get(FACING);
		Direction.Axis axis = direction.getAxis();
		double h = random.nextDouble() * 0.6 - 0.3;
		double p = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52 : h;
		double j = random.nextDouble() * 9.0 / 16.0;
		double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52 : h;
		world.addParticle(ParticleTypes.SMOKE, d + p, e + j, f + k, 0.0, 0.0, 0.0);
		world.addParticle(ParticleTypes.FLAME, d + p, e + j, f + k, 0.0, 0.0, 0.0);
		if (random.nextFloat() < 0.11f) {
			for (int i = 0; i < random.nextInt(2) + 2; ++i) {
				BloomeryBlock.spawnSmokeParticle(world, pos, state.get(BloomeryBlock.LIT), false);
			}
		}
		if (this.emitsParticles && random.nextInt(5) == 0) {
			for (int i = 0; i < random.nextInt(1) + 1; ++i) {
				world.addParticle(ParticleTypes.LAVA, (double)pos.getX() + 0.5, (double)pos.getY() + 1.5, (double)pos.getZ() + 0.5, random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
			}
		}
	}

	public static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
		Random random = world.getRandom();
		DefaultParticleType defaultParticleType = isSignal ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
		world.addImportantParticle(defaultParticleType, true, (double)pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), (double) pos.getY() + 1.5, (double)pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
		if (lotsOfSmoke) {
			world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.5 + random.nextDouble() / 4.0 * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + 0.4, (double)pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.005, 0.0);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BloomeryBlockEntity(pos, state);
	}
}


