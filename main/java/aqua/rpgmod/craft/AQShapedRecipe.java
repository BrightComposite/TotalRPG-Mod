package aqua.rpgmod.craft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AQShapedRecipe extends AQRecipe
{
	public static class Collection extends AQCraftCollection
	{
		@Override
		protected boolean accepts(AQRecipe recipe)
		{
			return recipe instanceof AQShapedRecipe;
		}
		
		@Override
		protected AQRecipe create(int craftWidth, int craftHeight, AQHashItemStack ... items)
		{
			return new AQShapedRecipe(craftWidth, craftHeight, items);
		}
	}
	
	public int width;
	public int height;
	
	public AQShapedRecipe(int width, int height, String mask, Object ... items)
	{
		this.width = width;
		this.height = height;
		
		readRecipeFromMask(mask, items);
	}
	
	public AQShapedRecipe(int width, int height, AQHashItemStack ... items)
	{
		this.width = width;
		this.height = height;
		
		setContents(items);
	}
	
	@Override
	public boolean equals(AQRecipe recipe)
	{
		if(recipe instanceof AQShapedRecipe == false)
			return false;
		
		AQShapedRecipe shaped = (AQShapedRecipe) recipe;
		
		if(this.width != shaped.width || this.height != shaped.height)
			return false;
		
		return super.equals(shaped);
	}
	
	protected void readRecipeFromMask(String mask, Object ... items)
	{
		AQHashItemStack[] buffer = AQCraft.genBuffer(this.width, this.height);
		
		for(int i = 0, j = 0; i < buffer.length && j < mask.length(); i++, j++)
		{
			char c = mask.charAt(j);
			
			if(!Character.isDigit(c))
			{
				if(c == ';')
					i--;
				
				continue;
			}
			
			int index = Integer.valueOf(String.valueOf(c)).intValue();
			
			while(true)
			{
				if(index >= items.length)
				{
					new Exception("Uncorrect item index at recipe!").printStackTrace();
					return;
				}
				
				AQHashItemStack stack = AQHashItemStack.createHashStackFor(items[index]);
				
				if(stack != null)
				{
					buffer[i] = stack;
					break;
				}
				
				new Exception("Strange item in recipe!").printStackTrace();
				
				List list = new ArrayList(Arrays.asList(items));
				list.remove(index);
				
				items = list.toArray();
			}
		}
		
		setContents(buffer);
	}
	
	public int firstRow()
	{
		for(int y = 0; y < this.height; y++)
			for(int x = 0; x < this.width; x++)
			{
				if(this.contents[y * this.width + x].id != 0)
					return y;
			}
		
		return this.height;
	}
	
	public int firstColumn()
	{
		for(int x = 0; x < this.width; x++)
			for(int y = 0; y < this.height; y++)
			{
				if(this.contents[y * this.width + x].id != 0)
					return x;
			}
		
		return this.width;
	}
	
	public int lastRow()
	{
		for(int y = this.height - 1; y >= 0; y--)
			for(int x = 0; x < this.width; x++)
			{
				if(this.contents[y * this.width + x].id != 0)
					return y;
			}
		
		return 0;
	}
	
	public int lastColumn()
	{
		for(int x = this.width - 1; x >= 0; x--)
			for(int y = 0; y < this.height; y++)
			{
				if(this.contents[y * this.width + x].id != 0)
					return x;
			}
		
		return 0;
	}
	
	@Override
	public void normalize()
	{
		int offsetX = firstColumn();
		int offsetY = firstRow();
		int maxX = lastColumn() + 1;
		int maxY = lastRow() + 1;
		
		if(offsetX == this.width || offsetY == this.height)
		{
			this.contents = new AQHashItemStack[] {};
			return;
		}
		
		AQHashItemStack[] buffer = AQCraft.genBuffer(maxX - offsetX, maxY - offsetY);
		
		for(int y = offsetY, ny = 0; y < maxY; y++, ny++)
			for(int x = offsetX, nx = 0; x < maxX; x++, nx++)
				buffer[ny * (maxX - offsetX) + nx] = this.contents[y * this.width + x];
		
		this.width = maxX - offsetX;
		this.height = maxY - offsetY;
		
		this.contents = buffer;
	}
	
}
