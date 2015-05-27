package aqua.rpgmod.craft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AQHashItemStack implements Comparable<AQHashItemStack>
{
	enum ContentType
	{
		ITEM,
		BLOCK,
		STACK;
	}
	
	public final Object contents;
	protected final ContentType type;
	protected final int id;
	
	public AQHashItemStack(ItemStack stack)
	{
		this.contents = stack;
		this.type = ContentType.STACK;
		this.id = (stack != null) ? Item.getIdFromItem(stack.getItem()) : 0;
	}
	
	public AQHashItemStack(Item item)
	{
		this.contents = item;
		this.type = ContentType.ITEM;
		this.id = (item != null) ? Item.getIdFromItem(item) : 0;
	}
	
	public AQHashItemStack(Block block)
	{
		this.contents = block;
		this.type = ContentType.BLOCK;
		this.id = (block != null) ? Block.getIdFromBlock(block) : 0;
	}
	
	public static AQHashItemStack createHashStackFor(Object obj)
	{
		if(obj instanceof Block)
			return new AQHashItemStack((Block) obj);
		
		if(obj instanceof Item)
			return new AQHashItemStack((Item) obj);
		
		if(obj instanceof ItemStack)
			return new AQHashItemStack((ItemStack) obj);
		
		return null;
	}
	
	public ItemStack getStack()
	{
		switch(this.type)
		{
			case BLOCK:
				return new ItemStack((Block) this.contents);
			case ITEM:
				return new ItemStack((Item) this.contents);
			default:
				return ((ItemStack) this.contents).copy();
		}
	}
	
	public boolean equals(AQHashItemStack his)
	{
		return this.id == his.id && ((this.contents instanceof ItemStack && his.contents instanceof ItemStack) ? areContentsDamageEqual(his) : true);
	}
	
	boolean areContentsDamageEqual(AQHashItemStack his)
	{
		if(this.contents == null || his.contents == null)
			return his.contents == this.contents;
		
		return ((ItemStack) this.contents).getItemDamage() == ((ItemStack) his.contents).getItemDamage();
	}
	
	@Override
	public int compareTo(AQHashItemStack o)
	{
		return this.id > o.id ? 1 : this.id < o.id ? -1 : 0;
	}
}
