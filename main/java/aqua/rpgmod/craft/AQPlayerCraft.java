package aqua.rpgmod.craft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AQPlayerCraft extends AQCraft
{
	public static final AQPlayerCraft instance = new AQPlayerCraft();
	public AQCraftCollection shaped = new AQShapedRecipe.Collection();
	public AQCraftCollection shapeless = new AQShapelessRecipe.Collection();
	
	public AQPlayerCraft()
	{
		register(this.shaped);
		register(this.shapeless);
	}
	
	public void initialize()
	{
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.log, 1, 0)), new AQSimpleResult(new ItemStack(Blocks.planks, 4, 0)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.log, 1, 1)), new AQSimpleResult(new ItemStack(Blocks.planks, 4, 1)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.log, 1, 2)), new AQSimpleResult(new ItemStack(Blocks.planks, 4, 2)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.log, 1, 3)), new AQSimpleResult(new ItemStack(Blocks.planks, 4, 3)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.log2, 1, 0)), new AQSimpleResult(new ItemStack(Blocks.planks, 4, 4)));
		this.shapeless.add(new AQShapelessRecipe(new ItemStack(Blocks.log2, 1, 1)), new AQSimpleResult(new ItemStack(Blocks.planks, 4, 5)));
		
		this.shaped.add(new AQShapedRecipe(1, 2, "0;0", Blocks.planks), new AQSimpleResult(new ItemStack(Items.stick, 4)));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Items.coal, Items.stick), new AQSimpleResult(new ItemStack(Blocks.torch, 4)));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Items.stick, Blocks.cobblestone), new AQSimpleResult(Blocks.lever));
		this.shaped.add(new AQShapedRecipe(1, 2, "0;1", Items.redstone, Items.stick), new AQSimpleResult(Blocks.redstone_torch));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Items.snowball), new AQSimpleResult(Blocks.snow));
		this.shaped.add(new AQShapedRecipe(2, 2, "00;00", Blocks.planks), new AQSimpleResult(Blocks.crafting_table));
	}
}
