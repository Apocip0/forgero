package com.sigmundgranaas.forgero.minecraft.common.toolhandler;

import com.sigmundgranaas.forgero.core.Forgero;
import com.sigmundgranaas.forgero.core.property.active.VeinBreaking;
import com.sigmundgranaas.forgero.core.property.v2.feature.PropertyData;
import com.sigmundgranaas.forgero.minecraft.common.item.tool.DynamicAxeItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class VeinMiningStrategy implements BlockBreakingStrategy {
	private final VeinBreaking handler;

	public VeinMiningStrategy(VeinBreaking handler) {
		this.handler = handler;
	}

	public VeinMiningStrategy(PropertyData data) {
		this.handler = new VeinBreaking((int) data.getValue(), data.getTags().get(0), data.getDescription());
	}

	@Override
	public List<Pair<BlockState, BlockPos>> getAvailableBlocks(BlockView world, BlockPos rootPos, PlayerEntity player) {
		BlockState rootState = world.getBlockState(rootPos);
		Block rootBlock = world.getBlockState(rootPos).getBlock();
		int depth = handler.depth();
		var list = new ArrayList<Pair<BlockState, BlockPos>>();
		var blockSet = new HashSet<BlockPos>();
		var queue = new PriorityQueue<BlockPos>();
		if (BlockBreakingHandler.isBreakableBlock(world, rootPos, player) && depth >= 1) {
			list.add(new Pair<>(rootState, rootPos));
			blockSet.add(rootPos);
			queue.add(rootPos);
			depth -= 1;
		}

		if (rootState.isIn(TagKey.of(Registry.BLOCK_KEY, new Identifier(handler.tag())))) {
			calculateNextBlocks(blockSet, queue, depth, rootBlock, world, player);
		}

		for (BlockPos pos : blockSet) {
			list.add(new Pair<>(world.getBlockState(pos), pos));
		}

		return list;
	}

	private void calculateNextBlocks(@NotNull HashSet<BlockPos> blockSet, @NotNull PriorityQueue<BlockPos> queue, int depth, @NotNull Block rootBlock, @NotNull BlockView world, @NotNull PlayerEntity player) {
		if (depth < 1) {
			return;
		}

		BlockPos pos = queue.peek();
		if (pos == null) {
			return;
		}
		queue.remove();
		var directions = Arrays.asList(Direction.values());
		//Reversing the direction to make the algorithm prioritize blocks in other direction that up and down
		Collections.reverse(directions);
		for (Direction direction : directions) {
			BlockPos newBlock = pos.offset(direction, 1);

			if (!blockSet.contains(newBlock)) {
				// Axes are an exception here, because they should mine blocks of other types included in their tag as well in one go
				if (world.getBlockState(newBlock).getBlock() == rootBlock || (canBeMinedByAxe(rootBlock.getDefaultState()) && canBeMinedByAxe(world.getBlockState(newBlock)))) {
					depth -= 1;
					blockSet.add(newBlock);
					queue.add(newBlock);
				}

				for (Direction direction2 : directions) {
					BlockPos newBlock2 = newBlock.offset(direction2, 1);
					if (world.getBlockState(newBlock2).getBlock() == rootBlock || (canBeMinedByAxe(rootBlock.getDefaultState()) && canBeMinedByAxe(world.getBlockState(newBlock2))) && !blockSet.contains(newBlock2)) {
						depth -= 1;
						blockSet.add(newBlock2);
						queue.add(newBlock2);
					}
				}
				if (depth < 1) {
					return;
				}
			}
		}
		calculateNextBlocks(blockSet, queue, depth, rootBlock, world, player);
	}

	private boolean canBeMinedByAxe(BlockState blockState) {
		return blockState.isIn(TagKey.of(Registry.BLOCK_KEY, new Identifier("forgero", "vein_mining_logs")));
	}
}
