package com.sigmundgranaas.forgero.fabric.gametest;

import static com.sigmundgranaas.forgero.fabric.gametest.BlockSelectionTest.createSquare;
import static com.sigmundgranaas.forgero.fabric.gametest.cases.ItemStackCase.assertDamage;
import static com.sigmundgranaas.forgero.testutil.Utils.createTool;
import static net.minecraft.block.Blocks.*;

import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import com.sigmundgranaas.forgero.fabric.gametest.cases.BlockBreakingCase;
import com.sigmundgranaas.forgero.fabric.gametest.helper.WorldBlockHelper;
import com.sigmundgranaas.forgero.testutil.PlayerActionHelper;
import com.sigmundgranaas.forgero.testutil.PlayerFactory;
import com.sigmundgranaas.forgero.testutil.TestPos;
import com.sigmundgranaas.forgero.testutil.TestPosCollection;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class ToolMiningTest {
	// Ore miner heads
	public static String VEIN_MINING_PICKAXE_HEAD = "forgero:netherite-ore_miner_pickaxe_head";
	public static String STONE_VEIN_MINING_PICKAXE_HEAD = "forgero:stone-ore_miner_pickaxe_head";
	public static String IRON_ORE_MINER_PICKAXE_HEAD = "forgero:iron-ore_miner_pickaxe_head";


	// Grave digger heads
	public static String NETHERITE_GRAVE_DIGGER_HEAD = "forgero:netherite-grave_digger_head";


	public static String OAK_HANDLE = "forgero:oak-handle";

	// Pickaxes
	public static String IRON_PICKAXE = "forgero:netherite-pickaxe";
	public static String NETHERITE_PICKAXE = "forgero:netherite-pickaxe";
	public static String STONE_PICKAXE = "forgero:stone-pickaxe";

	// Ore miner pickaxes
	public static Supplier<ItemStack> NETHERITE_ORE_MINER_PICKAXE = () -> createTool(VEIN_MINING_PICKAXE_HEAD, OAK_HANDLE, NETHERITE_PICKAXE);
	public static Supplier<ItemStack> STONE_ORE_MINER_PICKAXE = () -> createTool(STONE_VEIN_MINING_PICKAXE_HEAD, OAK_HANDLE, STONE_PICKAXE);
	public static Supplier<ItemStack> IRON_ORE_MINER_PICKAXE = () -> createTool(IRON_ORE_MINER_PICKAXE_HEAD, OAK_HANDLE, IRON_PICKAXE);

	// Shovels
	public static String NETHERITE_SHOVEL = "forgero:netherite-shovel";

	// Grave digger shovels
	public static Supplier<ItemStack> NETHERITE_GRAVE_DIGGER_SHOVEL = () -> createTool(NETHERITE_GRAVE_DIGGER_HEAD, OAK_HANDLE, NETHERITE_SHOVEL);


	@GameTest(templateName = "forgero:coal_x7", batchId = "tool_mining_test")
	public void test_ore_mining_pickaxe_block_selection_creative(TestContext context) {
		TestPosCollection validationSquare = TestPosCollection.of(relativeValidationSquare, context);
		TestPos center = TestPos.of(RELATIVE_STAR_X7_CENTER, context);
		TestPos stone = TestPos.of(center, new BlockPos(-1, 0, -1));
		TestPos singleCoal = TestPos.of(center, new BlockPos(0, -2, 0));

		ServerPlayerEntity player = PlayerFactory.builder(context)
				.gameMode(GameMode.CREATIVE)
				.stack(NETHERITE_ORE_MINER_PICKAXE)
				.pos(center.absolute())
				.build()
				.createPlayer();

		PlayerActionHelper actionHelper = PlayerActionHelper.of(context, player);
		BlockBreakingCase blockBreakingCase = BlockBreakingCase.of(actionHelper);

		// Break the outcast stone block
		blockBreakingCase.assertBreakBlock(stone);

		// Make sure none of the coal blocks are mined
		blockBreakingCase.assertBlockCount(7, validationSquare, COAL_ORE);

		// Break the cluster of coal blocks using vein mining
		blockBreakingCase.assertBreakSelection(validationSquare, center);

		// Make sure the lowest coal block did not get mined by the vein mining
		blockBreakingCase.assertExists(singleCoal, "Expected the lowest coal block to not be mined by vein mining");

		// Make sure the tool does not take any damage in creative mode
		assertDamage(player.getMainHandStack(), 0);

		context.complete();
	}

	public static BlockPos RELATIVE_STAR_X7_CENTER = new BlockPos(3, 4, 3);
	public static Set<BlockPos> relativeValidationSquare = createSquare(RELATIVE_STAR_X7_CENTER.add(-1, -1, -1), 3, 3, 3);


	@GameTest(templateName = "forgero:coal_x7", batchId = "tool_mining_test")
	public void test_netherite_ore_mining_pickaxe_block_selection_survival(TestContext context) {
		// Netherite pickaxe
		int TICKS_FOR_MINING_STONE = 30;
		int TICKS_FOR_MINING_COAL_CLUSTER = 300;
		survivalOreMiningTest(TICKS_FOR_MINING_STONE, TICKS_FOR_MINING_COAL_CLUSTER, NETHERITE_ORE_MINER_PICKAXE, context);
		context.complete();
	}

	@GameTest(templateName = "forgero:coal_x7", batchId = "tool_mining_test")
	public void test_iron_ore_mining_pickaxe_block_selection_survival(TestContext context) {
		// Iron pickaxe
		int TICKS_FOR_MINING_STONE = 45;
		int TICKS_FOR_MINING_COAL_CLUSTER = 450;
		survivalOreMiningTest(TICKS_FOR_MINING_STONE, TICKS_FOR_MINING_COAL_CLUSTER, IRON_ORE_MINER_PICKAXE, context);

		context.complete();
	}

	@GameTest(templateName = "forgero:coal_x7", batchId = "tool_mining_test")
	public void test_stone_ore_mining_pickaxe_block_selection_survival(TestContext context) {
		// Stone pickaxe
		int TICKS_FOR_MINING_STONE = 60;
		int TICKS_FOR_MINING_COAL_CLUSTER = 650;
		survivalOreMiningTest(TICKS_FOR_MINING_STONE, TICKS_FOR_MINING_COAL_CLUSTER, STONE_ORE_MINER_PICKAXE, context);

		context.complete();
	}

	public static BlockPos RELATIVE_STAR_X21_CENTER = new BlockPos(3, 4, 3);
	public static Set<BlockPos> relativex21ValidationSquare = createSquare(RELATIVE_STAR_X21_CENTER.add(-2, -2, -2), 5, 5, 5);


	@GameTest(templateName = "forgero:coal_x21", batchId = "tool_mining_test")
	public void netherite_grave_digger_head_selection_survival(TestContext context) {
		// Netherite vein mining shovel
		int TICKS_FOR_MINING_PLANK = 250;
		int TICKS_FOR_MINING_DIRT = 30;
		int TICKS_FOR_MINING_DIRT_CLUSTER = 650;

		TestPosCollection validationSquare = TestPosCollection.of(relativex21ValidationSquare, context);
		TestPos center = TestPos.of(RELATIVE_STAR_X21_CENTER, context);
		TestPos plank = TestPos.of(center, new BlockPos(-1, 1, -1));
		TestPos singleDirt = TestPos.of(center, new BlockPos(0, -3, 0));

		ServerPlayerEntity player = PlayerFactory.builder(context)
				.gameMode(GameMode.SURVIVAL)
				.stack(NETHERITE_GRAVE_DIGGER_SHOVEL)
				.pos(center.absolute())
				.build()
				.createPlayer();

		PlayerActionHelper actionHelper = PlayerActionHelper.of(context, player);
		BlockBreakingCase blockBreakingCase = BlockBreakingCase.of(actionHelper);
		WorldBlockHelper worldHelper = new WorldBlockHelper(context);
		worldHelper.replace(ImmutableMap.of(COAL_ORE, DIRT, STONE, OAK_PLANKS));

		// Break the outcast plank block
		blockBreakingCase.assertBreakBlock(plank, TICKS_FOR_MINING_PLANK);

		// Make sure none of the dirt blocks are mined
		blockBreakingCase.assertBlockCount(25, validationSquare, DIRT);

		// Break the cluster of dirt blocks using vein mining
		blockBreakingCase.assertBreakSelection(validationSquare, center, TICKS_FOR_MINING_DIRT_CLUSTER);

		// Make sure the lowest dirt block did not get mined by the vein mining
		blockBreakingCase.assertExists(singleDirt, "Expected the lowest dirt block to not be mined by vein mining");

		// Break the outcast dirt block
		blockBreakingCase.assertBreakBlock(singleDirt, TICKS_FOR_MINING_DIRT);

		// Make sure the tool takes damage
		assertDamage(player.getMainHandStack(), 27);
	}

	private void survivalOreMiningTest(int stoneTicks, int clusterTicks, Supplier<ItemStack> tool, TestContext context) {
		TestPosCollection validationSquare = TestPosCollection.of(relativeValidationSquare, context);
		TestPos center = TestPos.of(RELATIVE_STAR_X7_CENTER, context);
		TestPos stone = TestPos.of(center, new BlockPos(-1, 0, -1));
		TestPos singleCoal = TestPos.of(center, new BlockPos(0, -2, 0));

		ServerPlayerEntity player = PlayerFactory.builder(context)
				.gameMode(GameMode.SURVIVAL)
				.stack(tool)
				.pos(center.absolute())
				.build()
				.createPlayer();

		PlayerActionHelper actionHelper = PlayerActionHelper.of(context, player);
		BlockBreakingCase blockBreakingCase = BlockBreakingCase.of(actionHelper);

		// Break the outcast stone block
		blockBreakingCase.assertBreakBlock(stone, stoneTicks);

		// Make sure none of the coal blocks are mined
		blockBreakingCase.assertBlockCount(7, validationSquare, COAL_ORE);

		// Break the cluster of coal blocks using vein mining
		blockBreakingCase.assertBreakSelection(validationSquare, center, clusterTicks);

		// Make sure the lowest coal block did not get mined by the vein mining
		blockBreakingCase.assertExists(singleCoal, "Expected the lowest coal block to not be mined by vein mining");

		// Make sure it is not possible to mine the coal block in the same amount of ticks as stone
		String message = String.format("Expected block to be stay the same, got air, which means it mined the block in under %s ticks, which it should not do.", stoneTicks);
		blockBreakingCase.assertNotBreakBlock(singleCoal, stoneTicks, message);


		// Make sure the tool takes damage
		assertDamage(player.getMainHandStack(), 8);
	}
}
