package aqua.rpgmod.craft;

import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AQWorkbenchCraft extends AQPlayerCraft
{
	public static final AQWorkbenchCraft instance = new AQWorkbenchCraft();
	public AQCraftCollection morphing = new AQMorphingRecipe.Collection();
	
	public AQWorkbenchCraft()
	{
		super();
		register(this.morphing);
	}
	
	public void initialize()
	{
		super.initialize();
		
		/*
		 * new RecipesDyes().addRecipes(this); this.recipes.add(new
		 * RecipesArmorDyes()); this.recipes.add(new RecipeBookCloning());
		 * this.recipes.add(new RecipesMapCloning()); this.recipes.add(new
		 * RecipesMapExtending()); this.recipes.add(new RecipeFireworks());
		 */
		
		this.shapeless.add(new AQShapelessRecipe(Items.paper, Items.paper, Items.paper, Items.leather), new AQSimpleResult(Items.book));
		this.shapeless.add(new AQShapelessRecipe(Items.book, new ItemStack(Items.dye, 1, 0), Items.feather), new AQSimpleResult(Items.writable_book));
		this.shapeless.add(new AQShapelessRecipe(Items.reeds), new AQSimpleResult(Items.sugar));
		this.shapeless.add(new AQShapelessRecipe(Items.ender_pearl, Items.blaze_powder), new AQSimpleResult(Items.ender_eye));
		this.shapeless.add(new AQShapelessRecipe(Items.gunpowder, Items.blaze_powder, Items.coal), new AQSimpleResult(new ItemStack(Items.fire_charge, 3)));
		this.shapeless.add(new AQShapelessRecipe(Blocks.stone), new AQSimpleResult(Blocks.stone_button));
		this.shapeless.add(new AQShapelessRecipe(Blocks.planks), new AQSimpleResult(Blocks.wooden_button));
		this.shapeless.add(new AQShapelessRecipe(Blocks.brown_mushroom, Blocks.red_mushroom, Items.bowl), new AQSimpleResult(Items.mushroom_stew));
		this.shapeless.add(new AQShapelessRecipe(Blocks.pumpkin, Items.sugar, Items.egg), new AQSimpleResult(Items.pumpkin_pie));
		this.shapeless.add(new AQShapelessRecipe(Items.spider_eye, Blocks.brown_mushroom, Items.sugar), new AQSimpleResult(Items.fermented_spider_eye));
		this.shapeless.add(new AQShapelessRecipe(Items.blaze_rod), new AQSimpleResult(new ItemStack(Items.blaze_powder, 2)));
		this.shapeless.add(new AQShapelessRecipe(Items.blaze_powder, Items.slime_ball), new AQSimpleResult(Items.magma_cream));
		this.shapeless.add(new AQShapelessRecipe(Blocks.pumpkin), new AQSimpleResult(new ItemStack(Items.pumpkin_seeds, 4)));
		this.shapeless.add(new AQShapelessRecipe(Items.melon), new AQSimpleResult(new ItemStack(Items.melon_seeds, 4)));
		
		this.shaped.add(new AQShapedRecipe(2, 1, "00", Blocks.stone), new AQSimpleResult(Blocks.stone_pressure_plate));
		this.shaped.add(new AQShapedRecipe(2, 1, "00", Blocks.planks), new AQSimpleResult(Blocks.wooden_pressure_plate));
		this.shaped.add(new AQShapedRecipe(2, 1, "00", Items.iron_ingot), new AQSimpleResult(Blocks.heavy_weighted_pressure_plate));
		this.shaped.add(new AQShapedRecipe(2, 1, "00", Items.gold_ingot), new AQSimpleResult(Blocks.light_weighted_pressure_plate));
		this.shaped.add(new AQShapedRecipe(2, 1, "01", Blocks.chest, Blocks.tripwire_hook), new AQSimpleResult(Blocks.trapped_chest));
		
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.stone), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 0)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.sandstone), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 1)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.cobblestone), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 3)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.brick_block), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 4)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.stonebrick), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 5)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.nether_brick), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 6)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.quartz_block), new AQSimpleResult(new ItemStack(Blocks.stone_slab, 6, 7)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", new ItemStack(Blocks.planks, 1, 0)), new AQSimpleResult(new ItemStack(Blocks.wooden_slab, 6, 0)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", new ItemStack(Blocks.planks, 1, 1)), new AQSimpleResult(new ItemStack(Blocks.wooden_slab, 6, 1)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", new ItemStack(Blocks.planks, 1, 2)), new AQSimpleResult(new ItemStack(Blocks.wooden_slab, 6, 2)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", new ItemStack(Blocks.planks, 1, 3)), new AQSimpleResult(new ItemStack(Blocks.wooden_slab, 6, 3)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", new ItemStack(Blocks.planks, 1, 4)), new AQSimpleResult(new ItemStack(Blocks.wooden_slab, 6, 4)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", new ItemStack(Blocks.planks, 1, 5)), new AQSimpleResult(new ItemStack(Blocks.wooden_slab, 6, 5)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Blocks.snow), new AQSimpleResult(new ItemStack(Blocks.snow_layer, 6)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Items.reeds), new AQSimpleResult(new ItemStack(Items.paper, 3)));
		this.shaped.add(new AQShapedRecipe(3, 1, "000", Items.wheat), new AQSimpleResult(Items.bread));
		this.shaped.add(new AQShapedRecipe(3, 1, "010", Items.wheat, new ItemStack(Items.dye, 1, 3)), new AQSimpleResult(new ItemStack(Items.cookie, 8)));
		
		this.shaped.add(new AQShapedRecipe(1, 2, "0;0", new ItemStack(Blocks.stone_slab, 1, 1)), new AQSimpleResult(new ItemStack(Blocks.sandstone, 1, 1)));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;0", new ItemStack(Blocks.stone_slab, 1, 7)), new AQSimpleResult(new ItemStack(Blocks.quartz_block, 1, 1)));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;0", new ItemStack(Blocks.quartz_block, 1, 0)), new AQSimpleResult(new ItemStack(Blocks.quartz_block, 2, 2)));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Blocks.pumpkin, Blocks.torch), new AQSimpleResult(Blocks.lit_pumpkin));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Blocks.chest, Items.minecart), new AQSimpleResult(Items.chest_minecart));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Blocks.furnace, Items.minecart), new AQSimpleResult(Items.furnace_minecart));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Blocks.tnt, Items.minecart), new AQSimpleResult(Items.tnt_minecart));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Blocks.hopper, Items.minecart), new AQSimpleResult(Items.hopper_minecart));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Items.slime_ball, Blocks.piston), new AQSimpleResult(Blocks.sticky_piston));
		
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.clay_ball), new AQSimpleResult(Blocks.clay));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.brick), new AQSimpleResult(Blocks.brick_block));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.glowstone_dust), new AQSimpleResult(Blocks.glowstone));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.quartz), new AQSimpleResult(Blocks.quartz_block));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.string), new AQSimpleResult(Blocks.wool));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Blocks.stone), new AQSimpleResult(new ItemStack(Blocks.stonebrick, 4)));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Blocks.sandstone), new AQSimpleResult(new ItemStack(Blocks.sandstone, 4, 2)));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", new ItemStack(Blocks.sand, 1, 0)), new AQSimpleResult(Blocks.sandstone));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.netherbrick), new AQSimpleResult(Blocks.nether_brick));
		
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;2", Blocks.planks, Items.stick, Items.iron_ingot), new AQSimpleResult(new ItemStack(
			Blocks.tripwire_hook,
			2)));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;2", Items.flint, Items.stick, Items.feather), new AQSimpleResult(new ItemStack(Items.arrow, 4)));
		
		this.shaped.add(new AQShapedRecipe(3, 2, " 0 ;111", Items.blaze_rod, Blocks.cobblestone), new AQSimpleResult(Items.brewing_stand));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0; 0 ", Blocks.glass), new AQSimpleResult(new ItemStack(Items.glass_bottle, 3)));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0; 0 ", Items.iron_ingot), new AQSimpleResult(Items.bucket));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0; 0 ", Items.brick), new AQSimpleResult(Items.flower_pot));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0;000", Items.iron_ingot), new AQSimpleResult(Items.minecart));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0;000", Blocks.planks), new AQSimpleResult(Items.boat));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Items.stick), new AQSimpleResult(new ItemStack(Blocks.fence, 2)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Blocks.cobblestone), new AQSimpleResult(new ItemStack(Blocks.cobblestone_wall, 6, 0)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Blocks.mossy_cobblestone), new AQSimpleResult(new ItemStack(Blocks.cobblestone_wall, 6, 1)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Blocks.nether_brick), new AQSimpleResult(new ItemStack(Blocks.nether_brick_fence, 6)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Blocks.planks), new AQSimpleResult(new ItemStack(Blocks.trapdoor, 2)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Items.iron_ingot), new AQSimpleResult(new ItemStack(Blocks.iron_bars, 16)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;000", Blocks.glass), new AQSimpleResult(new ItemStack(Blocks.glass_pane, 16)));
		this.shaped.add(new AQShapedRecipe(3, 2, "000;111", Blocks.wool, Blocks.planks), new AQSimpleResult(Items.bed));
		this.shaped.add(new AQShapedRecipe(3, 2, "010;010", Items.stick, Blocks.planks), new AQSimpleResult(Blocks.fence_gate));
		this.shaped.add(new AQShapedRecipe(3, 2, "010;222", Blocks.redstone_torch, Items.redstone, Blocks.stone), new AQSimpleResult(Items.repeater));
		
		this.shaped.add(new AQShapedRecipe(2, 3, "00;00;00", Blocks.planks), new AQSimpleResult(Items.wooden_door));
		this.shaped.add(new AQShapedRecipe(3, 3, " 0 ;010; 0 ", Items.redstone, Blocks.glowstone), new AQSimpleResult(Blocks.redstone_lamp));
		this.shaped.add(new AQShapedRecipe(3, 3, " 0 ;010;222", Blocks.redstone_torch, Items.quartz, Blocks.stone), new AQSimpleResult(Items.comparator));
		this.shaped.add(new AQShapedRecipe(3, 3, " 0 ;121;222", Items.book, Blocks.obsidian, Items.diamond), new AQSimpleResult(Blocks.enchanting_table));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", new ItemStack(Blocks.planks, 1, 0)), new AQSimpleResult(new ItemStack(Blocks.oak_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", new ItemStack(Blocks.planks, 1, 1)), new AQSimpleResult(new ItemStack(Blocks.spruce_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", new ItemStack(Blocks.planks, 1, 2)), new AQSimpleResult(new ItemStack(Blocks.birch_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", new ItemStack(Blocks.planks, 1, 3)), new AQSimpleResult(new ItemStack(Blocks.jungle_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", new ItemStack(Blocks.planks, 1, 4)), new AQSimpleResult(new ItemStack(Blocks.acacia_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", new ItemStack(Blocks.planks, 1, 5)), new AQSimpleResult(
			new ItemStack(Blocks.dark_oak_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", Blocks.cobblestone), new AQSimpleResult(new ItemStack(Blocks.stone_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", Blocks.brick_block), new AQSimpleResult(new ItemStack(Blocks.brick_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", Blocks.stonebrick), new AQSimpleResult(new ItemStack(Blocks.stone_brick_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", Blocks.nether_brick), new AQSimpleResult(new ItemStack(Blocks.nether_brick_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", Blocks.sandstone), new AQSimpleResult(new ItemStack(Blocks.sandstone_stairs, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0  ;00 ;000", Blocks.quartz_block), new AQSimpleResult(new ItemStack(Blocks.quartz_stairs, 4)));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0; 0 ; 0 ", Blocks.planks), new AQSimpleResult(new ItemStack(Items.bowl, 4)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;0 0;000", Items.iron_ingot), new AQSimpleResult(Items.cauldron));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;010; 0 ", Items.iron_ingot, Blocks.chest), new AQSimpleResult(Blocks.hopper));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;000;0 0", Items.stick), new AQSimpleResult(new ItemStack(Blocks.ladder, 3)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;010;0 0", Items.iron_ingot, Items.stick), new AQSimpleResult(new ItemStack(Blocks.rail, 16)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;010;020", Items.iron_ingot, Items.stick, Items.redstone), new AQSimpleResult(new ItemStack(
			Blocks.golden_rail,
			6)));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;010;020", Items.iron_ingot, Blocks.stone_pressure_plate, Items.redstone), new AQSimpleResult(
			new ItemStack(Blocks.detector_rail, 6)));
		this.shaped.add(new AQShapedRecipe(3, 3, "00 ;01 ;  0", Items.string, Items.slime_ball), new AQSimpleResult(new ItemStack(Items.lead, 2)));
		this.shaped.add(new AQShapedRecipe(3, 3, "000; 1 ;111", Blocks.iron_block, Items.iron_ingot), new AQSimpleResult(Blocks.anvil));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;000", Blocks.planks), new AQSimpleResult(Blocks.chest));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;000", Blocks.cobblestone), new AQSimpleResult(Blocks.furnace));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;010", Blocks.cobblestone, Items.redstone), new AQSimpleResult(Blocks.dropper));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000; 1 ", Blocks.planks, Items.stick), new AQSimpleResult(new ItemStack(Items.sign, 3)));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.melon), new AQSimpleResult(Blocks.melon_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", new ItemStack(Items.dye, 1, 4)), new AQSimpleResult(Blocks.lapis_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.redstone), new AQSimpleResult(Blocks.redstone_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.coal), new AQSimpleResult(Blocks.coal_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.wheat), new AQSimpleResult(Blocks.hay_block));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Blocks.obsidian, Items.ender_eye), new AQSimpleResult(Blocks.ender_chest));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Blocks.planks, Items.diamond), new AQSimpleResult(Blocks.jukebox));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Blocks.planks, Items.redstone), new AQSimpleResult(Blocks.noteblock));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Items.stick, Blocks.wool), new AQSimpleResult(Items.painting));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Items.stick, Items.leather), new AQSimpleResult(Items.item_frame));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Items.gold_ingot, Items.apple), new AQSimpleResult(new ItemStack(Items.golden_apple, 1, 0)));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Blocks.gold_block, Items.apple), new AQSimpleResult(new ItemStack(Items.golden_apple, 1, 1)));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Items.gold_nugget, Items.carrot), new AQSimpleResult(new ItemStack(Items.golden_carrot, 1, 0)));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Items.gold_nugget, Items.melon), new AQSimpleResult(Items.speckled_melon));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Items.paper, Items.compass), new AQSimpleResult(Items.map));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;020", Blocks.cobblestone, Items.bow, Items.redstone), new AQSimpleResult(Blocks.dispenser));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;010;222", Blocks.glass, Items.nether_star, Blocks.obsidian), new AQSimpleResult(Blocks.beacon));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;111;000", Blocks.planks, Items.book), new AQSimpleResult(Blocks.bookshelf));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;111;222", Blocks.glass, Items.quartz, Blocks.wooden_slab), new AQSimpleResult(Blocks.daylight_detector));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;121;131", Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.redstone), new AQSimpleResult(
			Blocks.piston));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;131;222", Items.milk_bucket, Items.sugar, Items.wheat, Items.egg), new AQSimpleResult(Items.cake));
		this.shaped.add(new AQShapedRecipe(3, 3, "010;101;010", Items.gunpowder, Blocks.sand), new AQSimpleResult(Blocks.tnt));
		this.shaped.add(new AQShapedRecipe(3, 3, "020;010;020", Items.iron_ingot, Blocks.redstone_torch, Items.stick), new AQSimpleResult(new ItemStack(
			Blocks.activator_rail,
			6)));
		
		this.morphing.add(new AQFireworkRecipe(), new AQMorphingResult());
		
		// TOOLS
		
		this.shaped.add(new AQShapedRecipe(3, 3, "  0; 01;0 1", Items.stick, Items.string), new AQSimpleResult(Items.fishing_rod));
		
		/*
		 * !!! ShapedRecipes.func_92100_c(); this.shaped.add(new
		 * AQShapedRecipe(2, 2, "0 ; 1", Items.fishing_rod, Items.carrot), new
		 * AQSimpleResult(Items.carrot_on_a_stick));
		 */
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000; 1 ; 1 ", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_pickaxe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00; 1; 1", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;1 ;1 ", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;01; 1", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_axe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;10;1 ", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_axe));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;1", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_shovel));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000; 1 ; 1 ", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_pickaxe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00; 1; 1", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;1 ;1 ", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;01; 1", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_axe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;10;1 ", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_axe));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;1", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_shovel));
		
		// WEAPONS
		
		this.shaped.add(new AQShapedRecipe(1, 3, "0;0;1", Blocks.planks, Items.stick), new AQSimpleResult(Items.wooden_sword));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;0;1", Blocks.cobblestone, Items.stick), new AQSimpleResult(Items.stone_sword));
		this.shaped.add(new AQShapedRecipe(3, 3, " 01;0 1; 01", Items.stick, Items.string), new AQSimpleResult(Items.bow));
		this.shaped.add(new AQShapedRecipe(3, 3, "10 ;1 0;10 ", Items.stick, Items.string), new AQSimpleResult(Items.bow));
		
		// ARMOR
		
		this.shaped.add(new AQShapedRecipe(3, 2, "000;0 0", Items.leather), new AQSimpleResult(Items.leather_helmet));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;000;000", Items.leather), new AQSimpleResult(Items.leather_chestplate));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;0 0", Items.leather), new AQSimpleResult(Items.leather_leggings));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0;0 0", Items.leather), new AQSimpleResult(Items.leather_boots));
		
		// DYES
		
		for(int i = 0; i < 16; ++i)
		{
			this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, i), Blocks.wool), new AQSimpleResult(new ItemStack(
				Blocks.wool,
				1,
				BlockColored.func_150031_c(i))));
			this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Blocks.hardened_clay, new ItemStack(Items.dye, 1, i)), new AQSimpleResult(new ItemStack(
				Blocks.stained_hardened_clay,
				8,
				BlockColored.func_150031_c(i))));
			this.shaped.add(new AQShapedRecipe(3, 3, "000;010;000", Blocks.glass, new ItemStack(Items.dye, 1, i)), new AQSimpleResult(new ItemStack(
				Blocks.stained_glass,
				8,
				BlockColored.func_150031_c(i))));
			this.shaped.add(new AQShapedRecipe(3, 2, "000;000", new ItemStack(Blocks.stained_glass, 1, i)), new AQSimpleResult(new ItemStack(
				Blocks.stained_glass_pane,
				16,
				i)));
			this.shaped.add(new AQShapedRecipe(2, 1, "00", new ItemStack(Blocks.wool, 1, i)), new AQSimpleResult(new ItemStack(Blocks.carpet, 3, i)));
		}
		
		this.shapeless.add(new AQShapelessRecipe(Blocks.yellow_flower), new AQSimpleResult(new ItemStack(Items.dye, 1, 11)));
		this.shapeless.add(new AQShapelessRecipe(Blocks.red_flower), new AQSimpleResult(new ItemStack(Items.dye, 1, 1)));
		this.shapeless.add(new AQShapelessRecipe(Items.bone), new AQSimpleResult(new ItemStack(Items.dye, 3, 15)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 15)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			9)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 11)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			14)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 2), new ItemStack(Items.dye, 1, 15)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			10)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 0), new ItemStack(Items.dye, 1, 15)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			8)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 8), new ItemStack(Items.dye, 1, 15)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			7)));
		this.shapeless.add(
			new AQShapelessRecipe(new ItemStack(Items.dye, 1, 0), new ItemStack(Items.dye, 1, 15), new ItemStack(Items.dye, 1, 15)),
			new AQSimpleResult(new ItemStack(Items.dye, 3, 7)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 15)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			12)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 2)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			6)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 1)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			5)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 5), new ItemStack(Items.dye, 1, 9)), new AQSimpleResult(new ItemStack(
			Items.dye,
			2,
			13)));
		this.shapeless.add(
			new AQShapelessRecipe(new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 9)),
			new AQSimpleResult(new ItemStack(Items.dye, 3, 13)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 1), new ItemStack(
			Items.dye,
			1,
			15)), new AQSimpleResult(new ItemStack(Items.dye, 4, 13)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 1)), new AQSimpleResult(new ItemStack(Items.dye, 1, 12)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 2)), new AQSimpleResult(new ItemStack(Items.dye, 1, 13)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 3)), new AQSimpleResult(new ItemStack(Items.dye, 1, 7)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 4)), new AQSimpleResult(new ItemStack(Items.dye, 1, 1)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 5)), new AQSimpleResult(new ItemStack(Items.dye, 1, 14)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 6)), new AQSimpleResult(new ItemStack(Items.dye, 1, 7)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 7)), new AQSimpleResult(new ItemStack(Items.dye, 1, 9)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.red_flower, 1, 8)), new AQSimpleResult(new ItemStack(Items.dye, 1, 7)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.double_plant, 1, 0)), new AQSimpleResult(new ItemStack(Items.dye, 2, 11)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.double_plant, 1, 1)), new AQSimpleResult(new ItemStack(Items.dye, 2, 13)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.double_plant, 1, 4)), new AQSimpleResult(new ItemStack(Items.dye, 2, 1)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.double_plant, 1, 5)), new AQSimpleResult(new ItemStack(Items.dye, 2, 9)));
	}
}
