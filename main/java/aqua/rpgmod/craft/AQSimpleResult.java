package aqua.rpgmod.craft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AQSimpleResult extends AQCraftResult
{
	public AQSimpleResult(ItemStack result)
	{
		this.result = result;
	}
	
	public AQSimpleResult(Item result)
	{
		this.result = new ItemStack(result);
	}
	
	public AQSimpleResult(Block result)
	{
		this.result = new ItemStack(result);
	}
}
