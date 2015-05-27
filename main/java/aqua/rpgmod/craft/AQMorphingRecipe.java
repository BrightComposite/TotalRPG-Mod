package aqua.rpgmod.craft;

import net.minecraft.item.ItemStack;

public class AQMorphingRecipe extends AQShapelessRecipe
{
	public static class Collection extends AQCraftCollection
	{
		@Override
		protected boolean accepts(AQRecipe recipe)
		{
			return recipe instanceof AQMorphingRecipe;
		}
		
		@Override
		protected AQRecipe create(int craftWidth, int craftHeight, AQHashItemStack ... items)
		{
			return new AQMorphingRecipe(items);
		}
	}
	
	public ItemStack output;
	
	public AQMorphingRecipe(AQHashItemStack ... items)
	{
		super(items);
		
		this.hash = 0;
	}
	
	@SuppressWarnings(
	{
		"static-method", "unused"
	})
	public boolean equals(AQMorphingRecipe recipe)
	{
		return true;
	}
	
	@Override
	public boolean equals(AQRecipe recipe)
	{
		return recipe != null && recipe instanceof AQMorphingRecipe && ((AQMorphingRecipe) recipe).equals(this);
	}
}
