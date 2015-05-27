package aqua.rpgmod.craft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AQMorphingResult extends AQCraftResult
{
	@Override
	public AQCraftResult apply(AQRecipe recipe, EntityPlayer player)
	{
		this.result = null;
		
		if(recipe instanceof AQMorphingRecipe)
		{
			this.result = ((AQMorphingRecipe) recipe).output;
		}
		
		return this;
	}
	
	@Override
	public ItemStack get()
	{
		return this.result;
	}
	
}
