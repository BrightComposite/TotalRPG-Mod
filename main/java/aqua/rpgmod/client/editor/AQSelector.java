package aqua.rpgmod.client.editor;

public interface AQSelector
{
	public void next();
	
	public void previous();
	
	public boolean hasNext();
	
	public boolean hasPrevious();
	
	public boolean hasVariants();
	
	public int getType();
}
