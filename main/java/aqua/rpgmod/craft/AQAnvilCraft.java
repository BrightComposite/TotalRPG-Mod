package aqua.rpgmod.craft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AQAnvilCraft extends AQCraft
{
	public static final AQAnvilCraft instance = new AQAnvilCraft();
	public AQCraftCollection shaped = new AQShapedRecipe.Collection();
	public AQCraftCollection shapeless = new AQShapelessRecipe.Collection();
	
	public AQAnvilCraft()
	{
		register(this.shaped);
		register(this.shapeless);
	}
	
	public void initialize()
	{
		this.shaped.add(new AQShapedRecipe(2, 3, "00;00;00", Items.iron_ingot), new AQSimpleResult(Items.iron_door));
		
		// MATERIAL BLOCKS
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.iron_ingot), new AQSimpleResult(Blocks.iron_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.gold_ingot), new AQSimpleResult(Blocks.gold_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.diamond), new AQSimpleResult(Blocks.diamond_block));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;000;000", Items.emerald), new AQSimpleResult(Blocks.emerald_block));
		
		// REPAIR
		
		this.shapeless.add(new AQShapelessRecipe(Items.shears, Items.iron_ingot), new AQRepairResult(Items.shears, 238, 64));
		
		this.shapeless.add(new AQShapelessRecipe(Items.iron_pickaxe, Items.iron_ingot), new AQRepairResult(
			Items.iron_pickaxe,
			ToolMaterial.IRON.getMaxUses() / 3,
			64));
		this.shapeless.add(
			new AQShapelessRecipe(Items.golden_pickaxe, Items.gold_ingot),
			new AQRepairResult(Items.golden_pickaxe, ToolMaterial.GOLD.getMaxUses() / 3, 64));
		this.shapeless.add(
			new AQShapelessRecipe(Items.diamond_pickaxe, Items.diamond),
			new AQRepairResult(Items.diamond_pickaxe, ToolMaterial.EMERALD.getMaxUses() / 3, 80));
		
		this.shapeless.add(
			new AQShapelessRecipe(Items.iron_shovel, Items.iron_ingot),
			new AQRepairResult(Items.iron_shovel, ToolMaterial.IRON.getMaxUses(), 64));
		this.shapeless.add(new AQShapelessRecipe(Items.golden_shovel, Items.gold_ingot), new AQRepairResult(
			Items.golden_shovel,
			ToolMaterial.GOLD.getMaxUses(),
			64));
		this.shapeless.add(
			new AQShapelessRecipe(Items.diamond_shovel, Items.diamond),
			new AQRepairResult(Items.diamond_shovel, ToolMaterial.EMERALD.getMaxUses(), 80));
		
		this.shapeless.add(new AQShapelessRecipe(Items.iron_axe, Items.iron_ingot), new AQRepairResult(Items.iron_axe, ToolMaterial.IRON.getMaxUses() / 3, 64));
		this.shapeless.add(new AQShapelessRecipe(Items.golden_axe, Items.gold_ingot), new AQRepairResult(
			Items.golden_axe,
			ToolMaterial.GOLD.getMaxUses() / 3,
			64));
		this.shapeless.add(new AQShapelessRecipe(Items.diamond_axe, Items.diamond), new AQRepairResult(
			Items.diamond_axe,
			ToolMaterial.EMERALD.getMaxUses() / 3,
			80));
		
		this.shapeless.add(new AQShapelessRecipe(Items.iron_hoe, Items.iron_ingot), new AQRepairResult(Items.iron_hoe, ToolMaterial.IRON.getMaxUses(), 32));
		this.shapeless.add(new AQShapelessRecipe(Items.golden_hoe, Items.gold_ingot), new AQRepairResult(Items.golden_hoe, ToolMaterial.GOLD.getMaxUses(), 32));
		this.shapeless.add(
			new AQShapelessRecipe(Items.diamond_hoe, Items.diamond),
			new AQRepairResult(Items.diamond_hoe, ToolMaterial.EMERALD.getMaxUses(), 40));
		
		this.shapeless.add(new AQShapelessRecipe(Items.iron_sword, Items.iron_ingot), new AQRepairResult(
			Items.iron_sword,
			ToolMaterial.IRON.getMaxUses() / 2,
			64));
		this.shapeless.add(new AQShapelessRecipe(Items.golden_sword, Items.gold_ingot), new AQRepairResult(
			Items.golden_sword,
			ToolMaterial.GOLD.getMaxUses() / 2,
			64));
		this.shapeless.add(new AQShapelessRecipe(Items.diamond_sword, Items.diamond), new AQRepairResult(
			Items.diamond_sword,
			ToolMaterial.EMERALD.getMaxUses() / 2,
			80));
		
		this.shapeless.add(
			new AQShapelessRecipe(Items.iron_helmet, Items.iron_ingot),
			new AQRepairResult(Items.iron_helmet, ItemArmor.ArmorMaterial.IRON.getDurability(0) * 2 / 5, 80));
		this.shapeless.add(new AQShapelessRecipe(Items.iron_chestplate, Items.iron_ingot), new AQRepairResult(
			Items.iron_chestplate,
			ItemArmor.ArmorMaterial.IRON.getDurability(1) / 4,
			128));
		this.shapeless.add(
			new AQShapelessRecipe(Items.iron_leggings, Items.iron_ingot),
			new AQRepairResult(Items.iron_leggings, ItemArmor.ArmorMaterial.IRON.getDurability(2) * 2 / 7, 112));
		this.shapeless.add(
			new AQShapelessRecipe(Items.iron_boots, Items.iron_ingot),
			new AQRepairResult(Items.iron_boots, ItemArmor.ArmorMaterial.IRON.getDurability(3) / 2, 64));
		
		this.shapeless.add(
			new AQShapelessRecipe(Items.golden_helmet, Items.gold_ingot),
			new AQRepairResult(Items.golden_helmet, ItemArmor.ArmorMaterial.GOLD.getDurability(0) * 2 / 5, 80));
		this.shapeless.add(new AQShapelessRecipe(Items.golden_chestplate, Items.gold_ingot), new AQRepairResult(
			Items.golden_chestplate,
			ItemArmor.ArmorMaterial.GOLD.getDurability(1) / 4,
			128));
		this.shapeless.add(new AQShapelessRecipe(Items.golden_leggings, Items.gold_ingot), new AQRepairResult(
			Items.golden_leggings,
			ItemArmor.ArmorMaterial.GOLD.getDurability(2) * 2 / 7,
			112));
		this.shapeless.add(
			new AQShapelessRecipe(Items.golden_boots, Items.gold_ingot),
			new AQRepairResult(Items.golden_boots, ItemArmor.ArmorMaterial.GOLD.getDurability(3) / 2, 64));
		
		this.shapeless.add(
			new AQShapelessRecipe(Items.diamond_helmet, Items.diamond),
			new AQRepairResult(Items.diamond_helmet, ItemArmor.ArmorMaterial.DIAMOND.getDurability(0) * 2 / 5, 100));
		this.shapeless.add(new AQShapelessRecipe(Items.diamond_chestplate, Items.diamond), new AQRepairResult(
			Items.diamond_chestplate,
			ItemArmor.ArmorMaterial.DIAMOND.getDurability(1) / 4,
			160));
		this.shapeless.add(new AQShapelessRecipe(Items.diamond_leggings, Items.diamond), new AQRepairResult(
			Items.diamond_leggings,
			ItemArmor.ArmorMaterial.DIAMOND.getDurability(2) * 2 / 7,
			140));
		this.shapeless.add(
			new AQShapelessRecipe(Items.diamond_boots, Items.diamond),
			new AQRepairResult(Items.diamond_boots, ItemArmor.ArmorMaterial.DIAMOND.getDurability(3) / 2, 80));
		
		// TOOLS
		
		this.shaped.add(new AQShapedRecipe(3, 3, " 0 ;010; 0 ", Items.gold_ingot, Items.redstone), new AQSimpleResult(Items.clock));
		this.shaped.add(new AQShapedRecipe(3, 3, " 0 ;010; 0 ", Items.iron_ingot, Items.redstone), new AQSimpleResult(Items.compass));
		
		this.shaped.add(new AQShapedRecipe(2, 2, " 0;0 ", Items.iron_ingot), new AQSimpleResult(Items.shears));
		this.shapeless.add(new AQShapelessRecipe(Items.iron_ingot, Items.flint), new AQSimpleResult(new ItemStack(Items.flint_and_steel)));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000; 1 ; 1 ", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_pickaxe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00; 1; 1", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;1 ;1 ", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;01; 1", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_axe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;10;1 ", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_axe));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;1", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_shovel));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000; 1 ; 1 ", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_pickaxe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00; 1; 1", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;1 ;1 ", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;01; 1", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_axe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;10;1 ", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_axe));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;1", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_shovel));
		
		this.shaped.add(new AQShapedRecipe(3, 3, "000; 1 ; 1 ", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_pickaxe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00; 1; 1", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;1 ;1 ", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_hoe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;01; 1", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_axe));
		this.shaped.add(new AQShapedRecipe(2, 3, "00;10;1 ", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_axe));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;1;1", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_shovel));
		
		// WEAPONS
		
		this.shaped.add(new AQShapedRecipe(1, 3, "0;0;1", Items.iron_ingot, Items.stick), new AQSimpleResult(Items.iron_sword));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;0;1", Items.gold_ingot, Items.stick), new AQSimpleResult(Items.golden_sword));
		this.shaped.add(new AQShapedRecipe(1, 3, "0;0;1", Items.diamond, Items.stick), new AQSimpleResult(Items.diamond_sword));
		
		// ARMOR
		
		this.shaped.add(new AQShapedRecipe(3, 2, "000;0 0", Items.iron_ingot), new AQSimpleResult(Items.iron_helmet));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;000;000", Items.iron_ingot), new AQSimpleResult(Items.iron_chestplate));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;0 0", Items.iron_ingot), new AQSimpleResult(Items.iron_leggings));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0;0 0", Items.iron_ingot), new AQSimpleResult(Items.iron_boots));
		
		this.shaped.add(new AQShapedRecipe(3, 2, "000;0 0", Items.gold_ingot), new AQSimpleResult(Items.golden_helmet));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;000;000", Items.gold_ingot), new AQSimpleResult(Items.golden_chestplate));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;0 0", Items.gold_ingot), new AQSimpleResult(Items.golden_leggings));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0;0 0", Items.gold_ingot), new AQSimpleResult(Items.golden_boots));
		
		this.shaped.add(new AQShapedRecipe(3, 2, "000;0 0", Items.diamond), new AQSimpleResult(Items.diamond_helmet));
		this.shaped.add(new AQShapedRecipe(3, 3, "0 0;000;000", Items.diamond), new AQSimpleResult(Items.diamond_chestplate));
		this.shaped.add(new AQShapedRecipe(3, 3, "000;0 0;0 0", Items.diamond), new AQSimpleResult(Items.diamond_leggings));
		this.shaped.add(new AQShapedRecipe(3, 2, "0 0;0 0", Items.diamond), new AQSimpleResult(Items.diamond_boots));
	}
}
