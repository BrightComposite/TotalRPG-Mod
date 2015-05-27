package aqua.rpgmod.craft;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

abstract public class AQCraftCollection
{
	protected HashMap<AQRecipe, AQCraftResult> recipes = new HashMap<AQRecipe, AQCraftResult>();
	
	abstract protected boolean accepts(AQRecipe recipe);
	
	abstract protected AQRecipe create(int craftWidth, int craftHeight, AQHashItemStack ... items);
	
	public AQCraftResult get(EntityPlayer player, int width, int height, AQHashItemStack ... items)
	{
		AQRecipe recipe = create(width, height, items);
		AQCraftResult result = this.recipes.get(recipe);
		
		return (result != null) ? result.apply(recipe, player) : null;
	}
	
	public boolean add(AQRecipe recipe, AQCraftResult result)
	{
		if(!accepts(recipe))
			return false;
		
		result.setRecipe(recipe);
		this.recipes.put(recipe, result);
		return true;
	}
}
