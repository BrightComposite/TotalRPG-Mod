package aqua.rpgmod.craft;

abstract public class AQRecipe
{
	public AQHashItemStack contents[] = null;
	public int hash = 0;
	
	public void setContents(AQHashItemStack ... items)
	{
		this.contents = items;
		normalize();
		this.hash = AQCraft.generateHash(this.contents);
	}
	
	@Override
	public int hashCode()
	{
		return this.hash;
	}
	
	public boolean equals(AQRecipe recipe)
	{
		if(this.contents.length != recipe.contents.length)
			return false;
		
		for(int i = 0; i < this.contents.length; i++)
		{
			if(!this.contents[i].equals(recipe.contents[i]))
				return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof AQRecipe == false)
			return false;
		
		return equals((AQRecipe) obj);
	}
	
	abstract public void normalize();
}
