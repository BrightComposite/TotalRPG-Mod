package aqua.rpgmod.craft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AQShapelessRecipe extends AQRecipe
{
	public static class Collection extends AQCraftCollection
	{
		@Override
		protected boolean accepts(AQRecipe recipe)
		{
			return recipe instanceof AQShapelessRecipe;
		}
		
		@Override
		protected AQRecipe create(int craftWidth, int craftHeight, AQHashItemStack ... items)
		{
			return new AQShapelessRecipe(items);
		}
	}
	
	public AQShapelessRecipe(Object ... items)
	{
		setContents(transform(items));
	}
	
	public AQShapelessRecipe(AQHashItemStack ... items)
	{
		setContents(items);
	}
	
	static AQHashItemStack[] transform(Object ... items)
	{
		List<AQHashItemStack> buffer = new ArrayList();
		
		for(int i = 0; i < items.length; ++i)
		{
			AQHashItemStack stack = AQHashItemStack.createHashStackFor(items[i]);
			
			if(stack != null)
				buffer.add(stack);
		}
		
		return buffer.toArray(new AQHashItemStack[0]);
	}
	
	@Override
	public void normalize()
	{
		List<AQHashItemStack> buffer = new ArrayList();
		
		for(int i = 0; i < this.contents.length; i++)
			if(this.contents[i].id > 0)
				buffer.add(this.contents[i]);
		
		this.contents = buffer.toArray(new AQHashItemStack[0]);
		Arrays.sort(this.contents);
	}
}
